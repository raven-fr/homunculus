package com.mondecitronne.homunculus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWatchLeashHolder extends EntityAIBase {
	protected EntityCreature entity;

	public EntityAIWatchLeashHolder(EntityCreature entityIn) {
		entity = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		return entity.getLeashed();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}

	@Override
	public void updateTask() {
		Entity holder = this.entity.getLeashHolder();
		if (holder == null) {
			return;
		}
		entity.getLookHelper().setLookPosition(holder.posX, holder.posY + (double) holder.getEyeHeight(), holder.posZ,
				(float) entity.getHorizontalFaceSpeed(), (float) entity.getVerticalFaceSpeed());
	}
}
