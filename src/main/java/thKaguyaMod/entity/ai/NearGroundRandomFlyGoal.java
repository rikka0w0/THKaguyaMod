package thKaguyaMod.entity.ai;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import thKaguyaMod.entity.living.MobUtil;

public class NearGroundRandomFlyGoal extends Goal {
	private final CreatureEntity parentEntity;
	private final Supplier<Double> heightSupplier;
	private final double range = 0.5;

	public NearGroundRandomFlyGoal(CreatureEntity entity, Supplier<Double> heightSupplier) {
		this.parentEntity = entity;
		this.heightSupplier = heightSupplier;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		MovementController movementcontroller = this.parentEntity.getMoveHelper();
		if (!movementcontroller.isUpdating()) {
			return true;
		} else {
			double d0 = movementcontroller.getX() - this.parentEntity.posX;
			double d1 = movementcontroller.getY() - this.parentEntity.posY;
			double d2 = movementcontroller.getZ() - this.parentEntity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		Random random = this.parentEntity.getRNG();
		double targetX = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 8.0F);
		double targetZ = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 8.0F);

		// Find the ground level at target
		BlockPos ground = new BlockPos(targetX, this.parentEntity.posY, targetZ);
		if (this.parentEntity.world.isAirBlock(ground.up())) {
			while(this.parentEntity.world.isAirBlock(ground)) {
				ground = ground.down();
			}
		} else {
			// We are under water
			while(!this.parentEntity.world.isAirBlock(ground)) {
				ground = ground.up();
			}
		}

		double heightSetPoint = heightSupplier.get();
		double targetY = ground.getY() + heightSetPoint + (double) ((random.nextFloat() * 2.0F - 1.0F) * range);

		this.parentEntity.getMoveHelper().setMoveTo(targetX, targetY, targetZ, 1.0D);
		MobUtil.setLookTowards(parentEntity, targetX - parentEntity.posX, targetY - parentEntity.posY, targetZ - parentEntity.posZ);
	}

	@Override
	public void tick() {
		//MobUtil.setLookTowards(parentEntity, parentEntity.getMotion());
	}
}