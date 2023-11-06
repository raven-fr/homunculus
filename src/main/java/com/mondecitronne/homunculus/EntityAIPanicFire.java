package com.mondecitronne.homunculus;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;

public class EntityAIPanicFire extends EntityAIPanic {
	public EntityAIPanicFire(EntityCreature creature, double speedIn) {
		super(creature, speedIn);
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && creature.isBurning();
	}
}
