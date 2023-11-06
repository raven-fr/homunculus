package com.mondecitronne.homunculus;

import com.mojang.authlib.GameProfile;
import com.mondecitronne.homunculus.skin.DefaultSkin;
import com.mondecitronne.homunculus.skin.FallbackSkin;
import com.mondecitronne.homunculus.skin.HTTPSkin;
import com.mondecitronne.homunculus.skin.PlayerSkin;
import com.mondecitronne.homunculus.skin.Skin;
import io.netty.util.internal.StringUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityHomunculus extends EntityCreature {
	private static final DataParameter<NBTTagCompound> SKIN_SOURCE = EntityDataManager.createKey(EntityHomunculus.class,
			DataSerializers.COMPOUND_TAG);

	private final Skin defaultSkin;
	private static final Skin FALLBACK_SKIN = new FallbackSkin();
	private Skin skin;

	public EntityHomunculus(World world) {
		super(world);
		getDataManager().register(SKIN_SOURCE, new NBTTagCompound());
		defaultSkin = new DefaultSkin(getUniqueID());
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	private boolean isPlayerProfileUpdated(GameProfile a, GameProfile b) {
		if (b == null || b.getName() == null) {
			return a != null;
		} else {
			return !a.getName().toLowerCase().equals(b.getName().toLowerCase())
					|| (b.getId() != null && a.getId() != null && !b.getId().equals(a.getId()));
		}
	}

	private void dispatchFetchSkin() {
		if (this.getEntityWorld().isRemote && skin != null) {
			skin.dispatchFetch();
		}
	}

	private void updateSkin() {
		NBTTagCompound sourceNBT = getDataManager().get(SKIN_SOURCE);
		if (sourceNBT.hasKey("Type")) {
			switch (sourceNBT.getString("Type")) {
			case "player":
				GameProfile sourceProfile = NBTUtil.readGameProfileFromNBT(sourceNBT);
				if (sourceProfile == null || StringUtil.isNullOrEmpty(sourceProfile.getName())) {
					skin = null;
					break;
				}
				if (!(skin instanceof PlayerSkin)
						|| isPlayerProfileUpdated(((PlayerSkin) skin).getPlayerProfile(), sourceProfile)) {
					skin = new PlayerSkin(sourceProfile, getEntityWorld().isRemote);
					dispatchFetchSkin();
				}
				break;
			case "http":
				String url = sourceNBT.getString("URL");
				String modelType = sourceNBT.getString("Model");
				if (!StringUtil.isNullOrEmpty(url)) {
					if (modelType == null || !modelType.equals("default") && !modelType.equals("slim")) {
						modelType = "default";
					}
					skin = new HTTPSkin(url, modelType);
					dispatchFetchSkin();
				} else {
					skin = null;
				}
				break;
			default:
				skin = null;
				break;
			}
		} else {
			skin = null;
		}
	}

	public Skin getSkin() {
		updateSkin();
		if (skin != null) {
			if (skin.isLoaded()) {
				return skin;
			} else {
				return FALLBACK_SKIN;
			}
		} else {
			return defaultSkin;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("SkinSource")) {
			NBTTagCompound sourceCompound = compound.getCompoundTag("SkinSource");
			this.getDataManager().set(SKIN_SOURCE, sourceCompound);
		} else if (compound.hasKey("SkinOwner")) {
			// backwards compatibility with old NBT
			NBTTagCompound ownerCompound = compound.getCompoundTag("SkinOwner").copy();
			if (ownerCompound.hasKey("Name")) {
				ownerCompound.setString("Type", "player");
				this.getDataManager().set(SKIN_SOURCE, ownerCompound);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		updateSkin();
		if (skin instanceof PlayerSkin) {
			NBTTagCompound profileCompound = new NBTTagCompound();
			NBTUtil.writeGameProfile(profileCompound, ((PlayerSkin) skin).getPlayerProfile());
			NBTTagCompound sourceCompound = new NBTTagCompound();
			sourceCompound.setTag("Name", profileCompound.getTag("Name"));
			if (profileCompound.hasKey("Id")) {
				// save ID so that if a player changes their name, the skin is invalidated
				// rather than pulling the skin of whoever takes the name
				sourceCompound.setTag("Id", profileCompound.getTag("Id"));
			}
			sourceCompound.setString("Type", "player");
			compound.setTag("SkinSource", sourceCompound);
		} else if (skin instanceof HTTPSkin) {
			NBTTagCompound sourceCompound = new NBTTagCompound();
			sourceCompound.setString("URL", ((HTTPSkin) skin).getUrl());
			sourceCompound.setString("Model", skin.getModelType());
			sourceCompound.setString("Type", "http");
			compound.setTag("SkinSource", sourceCompound);
		} else if (compound.hasKey("SkinSource")) {
			compound.removeTag("SkinSource");
		}
		return compound;
	}
}
