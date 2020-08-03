package thKaguyaMod.entity.living;

import javax.annotation.Nonnull;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class MobUtil {
	public static void setLookTowards(LivingEntity entity, @Nonnull LivingEntity victim) {
		// Look at the victim
		double x = victim.posX - entity.posX;
		double y = (victim.posY + victim.getEyeHeight()) - (entity.posY + entity.getEyeHeight());
		double z = victim.posZ - entity.posZ;
		setLookTowards(entity, x, y, z);
	}

	public static void setLookTowards(LivingEntity entity, @Nonnull Vec3d towards) {
		// Look at the victim
		double x = towards.x;
		double y = towards.y;
		double z = towards.z;
		setLookTowards(entity, x, y, z);
	}

	public static void setLookTowards(LivingEntity entity, double x, double y, double z) {
		float angleXZ = - ((float)Math.atan2(x, z)) / 3.141593F * 180F;
		float angleY  = -(float)Math.atan2( y, Math.sqrt(x * x + z * z)) / 3.141593F * 180F;
		entity.rotationYaw = angleXZ % 360;
		entity.rotationPitch = angleY % 360;
	}
}
