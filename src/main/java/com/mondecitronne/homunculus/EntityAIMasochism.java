package com.mondecitronne.homunculus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMasochism extends EntityAIBase {
	protected EntityHomunculus homunculus;
	protected Entity dominant;

	private final float targetDistance;
	private final double speed;
	private int interestTimer;

	public EntityAIMasochism(EntityHomunculus entityIn, double moveSpeed, float maxTargetDistance) {
		homunculus = entityIn;
		speed = moveSpeed;
		targetDistance = maxTargetDistance;
		interestTimer = 0;
	}

	protected boolean interestElapsed() {
		return homunculus.ticksExisted - interestTimer > 1200;
	}

	protected void resetInterest() {
		interestTimer = homunculus.ticksExisted;
	}

	@Override
	public boolean shouldExecute() {
		if (!homunculus.isMasochist()) {
			return false;
		}
		if (dominant != null && !interestElapsed()) {
			return true;
		}
		dominant = homunculus.getRevengeTarget();
		resetInterest();
		return dominant != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !homunculus.getNavigator().noPath() &&
				dominant.isEntityAlive() &&
				!interestElapsed() &&
				dominant.getDistanceSq(homunculus) < (double) (targetDistance * targetDistance);
	}

	@Override
	public void startExecuting() {
		if (dominant.getDistanceSq(homunculus) > 8.0) {
			homunculus.getNavigator().tryMoveToEntityLiving(dominant, speed);
		}
		updateTask();
	}

	@Override
	public void updateTask() {
		homunculus.getLookHelper().setLookPosition(dominant.posX, dominant.posY + (double) dominant.getEyeHeight(),
				dominant.posZ, (float) homunculus.getHorizontalFaceSpeed(), (float) homunculus.getVerticalFaceSpeed());
	}
}
