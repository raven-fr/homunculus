package com.mondecitronne.homunculus;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.mondecitronne.homunculus.proxy.SkinHandlerProxy;

import io.netty.util.internal.StringUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHomunculus extends EntityLiving {
	@SidedProxy(clientSide = "com.mondecitronne.homunculus.proxy.SkinHandlerClientProxy", serverSide = "com.mondecitronne.homunculus.proxy.SkinHandlerProxy", modId = "homunculus")
	private static SkinHandlerProxy skinProxy;
	
	private SkinHandlerProxy.SkinOwner skinOwner;
	private static final DataParameter<NBTTagCompound> SKIN_OWNER = EntityDataManager.createKey(EntityHomunculus.class, DataSerializers.COMPOUND_TAG);
	
	public EntityHomunculus(World world) {
		super(world);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(SKIN_OWNER, new NBTTagCompound());
	}
	
	private boolean isPlayerProfileUpdated(GameProfile input) {
		if (skinOwner == null) {
			return input != null;
		} else {
			GameProfile skinOwnerProfile = skinOwner.getPlayerProfile();
			return !skinOwnerProfile.getName().toLowerCase().equals(input.getName().toLowerCase()) || (input.getId() != null && !input.getId().equals(skinOwnerProfile.getId()));
		}
	}
	
	@Nullable
	public SkinHandlerProxy.SkinOwner getSkinOwner() {
		GameProfile ownerProfile = NBTUtil.readGameProfileFromNBT(this.getDataManager().get(SKIN_OWNER));
		if (isPlayerProfileUpdated(ownerProfile) && !StringUtil.isNullOrEmpty(ownerProfile.getName())) {
			skinOwner = skinProxy.createSkinOwner(ownerProfile);
		} else if (ownerProfile == null) {
			skinOwner = null;
		}
		return skinOwner;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("SkinOwner")) {
			NBTTagCompound ownerCompound = compound.getCompoundTag("SkinOwner");
			if (ownerCompound.hasKey("Name")) {
				this.getDataManager().set(SKIN_OWNER, ownerCompound);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		SkinHandlerProxy.SkinOwner owner = getSkinOwner();
		if (owner != null) {
			NBTTagCompound profileCompound = new NBTTagCompound();
			NBTUtil.writeGameProfile(profileCompound, owner.getPlayerProfile());
            NBTTagCompound ownerCompound = new NBTTagCompound();
            ownerCompound.setTag("Name", profileCompound.getTag("Name"));
            if (profileCompound.hasKey("Id")) {
            	ownerCompound.setTag("Id", profileCompound.getTag("Id"));
            }
            compound.setTag("SkinOwner", ownerCompound);
        }
		return compound;
	}
}
