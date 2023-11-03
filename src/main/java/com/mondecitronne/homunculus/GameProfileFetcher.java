package com.mondecitronne.homunculus;

import javax.annotation.Nullable;
import com.mojang.authlib.GameProfile;

public class GameProfileFetcher {
	@Nullable
	private GameProfile profile;
    
    public static void initProfileCache() {
    }
	
	public GameProfileFetcher(GameProfile profileIn) {
		dispatchFetchProfile(profileIn);
	}
	
	@Nullable
	public GameProfile getGameProfile() {
		synchronized (this) {
			return profile;
		}
	}
	
    private static void dispatchFetchProfile(GameProfile profileIn) {
    }
}
