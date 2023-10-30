package com.mondecitronne.homunculus.proxy;

import com.mojang.authlib.GameProfile;
import com.mondecitronne.homunculus.PlayerSkin;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkinHandlerClientProxy extends SkinHandlerProxy {
	public class SkinOwner extends SkinHandlerProxy.SkinOwner {
		private PlayerSkin skin;
		
		SkinOwner(GameProfile playerProfileIn) {
			super(playerProfileIn);
			skin = new PlayerSkin(playerProfileIn);
		}
		
		@Override
		public GameProfile getPlayerProfile() {
			if (skin != null) {
				return skin.getPlayerProfile();
			} else {
				return super.getPlayerProfile();
			}
		}
		
		public PlayerSkin getPlayerSkin() {
			return skin;
		}
		
		@Override
		public void loadSkin() {
			skin.loadSkin();
		}
	}
	
	@Override
	public SkinOwner createSkinOwner(GameProfile playerProfile) {
		return new SkinOwner(playerProfile);
	}
}
