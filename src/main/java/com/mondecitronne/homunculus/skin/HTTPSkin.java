package com.mondecitronne.homunculus.skin;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;

public class HTTPSkin extends Skin {
	private final String url;
	private final String modelType;
	public HTTPSkin(String skin_url, String modelType) {
		url = skin_url;
		this.modelType = modelType;
	}
	
	@Override
	public boolean isLoaded() {
		return false;
	}
	
	@Override
	@Nullable
	public String getModelType() {
		return modelType;
	}

	@Override
	@Nullable
	public ResourceLocation getTexture() {
		return null;
	}

	public String getUrl() {
		return url;
	}
}
