package thKaguyaMod;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class THShotLib extends THMathUtil {
	/**
	 * 爆発の音とパーティクルを出す
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param explosionSize
	 */
	public static void explosionEffect(World world, double posX, double posY, double posZ, float explosionSize) {
		world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2.0F, 3.0F);
		
		if (explosionSize >= 2.0F) {
			world.addParticle(ParticleTypes.EXPLOSION_EMITTER, posX, posY, posZ, 1.0D, 0.0D, 0.0D);
		} else {
			world.addParticle(ParticleTypes.EXPLOSION, posX, posY, posZ, 1.0D, 0.0D, 0.0D);
		}
	}
    
	/**
	 * 軽い爆発の音とパーティクルを出す
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param explosionSize
	 */
    public static void explosionEffect2(World world, double posX, double posY, double posZ, float explosionSize) {
    	world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.HOSTILE, 2.0F, 3.0F);

		if (explosionSize >= 2.0F) {
			world.addParticle(ParticleTypes.EXPLOSION_EMITTER, posX, posY, posZ, 1.0D, 0.0D, 0.0D);
		} else {
			world.addParticle(ParticleTypes.EXPLOSION, posX, posY, posZ, 1.0D, 0.0D, 0.0D);
		}
    }
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*		弾幕関連処理系	Danmaku Handle																							*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * 倒れたときに周囲の妖精にダメージを与えて消える。距離があるほどダメージが少なくなる
     * @param deadEntity   : 倒れたEntity
     * @param range        : 周囲にダメージを与える有効距離
     * @param maxDamage    : 最も接近していた時のダメージ
     */
	public static void banishExplosion(Entity deadEntity, double range, float maxDamage) {
		List<Entity> list = deadEntity.world.getEntitiesWithinAABBExcludingEntity(deadEntity,
				deadEntity.getBoundingBox().expand(range, range, range));
		for (int k = 0; k < list.size(); k++) {
			Entity entity = (Entity) list.get(k);

			// TODO: EntityTHFairy
			/*if (entity instanceof EntityTHFairy && !(entity instanceof EntityFamiliar)) {
				EntityTHFairy fairy = (EntityTHFairy) entity;
				double distance = entity.getDistanceToEntity(deadEntity);

				if (distance <= range) {
					float damage = maxDamage * (1.0F - (float) (distance / range));
					if (damage < 2.0F) {
						damage = 2.0F;
					}
					fairy.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, deadEntity), damage);
				}
			}*/
		}
	}
}
