package com.mondecitronne.homunculus.skin;

import java.util.Map;
import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mondecitronne.homunculus.GameProfileFetcher;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class PlayerSkin extends Skin {
	private final GameProfile profile;
	private final GameProfileFetcher profileFetcher;
	private final boolean isRemote;
    
    public PlayerSkin(GameProfile profile, boolean isRemote) {
    	this.profile = profile;
    	profileFetcher = new GameProfileFetcher(this.profile);
    	this.isRemote = isRemote;
    }
    
    @Override
    public boolean isLoaded() {
    	if (this.isRemote) {
    		return getTexture() != null;
    	} else {
    		return profileFetcher.getGameProfile() != null;
    	}
    }

	public GameProfile getPlayerProfile() {
    	GameProfile fetchProfile = profileFetcher.getGameProfile();
    	if (fetchProfile != null) {
    		return profileFetcher.getGameProfile();
    	} else {
    		return profile;
    	}
	}

	protected MinecraftProfileTexture getProfileTexture() {
		assert(this.isRemote);
		GameProfile playerProfile = getPlayerProfile();
		if (playerProfile != null) {
			Minecraft minecraft = Minecraft.getMinecraft();
			Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(playerProfile);
			if (map != null) {
				return map.get(Type.SKIN);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getModelType() {
		assert(this.isRemote);
		if (getTexture() != null) {
			MinecraftProfileTexture tex = getProfileTexture();
			if (tex != null && tex.getMetadata("model") != null) {
				return tex.getMetadata("model");
			}
		}
		return null;
	}
	
	@Override
	@Nullable
	public ResourceLocation getTexture() {
		assert(this.isRemote);
		MinecraftProfileTexture tex = getProfileTexture();
		if (tex != null) {
			ResourceLocation location = Minecraft.getMinecraft().getSkinManager().loadSkin(tex, Type.SKIN);
			if (location != null) {
				return location;
			}
		}
		return null;
	}
}
