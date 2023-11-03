package com.mondecitronne.homunculus.client;

import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.mondecitronne.homunculus.EntityHomunculus;
import com.mondecitronne.homunculus.skin.Skin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderHomunculus extends RenderLivingBase<EntityHomunculus> {
	public static final Factory FACTORY = new Factory();
	private final Map<String, ModelPlayer> modelTypes = Maps.<String, ModelPlayer>newHashMap();
	
	public RenderHomunculus(RenderManager rendermanagerIn) { 
		super(rendermanagerIn, new ModelPlayer(0.0F, false), 0.5F);
		modelTypes.put("default", new ModelPlayer(0.0F, false));
        modelTypes.put("slim", new ModelPlayer(0.0F, true));
	}
	
	@Nonnull
	public String getModelType(EntityHomunculus entity) {
		String modelType = DefaultPlayerSkin.getSkinType(entity.getUniqueID());
		Skin skin = entity.getSkin();
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
		Skin skin = entity.getSkin();
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
