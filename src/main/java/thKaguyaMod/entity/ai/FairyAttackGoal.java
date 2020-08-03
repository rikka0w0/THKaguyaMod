package thKaguyaMod.entity.ai;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import thKaguyaMod.entity.living.IDanmakuMob;
import thKaguyaMod.entity.living.MobUtil;

/**
 * Chase and look at the target, attempt to attack.
 */
public class FairyAttackGoal extends Goal {
	public final CreatureEntity parentEntity;
	
	public FairyAttackGoal(CreatureEntity parentEntity) {
		this.parentEntity = parentEntity;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		return this.parentEntity.getAttackTarget() != null;
	}

	@Override
	public void startExecuting() {

	}

	@Override
	public void resetTask() {

	}

	@Override
	public void tick() {
		IDanmakuMob me = (IDanmakuMob) parentEntity;
		double attackDistance = me.getAttackDistance();
		double flyingHeight = me.getFlyingHeight();
		double chaseSpeed = me.getSpeed();
		
		LivingEntity livingentity = parentEntity.getAttackTarget();
		double distance = livingentity.getDistance(this.parentEntity);
		
		// Regulate the flying height
    	int heightCount = 0;
    	while(parentEntity.world.isAirBlock(new BlockPos(parentEntity.posX, parentEntity.posY - heightCount, parentEntity.posZ)) && heightCount < 8) {
    		heightCount++;
    	}

		if (heightCount > flyingHeight * 1.1 || heightCount < flyingHeight * 0.9) {
			double delta_motionY = 0D;
			if (heightCount > flyingHeight) {
				delta_motionY = (livingentity.posY - parentEntity.posY) * 0.006D;
			} else {
				delta_motionY = 0.008D;
			}
			parentEntity.setMotion(parentEntity.getMotion().add(0, delta_motionY, 0));
		}

		// Chase the victim
		if (distance > attackDistance) {
			Vec3d towards = livingentity.getPositionVec().subtract(parentEntity.getPositionVec()).normalize();
			double rate = (distance / attackDistance);
			this.parentEntity.move(MoverType.SELF, towards.scale(0.3 * chaseSpeed * rate));
		}

		// Look at the victim
		MobUtil.setLookTowards(parentEntity, livingentity);
		this.parentEntity.getLookController().setLookPositionWithEntity(livingentity, 10F, 10F);

		// Attack
		me.shotDamakuTick(distance < attackDistance && this.parentEntity.canEntityBeSeen(livingentity));
	}
}
