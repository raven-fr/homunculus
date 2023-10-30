package com.mondecitronne.homunculus;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerSkin {
	private GameProfile playerProfile;
	private boolean skinLoaded;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;
    
    public static void initProfileCache() {
    	AuthenticationService authenticationService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
    	sessionService = authenticationService.createMinecraftSessionService();
    	GameProfileRepository profileRepository = authenticationService.createProfileRepository();
    	profileCache = new PlayerProfileCache(profileRepository, new File(Minecraft.getMinecraft().gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
    }
    
    public PlayerSkin(GameProfile profile) {
    	playerProfile = profile;
    }
    
    public void loadSkin() {
    	if (!skinLoaded) {
    		playerProfile = populateGameProfile(playerProfile);
    		skinLoaded = true;
    	}
    }
	
	public GameProfile getPlayerProfile() {
		assert(playerProfile != null);
		return playerProfile;
	}
	
	protected MinecraftProfileTexture getProfileTexture() {
		Minecraft minecraft = Minecraft.getMinecraft();
		Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(playerProfile);
		if (map != null) {
			return map.get(Type.SKIN);
		} else {
			return null;
		}
	}

	@Nullable
	public String getModelType() {
		if (getTexture() != null) {
			MinecraftProfileTexture tex = getProfileTexture();
			if (tex != null && tex.getMetadata("model") != null) {
				return tex.getMetadata("model");
			}
		}
		return null;
	}
	
	@Nullable
	public ResourceLocation getTexture() {
		MinecraftProfileTexture tex = getProfileTexture();
		if (tex != null) {
			ResourceLocation location = Minecraft.getMinecraft().getSkinManager().loadSkin(tex, Type.SKIN);
			if (location != null) {
				return location;
			}
		}
		return null;
	}
	
    private static GameProfile populateGameProfile(GameProfile input) {
        if (input != null) {
            if (input.isComplete() && input.getProperties().containsKey("textures")) {
                return input;
            } else if (profileCache != null && sessionService != null) {
                GameProfile gameProfile = null;
                if (!StringUtils.isNullOrEmpty(input.getName())) {
                	gameProfile = profileCache.getGameProfileForUsername(input.getName());
                	if (input.getId() != null && !input.getId().equals(gameProfile.getId())) {
                		return input;
                	}
                }

                if (gameProfile == null) {
                    return input;
                } else {
                    Property property = (Property)Iterables.getFirst(gameProfile.getProperties().get("textures"), (Object)null);

                    if (property == null)  {
                        gameProfile = sessionService.fillProfileProperties(gameProfile, true);
                        if (gameProfile == null) {
                        	return input;
                        }
                    }

                    return gameProfile;
                }
            } else {
                return input;
            }
        } else {
            return input;
        }
    }
}
