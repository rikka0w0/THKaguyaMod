package thKaguyaMod.entity.living;

//import static thKaguyaMod.DanmakuConstants.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
/*import thKaguyaMod.LaserData;
import thKaguyaMod.ShotData;*/
import thKaguyaMod.THShotLib;
import thKaguyaMod.entity.ai.FairyAttackGoal;
import thKaguyaMod.entity.ai.NearGroundRandomFlyGoal;
import thKaguyaMod.entity.ai.NearGroundRandomFlyMovementController;
/*import thKaguyaMod.entity.spellcard.EntitySpellCard;
import thKaguyaMod.init.THKaguyaConfig;*/
import thKaguyaMod.init.THKaguyaItems;
//import thKaguyaMod.item.ItemTHShot;

/** チルノ */
public class EntityCirno extends CreatureEntity implements IDanmakuMob {
	public static final EntityType<EntityCirno> entityType;
	
	static {
		entityType = (EntityType<EntityCirno>) EntityType.Builder
				.create(EntityCirno::new, EntityClassification.CREATURE)
				.size(1.0F, 1.8F)	// MOBの当たり判定の大きさ 横奥行き、高さ、大きさ
				.func_225435_d()	// Natural spawning
				.setShouldReceiveVelocityUpdates(true)
				.build("cirno");
		entityType.setRegistryName("cirno");
	}
	
	private int random;
	
	/** チルノのコンストラクタ（Worldのみは必須） */
	public EntityCirno(final EntityType<? extends CreatureEntity> entityType, final World world) {
		super(entityType, world);		
		this.moveController = new NearGroundRandomFlyMovementController(this);

        this.experienceValue = 250;					// 経験値の量        											
    	//this.setDanmakuPattern(NORMAL_ATTACK01);	// 最初の弾幕パターン
        this.setHealth(38.0F);						// 最初のHP

    	this.random = 0;							// ランダム値
    }

	@Override
	public double getSpeed() {
		return 0.5F; // 移動速度
	}
	
	@Override
	public double getAttackDistance() {
		return 14; // 攻撃時取る間合い
	}

	@Override
	public int getFlyingHeight() {
		return 2; // 飛行高度
	}

    @Override
	public int getSpecies_1() {
		return SPECIES_FAIRY; // 種族の設定
	}

