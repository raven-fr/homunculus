package com.mondecitronne.homunculus.skin;

import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;

public abstract class Skin {
	public boolean isLoaded() {
		return true;
	}
	
	public void dispatchFetch() {
	}

	@Nullable
	abstract public String getModelType();
	
	@Nullable
	abstract public ResourceLocation getTexture();
}
