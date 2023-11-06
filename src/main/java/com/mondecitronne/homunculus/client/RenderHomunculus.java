package com.mondecitronne.homunculus.client;

import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.mondecitronne.homunculus.EntityHomunculus;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderHomunculus extends RenderLiving<EntityHomunculus> {
	public static final Factory FACTORY = new Factory();
	private final Map<String, ModelPlayer> modelTypes = Maps.<String, ModelPlayer>newHashMap();

	public RenderHomunculus(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelPlayer(0.0F, false), 0.5F);
		modelTypes.put("default", new ModelPlayer(0.0F, false));
		modelTypes.put("slim", new ModelPlayer(0.0F, true));
	}

	@Nonnull
	public String getModelType(EntityHomunculus entity) {
		return "default";
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityHomunculus entity) {
		return entity.getSkin().getTexture();
	}

	@Override
	public void doRender(EntityHomunculus entity, double x, double y, double z, float entityYaw, float partialTicks) {
		mainModel = modelTypes.get(entity.getSkin().getModelType());
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public static class Factory implements IRenderFactory<EntityHomunculus> {
		@Override
		public Render<? super EntityHomunculus> createRenderFor(RenderManager manager) {
			return new RenderHomunculus(manager);
		}
	}
}
