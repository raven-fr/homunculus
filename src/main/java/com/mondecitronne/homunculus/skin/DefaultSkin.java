package com.mondecitronne.homunculus.skin;

import java.util.UUID;

import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class DefaultSkin extends Skin {
	private final UUID id;

	public DefaultSkin(UUID id) {
		this.id = id;
	}

	@Override
	public String getModelType() {
		return DefaultPlayerSkin.getSkinType(id);
	}

	@Override
	public ResourceLocation getTexture() {
		return DefaultPlayerSkin.getDefaultSkin(id);
	}
}
