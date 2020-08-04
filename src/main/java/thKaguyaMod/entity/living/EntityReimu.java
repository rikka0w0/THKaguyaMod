package thKaguyaMod.entity.living;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class EntityReimu extends CreatureEntity implements IDanmakuMob {
	public static final EntityType<EntityReimu> entityType;
	
	static {
		entityType = (EntityType<EntityReimu>) EntityType.Builder
				.create(EntityReimu::new, EntityClassification.CREATURE)
				.size(1.0F, 1.8F)	// MOBの当たり判定の大きさ 横奥行き、高さ、大きさ
				.func_225435_d()	// Natural spawning
				.setShouldReceiveVelocityUpdates(true)
				.build("reimu");
		entityType.setRegistryName("reimu");
	}
	
	public EntityReimu(final EntityType<? extends CreatureEntity> entityType, final World world) {
		super(entityType, world);

        this.experienceValue = 250;					// 経験値の量        											
    	//this.setDanmakuPattern(NORMAL_ATTACK01);	// 最初の弾幕パターン
        this.setHealth(74.0F);						// 最初のHP

    }
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		//this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.1D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.2F);
		//this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0.0D); // 索敵範囲
	}

	@Override
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
		return worldIn.getBlockState(pos.down()).getBlock() == Blocks.GRASS_BLOCK ? 10.0F
				: worldIn.getBrightness(pos) - 0.5F;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromItems(Items.EMERALD), false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));

		// this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
//		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		// targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (player) -> true));
	}
	
	@Override
	public double getSpeed() {
		return 0.2F;
	}

	@Override
	public int getSpecies_1() {
		return SPECIES_HUMAN;
	}
	
	@Override
	public int getDanmakuPattern() {
		return SPELLCARD_ATTACK01;
	}

	@Override
	public boolean canFairyCall() {
		return false;
	}

	@Override
	public int getFlyingHeight() {
		return 0;
	}

	@Override
	public double getAttackDistance() {
		return 0;
	}

	@Override
	public void shotDamakuTick(boolean isTargetVisible) {
		
	}
}
