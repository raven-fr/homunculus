package com.mondecitronne.homunculus.skin;

import com.mondecitronne.homunculus.Homunculus;

import net.minecraft.util.ResourceLocation;

public class FallbackSkin extends Skin {
	static final ResourceLocation FALLBACK_TEXTURE = new ResourceLocation(Homunculus.MODID, "entities/homunculus_fallback");
	
	@Override
	public String getModelType() {
		return "default";
	}

	@Override
	public ResourceLocation getTexture() {
		return FALLBACK_TEXTURE;
	}

}
