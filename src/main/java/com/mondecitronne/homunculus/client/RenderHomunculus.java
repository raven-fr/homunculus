package com.mondecitronne.homunculus.client;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mondecitronne.homunculus.EntityHomunculus;
import com.mondecitronne.homunculus.PlayerSkin;
import com.mondecitronne.homunculus.proxy.SkinHandlerClientProxy;
import com.mondecitronne.homunculus.proxy.SkinHandlerProxy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderHomunculus extends RenderLivingBase<EntityHomunculus> {
	public static final Factory FACTORY = new Factory();
	private final Map<String, ModelPlayer> modelTypes = Maps.<String, ModelPlayer>newHashMap();
	
	public RenderHomunculus(RenderManager rendermanagerIn) { 
		super(rendermanagerIn, new ModelPlayer(0.0F, false), 0.5F);
		modelTypes.put("default", new ModelPlayer(0.0F, false));
        modelTypes.put("slim", new ModelPlayer(0.0F, true));
	}
	
	@Nullable
	protected static PlayerSkin getSkin(EntityHomunculus entity) {
		SkinHandlerProxy.SkinOwner owner = entity.getSkinOwner();
		if (owner != null) {
			PlayerSkin skin = ((SkinHandlerClientProxy.SkinOwner) entity.getSkinOwner()).getPlayerSkin();
			skin.loadSkin();
			return skin;
		} else {
			return null;
		}
	}
	
	@Nonnull
	public String getModelType(EntityHomunculus entity) {
		String modelType = DefaultPlayerSkin.getSkinType(entity.getUniqueID());
		PlayerSkin skin = getSkin(entity);
		if (skin != null) {
			String type = skin.getModelType();
			if (type != null) {
				modelType = type;
			}
		}
		return modelType;
	}
	
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityHomunculus entity) {
		ResourceLocation texture = DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID());
		PlayerSkin skin = getSkin(entity);
		if (skin != null) {
			ResourceLocation tex = skin.getTexture();
			if (tex != null) {
				texture = tex;
			}
		}
		return texture;
	}
	
	@Override
	public void doRender(EntityHomunculus entity, double x, double y, double z, float entityYaw, float partialTicks) {
		mainModel = modelTypes.get(getModelType(entity));
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected boolean canRenderName(EntityHomunculus entity) {
		return super.canRenderName(entity) && entity.hasCustomName();
	}

	public static class Factory implements IRenderFactory<EntityHomunculus> {
		@Override
		public Render<? super EntityHomunculus> createRenderFor(RenderManager manager) {
			return new RenderHomunculus(manager);
		}
	}
}
