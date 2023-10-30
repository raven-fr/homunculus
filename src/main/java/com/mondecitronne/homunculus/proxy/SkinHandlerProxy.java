package com.mondecitronne.homunculus.proxy;

import com.mojang.authlib.GameProfile;

public class SkinHandlerProxy {
	public class SkinOwner {
		private GameProfile playerProfile;
		
		SkinOwner(GameProfile playerProfileIn) {
			playerProfile = playerProfileIn;
		}
		
		public GameProfile getPlayerProfile() {
			return playerProfile;
		}
		
		public void loadSkin() {
		}
	}
	
	public SkinOwner createSkinOwner(GameProfile playerProfile) {
		return new SkinOwner(playerProfile);
	}
}