    @Override
	public int getSpecies_2() {
		return SPECIES_FAIRY_ICE;
	}
   
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		//this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.1D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.025F);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(34.0D); // 索敵範囲
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		this.goalSelector.addGoal(4, new FairyAttackGoal(this));
		this.goalSelector.addGoal(5, new NearGroundRandomFlyGoal(this, () -> Double.valueOf(this.getFlyingHeight())));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));

		// this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		// targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (player) -> true));
	}


    @Override
	public void fall(float distance, float damageMultiplier) {
		if (getFlyingHeight() > 0) {
			super.fall(distance, damageMultiplier);
		}
    }

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	
	@Override
	public void travel(Vec3d towards) {
		if (getFlyingHeight() <= 0) {
			super.travel(towards);
		} else {
			travelFlying(towards);
		}
	}

	protected final void travelFlying(Vec3d p_213352_1_) {
		if (this.isInWater()) {
			this.moveRelative(0.02F, p_213352_1_);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale((double) 0.8F));
		} else if (this.isInLava()) {
			this.moveRelative(0.02F, p_213352_1_);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.5D));
		} else {
			BlockPos ground = new BlockPos(this.posX, this.getBoundingBox().minY - 1.0D, this.posZ);
			float f = 0.91F;
			if (this.onGround) {
				f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
			}

			float f1 = 0.16277137F / (f * f * f);
			f = 0.91F;
			if (this.onGround) {
				f = this.world.getBlockState(ground).getSlipperiness(world, ground, this) * 0.91F;
			}

			this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, p_213352_1_);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale((double) f));
		}

		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d1 = this.posX - this.prevPosX;
		double d0 = this.posZ - this.prevPosZ;
		float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
		if (f2 > 1.0F) {
			f2 = 1.0F;
		}

		this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	@Override
	public void shotDamakuTick(boolean isTargetVisible) {
		if (isTargetVisible) {

		} else {
			
		}
	}

    //死んでいるときに呼ばれる
	/*@Override
	protected void onDeathUpdate() {
		if (ticksExisted <= lastTime) {
			return;
		}

		switch (getDanmakuPattern()) {
		case NORMAL_ATTACK01:
			moveDanmakuAttack(SPELLCARD_ATTACK01, 40, 46.0F, 60);
			break;
		case SPELLCARD_ATTACK01:
			moveDanmakuAttack(NORMAL_ATTACK02, 60, 32.0F, 20);
			break;
		case NORMAL_ATTACK02:
			moveDanmakuAttack(SPELLCARD_ATTACK02, 40, 58.0F, 60);
			break;
		case SPELLCARD_ATTACK02:
			moveDanmakuAttack(ATTACK_END, 30, 0F, 60);
			break;
		default:
			if (deathTime % 9 == 0) {
				THShotLib.explosionEffect(world, posX, posY, posZ, 1.0F + deathTime * 0.1F);
			}
			super.onDeathUpdate();

		}
	}*/
    /**
     * 使用しているスペルカードNoを返す
     * @return 使用しているスペルカードNo
     */
	/*@Override
    public int getUsingSpellCardNo() {
    	switch(getDanmakuPattern()) {
    	case SPELLCARD_ATTACK01:
    		return EntitySpellCard.SC_CIRNO_IcicleFall;
    	case SPELLCARD_ATTACK02:
    		return EntitySpellCard.SC_CIRNO_PerfectFreeze;
    	default:
    		return -1;
    	}
    }*/

    /** 弾幕のパターンを記述
     * @param level : EASY～LUNATICの難易度
     */

    /*@Override
    public void danmakuPattern(int level) {
    	Vec3d angle = THShotLib.getVecFromAngle(rotationYaw, rotationPitch);
    	if(attackCounter >= 40) {
    		attackCounter = 0;
        	attackEntityWithRangedAttack(this.getAttackTarget(), 1);
    	}
		/*switch (getDanmakuPattern()) {
		case NORMAL_ATTACK01:
			danmaku01(angle, level);
			break;
		case SPELLCARD_ATTACK01:
			spellcard01(angle, level);
			break;
		case NORMAL_ATTACK02:
			danmaku02(angle, level);
			break;
		case SPELLCARD_ATTACK02:
			spellcard02(angle, level);
			break;
		default:
			break;
		}
    }*/
    	/* TOOD danmaku system is pretty much fucked up
    //通常１
    private void danmaku01(Vec3 angle, int level)
    {
		if(attackCounter == 40)
		{
			angle = this.getLookVec();
			for(int i = 0; i < 7; i++)
			{
				THShotLib.createRingShot(this, this, pos(), angle, 0F, 9999, 0.6D + level * 0.15D, 0.6D + level * 0.15D, 0.1D, gravity_Zero(), ShotData.shot(FORM_CRYSTAL, AQUA, 0.2F, 4.0F, i, 120, 0), i + 2, 0.0D, i * (level), rand.nextFloat() * 360F);
			}
			world.playSoundAtEntity(this, "random.bow", 2.0F, 0.8F);//音を出す
		}
		if(attackCounter >= 50)
		{
			attackCounter = 0;
		}
    }
    
    //スペルカード１　アイシクルフォール
    private void spellcard01(Vec3 angle, int level)
    {
		if(attackCounter == 1)
		{
			useSpellCard(getUsingSpellCardNo());
		}
		else if(attackCounter > 30 && attackCounter < 170 && attackCounter % 17 == 0 && level >= 2)
		{
			
			float size = THShotLib.SIZE[THShotLib.SMALL[0] / 8];
			if(level >= 3)
			{
				size = THShotLib.SIZE[THShotLib.MEDIUM[0] / 8];
			}
			THShotLib.createWideShot(this, this,pos(), angle, 0.3D + level * 0.1D, 0.5D, 1.0D, gravity_Zero(), ShotData.shot(FORM_SMALL, YELLOW, size, 4.0F, 3, 80), 3, 70F, 0.2D);
			world.playSoundAtEntity(this, "random.bow", 2.0F, 0.8F);//音を出す
			
		}
		else if(attackCounter >= 240)
		{
			attackCounter = 0;
		}
    }
    
    //通常２
    private void danmaku02(Vec3 angle, int level)
    {
		if(attackCounter < 50 && attackCounter % 6 == 0)
		{
			THShotLib.createCircleShot(this, pos(), angle, 0.2D, ShotData.shot(FORM_TINY, WHITE), 5 + level * 2);
			THShotLib.createCircleShot(this, pos(), angle, 0.3D + level * 0.1D, ShotData.shot(FORM_CIRCLE, BLUE), 5 + level * 2);
			world.playSoundAtEntity(this, "random.bow", 2.0F, 0.8F);//音を出す
		}
		else if(attackCounter == 55 || attackCounter == 65 || attackCounter == 75)
		{
			//thShotLib.createWideLaserA01(this, this.posX, thShotLib.getPosYFromEye(this, -0.2D), this.posZ, angleXZ, angleY, 0.5D + (double)level * 0.1D, 6.0D, thShotLib.WHITE, 0.1F, 3.0D, 3, 30F);
			THShotLib.createWideLaserA(this, this.pos(), getLookVec(), 0.3D + level * 0.2D, LaserData.laser(WHITE, 0.1F, 4.0F, 4.0F), 3, 30F);
		}
		if(attackCounter > 128)
		{
			attackCounter = 0;
		}
    }
    
    //スペルカード２　パーフェクトフリーズ
    private void spellcard02(Vec3 angle, int level)
    {
		if(attackCounter == 1)
		{
			random = rand.nextInt(2);
			useSpellCard(getUsingSpellCardNo());
		}
		else if(attackCounter >= 70 && attackCounter <= 90 && attackCounter % 3 == 0)
		{
			int way = 4 + random;
			for(int i = 0; i < 3 + level; i++)
			{
				THShotLib.createWideShot(this, this, pos(), angle, 0.3D + level * 0.1D, 0.5D, 1.0D, gravity_Zero(), ShotData.shot(FORM_CIRCLE, BLUE, 0.3F, 4.0F, i, 80), way, 70F, 0.2D);
			}
			world.playSoundAtEntity(this, "random.bow", 2.0F, 0.8F);//音を出す
		}
		else if(attackCounter >= 170)
		{
			attackCounter = 0;
		}
    }*/
    
    //周りの妖精を呼び出すことができるか
    @Override
    public boolean canFairyCall() {
    	return false;//チルノは周りの妖精の助けを得ない
    }
    
    //ダメージを受けたときの処理
    /*@Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {

        if (super.attackEntityFrom(damageSource, applyPotionDamageCalculations(damageSource, damage)) && damageSource.getEntity() instanceof EntityLivingBase)
        {
        	//ノックバック耐性が高い
        	motionX *= 0.01D;
        	motionY *= 0.01D;
        	motionZ *= 0.01D;
        	
            EntityLivingBase entity = (EntityLivingBase)damageSource.getEntity();

            if (this.riddenByEntity != entity && this.ridingEntity != entity)
            {
                if (entity instanceof EntityPlayer)//entity != this)
                {
                    this.entityToAttack = entity;
                }

                return true;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }*/
    
    /**
     * Reduces damage, depending on potions
     */
	@Override
	protected float applyPotionDamageCalculations(DamageSource damageSource, float damage) {
		damage = super.applyPotionDamageCalculations(damageSource, damage);

		if (isInvulnerable()) {
			damage = (float) ((double) damage * 0.05D);
		}

		return damage;
	}

    @Override
    protected void updateAITasks()
    {
        super.updateAITasks();

        /*if (this.getIsTHFairyHanging())
        {
            if (!this.worldObj.isBlockNormalCube(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ)))
            {
                this.setIsTHFairyHanging(false);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }
            else
            {
                if (this.rand.nextInt(200) == 0)
                {
                    this.rotationYawHead = (float)this.rand.nextInt(360);
                }

                if (this.worldObj.getClosestPlayerToEntity(this, 0.5D) != null)
                {
                    this.setIsTHFairyHanging(false);
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
                }
            }
        }
        else
        {
            if (this.currentFlightTarget != null && (!this.worldObj.isAirBlock(this.currentFlightTarget.posX, this.currentFlightTarget.posY, this.currentFlightTarget.posZ) || this.currentFlightTarget.posY < 1))
            {
                this.currentFlightTarget = null;
            }

            if (this.currentFlightTarget == null || this.rand.nextInt(30) == 0 || this.currentFlightTarget.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F)
            {
                this.currentFlightTarget = new ChunkCoordinates((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double var1 = (double)this.currentFlightTarget.posX + 0.5D - this.posX;
            double var3 = (double)this.currentFlightTarget.posY + 0.1D - this.posY;
            double var5 = (double)this.currentFlightTarget.posZ + 0.5D - this.posZ;
            this.motionX += (Math.signum(var1) * 0.5D - this.motionX) * 0.10000000149011612D;
            this.motionY += (Math.signum(var3) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
            this.motionZ += (Math.signum(var5) * 0.5D - this.motionZ) * 0.10000000149011612D;
            float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
            float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += var8;

            if (this.rand.nextInt(100) == 0 && this.worldObj.isBlockNormalCube(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ)))
            {
                this.setIsTHFairyHanging(true);
            }
        }*/
    }
	
	//倒れたときに落とすアイテム
	/*@Override
    protected Item getDropItem()
    {
        return THKaguyaItems.icicle_sword;
    }*/
	
	//倒れたときに落とすアイテム
	@Override
	protected void dropLoot(DamageSource ds, boolean hasBeenAttackedByPlayer) {
		super.dropLoot(ds, hasBeenAttackedByPlayer);
		// TODO dropLoot
		/*if( hasBeenAttackedByPlayer && this.isSpellCardAttack() )
		{
	        int j = 40;//this.rand.nextInt(15) + this.rand.nextInt(1 + par2);
	        int k;
	        Vec3d vec3;
	        float yaw, pitch;
	
	        for (k = 0; k < j; k+=2)
	        {
	        	yaw = 360F / (float)j * (float)k;
	        	pitch = MathHelper.sin( yaw / 180F * 3.141593F * 4F) * 20F - 60F;
	        	vec3 = THShotLib.getVecFromAngle(yaw, pitch, 1.0F);
	        	this.dropPointItem(this.pos(), vec3);
	        	yaw = 360F / (float)j * (float)(k + 1);
	        	pitch = MathHelper.cos( yaw / 180F * 3.141593F * 4F) * 20F - 60F;
	        	vec3 = THShotLib.getVecFromAngle(yaw, pitch, 1.0F);
	        	this.dropPowerUpItem(this.pos(), vec3);
	        }
	        
	        // TODO this.dropShotItem(ItemTHShot.CRYSTAL, 17 + rand.nextInt(2) + lootingLevel, 5, 32, AQUA, 0, 0, 2);
		}
		if (hasBeenAttackedByPlayer && getDanmakuPattern() == SPELLCARD_ATTACK02) {
			// TODO this.dropItem(THKaguyaItems.icicle_sword, 1);
			this.dropExtendItem(this.pos(), this.angle(this.rotationYaw, -90F));
		}*/
    }

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
	@Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }
    
	@Override
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

	/**
	 * 自然スポーンするときに呼ばれる
	 * @return trueならスポーン成功
	 */
	// TODO: EntityCirno#canSpawn()
	/*@Override
    public boolean canSpawn(IWorld world, SpawnReason spawnReason) {
    	if(rand.nextInt(100) < THKaguyaConfig.fairySpawnRate)
    	{
    		return false;
    	}
    	//return true;
    	
        int yPosition = MathHelper.floor_double(this.posY + 0.1D);
    	int xPosition = MathHelper.floor_double(this.posX);
        int zPosition = MathHelper.floor_double(this.posZ);
        //int pointBlock, pointBlock2;
        Block pointBlock, pointBlock2;
        //pointBlock = pointBlock2 = worldObj.getBlockId(xPosition, yPosition, zPosition);
        pointBlock = pointBlock2 = world.getBlock(xPosition, yPosition, zPosition);
        //if(pointBlock != Block.snow.blockID)
        if(pointBlock != Blocks.snow_layer)
        {
        	return false;
        }
        for(int i = -2; i <= 2; i++)
        {
        	for(int j = -2; j<= 2; j++)
        	{
            	//pointBlock2 = worldObj.getBlockId(xPosition + i, yPosition - 1, zPosition + j);
            	pointBlock2 = world.getBlock(xPosition + i, yPosition - 1, zPosition + j);
            	//if(pointBlock2 == Block.ice.blockID)
            	if(pointBlock2 == Blocks.ice)
            	{
            		return true;
            	}
        	}
        }
    	return false;
    }*/

	public int getSpellCardMotion() {
		return -30;
	}
}
