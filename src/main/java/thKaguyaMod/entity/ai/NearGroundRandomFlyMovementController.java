package thKaguyaMod.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class NearGroundRandomFlyMovementController extends MovementController {
	private int courseChangeCooldown;

	public NearGroundRandomFlyMovementController(CreatureEntity entity) {
		super(entity);
	}

	@Override
	public void tick() {
		if (this.action == MovementController.Action.MOVE_TO) {
			if (this.courseChangeCooldown-- <= 0) {
				this.courseChangeCooldown += this.mob.getRNG().nextInt(5) + 2;
				Vec3d vec3d = new Vec3d(this.posX - this.mob.posX, this.posY - this.mob.posY,
						this.posZ - this.mob.posZ);
				double d0 = vec3d.length();
				vec3d = vec3d.normalize();
				if (this.func_220673_a(vec3d, MathHelper.ceil(d0))) {
					double speed = this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
					this.mob.setMotion(this.mob.getMotion().add(vec3d.scale(speed)));
				} else {
					this.action = MovementController.Action.WAIT;
				}
			}

		}
	}

	private boolean func_220673_a(Vec3d p_220673_1_, int p_220673_2_) {
		AxisAlignedBB axisalignedbb = this.mob.getBoundingBox();

		for (int i = 1; i < p_220673_2_; ++i) {
			axisalignedbb = axisalignedbb.offset(p_220673_1_);
			if (!this.mob.world.isCollisionBoxesEmpty(this.mob, axisalignedbb)) {
				return false;
			}
		}

		return true;
	}
}
