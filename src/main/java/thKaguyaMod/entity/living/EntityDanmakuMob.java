package thKaguyaMod.entity.living;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import thKaguyaMod.THKaguyaCore;
//import thKaguyaMod.THKaguyaLib;
import thKaguyaMod.THShotLib;
/*import thKaguyaMod.entity.EntityTHItem;
import thKaguyaMod.entity.effect.EntitySpellCardCircle;
import thKaguyaMod.init.THKaguyaConfig;*/ // TODO: THKaguyaConfig
import thKaguyaMod.init.THKaguyaItems;

//Removed unused: teleportRandomly, teleportToEntity and teleportTo   
/** 弾幕を使うMOBの定義クラス */
public abstract class EntityDanmakuMob extends MobEntity implements IDanmakuMob, ISpellCardUser {// implements IRangedAttackMob
    /** 最後に動いた時間 */
	protected int lastTime;
 
	public int attackCounter = 0;
	public int attackInterval;
	public int courseChangeCooldown = 0;
	public double waypointX;
    public double waypointY;
    public double waypointZ;
	
	public int lostTarget;
	
	/** 指定移動するときの方向 */
	protected Vec3d moveVec;
	
	/** 指定移動する時間 */
	protected int moveTimer;
	
	/** 指定移動する最大時間 */
	protected int moveTimerMax;
	
	/** 停止中かどうか？ */
	protected boolean stop;


	
	/** ターゲットとの間に取る間合い */
	private double attackDistance;
	
	/** 近づくと敵対化する距離 */
	protected double detectionDistance;
	
	/** 飛ぶ地面からの高さ */
	protected int flyingHeight;
	
	/** 飛行モードであるか？
	 * @true  飛行モード
	 * @false 着地モード
	 */
	public boolean isFlyingMode;
	
	
	
	/** 妖精などの出す弾幕のパターンの設定 */
	public int danmakuPattern;
	/** 妖精などの出す弾の発射頻度の設定 */
	public int danmakuSpan;
	/** 妖精などの出す弾の形状設定 */
	public byte shotForm;
	/** 妖精などの出す弾の色設定 */
	public byte shotColor;
	/** 妖精などの出す弾の速度設定 */
	public float speedRate;
	/** 妖精などの出す弾の特殊弾設定 */
	public short special;
	
	/** スペルカードモードか？ 
	 * @true  スペルカードを使用中
	 * @false スペルカードを使用していない
	 */
	public boolean isSpellCardMode;
	
	public final int EASY = 1;
	public final int NORMAL = 2;
	public final int HARD = 3;
	public final int LUNATIC = 4;

	
	////////////////////////
	/// Constructor and NBT
	////////////////////////
	public EntityDanmakuMob(final EntityType<? extends EntityDanmakuMob> entityType, final World world) {
		super(entityType, world);
		isFlyingMode = true;
		this.setFlyingHeight(3);
		this.stop = false;
		
		this.setNoAI(false); // @Override public boolean isAIEnabled() return true;
		
    	this.isSpellCardMode = false;				// スペルカードモード判定
    	this.attackInterval = 0;					// 次の攻撃パターンへの移行時間
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		
		if (compound.contains(THKaguyaCore.MODID)) {
			compound = compound.getCompound(THKaguyaCore.MODID);
			// 弾幕MOBの攻撃パターンを読み込み
			setForm(compound.getByte("Form"));
			setDanmakuPattern(compound.getInt("Danmaku"));
			danmakuSpan = compound.getShort("Span");
			shotForm = compound.getByte("ShotForm");
			shotColor = compound.getByte("ShotColor");
			speedRate = compound.getFloat("Speed");
			setAttackDistance(compound.getByte("AttackRange"));
			setDetectionDistance(compound.getByte("DetectRange"));
			setFlyingHeight((int) compound.getByte("Flying"));
			special = compound.getShort("Special");
			if (getFlyingHeight() <= 0) {
				isFlyingMode = false;
			} else {
				isFlyingMode = true;
			}
			if (shotForm < 0 || shotForm >= 32) {
				shotForm = 0;
			}
			if (shotColor < 0 || shotColor >= 10) {
				shotColor = 8;
			}
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		// 弾幕MOBの攻撃パターンを書き込む
		compound.putByte("Form", getForm());
		compound.putInt("Danmaku", getDanmakuPattern());
		compound.putShort("Span", (short) danmakuSpan);
		compound.putByte("ShotForm", shotForm);
		compound.putByte("ShotColor", shotColor);
		compound.putFloat("Speed", speedRate);
		compound.putByte("AttackRange", (byte) getAttackDistance());
		compound.putByte("DetectRange", (byte) getDetectionDistance());
		compound.putByte("Flying", (byte) getFlyingHeight());
		compound.putShort("Special", (short) special);
	}
	
	///////////////////////////////
	/// Overridden Entity methods
	///////////////////////////////  
    //Entityの発する音の大きさ
    @Override
	protected float getSoundVolume() {
		return 0.3F;
	}
    
	//発する音のピッチ
    @Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 1.95F;
	}

    //生きてるときに出す音
    @Override
	protected SoundEvent getAmbientSound() {
		return this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
	}

    //攻撃を受けたときの音
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

	//倒れたときの音
    @Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_BAT_DEATH;
	}
    
    //一つのチャンクに湧く最大数
    @Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	////////////////////////
	/// Entity Data
	////////////////////////
	private static final DataParameter<Byte> FORM = EntityDataManager.createKey(EntityDanmakuMob.class, DataSerializers.BYTE); // 16
	private static final DataParameter<Integer> DANMAKU_PATTERN = EntityDataManager.createKey(EntityDanmakuMob.class, DataSerializers.VARINT); //17
	private static final DataParameter<Byte> SPELLCARD_MOTION = EntityDataManager.createKey(EntityDanmakuMob.class, DataSerializers.BYTE); // 18
	private static final DataParameter<Byte> FLYING_HEIGHT = EntityDataManager.createKey(EntityDanmakuMob.class, DataSerializers.BYTE); // 19
	
	/**
	 * 最初に一度だけ呼ばれる処理
	 */
	@Override
    protected void registerData() { // entityInit -> registerData
        super.registerData();
        this.dataManager.register(FORM, (byte)0);				// 見た目
        this.dataManager.register(DANMAKU_PATTERN, 0);			// 使用中のスペルカード
        this.dataManager.register(SPELLCARD_MOTION, (byte)0);	// スペルカードモーション
        this.dataManager.register(FLYING_HEIGHT, (byte)0);		// 飛ぶ高さ
    }
	
    /**
     * 見た目を返す
     */
    public byte getForm() {
    	return this.dataManager.get(FORM).byteValue();
    }
    
    /**
     * 見た目を設定する
     */
	public void setForm(byte type) {
		this.dataManager.set(FORM, type);
	}
	
    /**
     * 弾幕のパターンを返す
     * @return 弾幕パターン
     */
	public int getDanmakuPattern() {
		return this.dataManager.get(DANMAKU_PATTERN).intValue();
	}
    
    /**
     * 弾幕パターンを設定
     * @param pattern 弾幕パターン
     */
    public void setDanmakuPattern(int pattern) {
		this.dataManager.set(DANMAKU_PATTERN, pattern);
    }
    
    /**
     * スペルカード宣言モーションを返す
     * @return スペルカード宣言時のモーションカウント
     */
    public byte getSpellCardMotion() {
		return this.dataManager.get(SPELLCARD_MOTION).byteValue();
    }
    
    /**
     * スペルカード宣言モーションを設定
     * @param motion モーションカウント
     */
    public void setSpellCardMotion(byte motion) {
    	this.dataManager.set(SPELLCARD_MOTION, motion);
    }

    /**
     * スペルカード宣言モーションを返す
     * @return スペルカード宣言時のモーションカウント
     */
    public int getFlyingHeight() {
		return this.dataManager.get(FLYING_HEIGHT).byteValue();
    }
    
    /**
     * スペルカード宣言モーションを設定
     * @param motion モーションカウント
     */
    public void setFlyingHeight(int height) {
    	this.dataManager.set(FLYING_HEIGHT, (byte) height);
    }
    
	////////////////////////
	/// Entity Attributes
	////////////////////////
    /**
     * 最大HPを設定
     * @param hp 最大HP
     */
    public void setMaxHP(double hp) {
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(hp);
    }
    
    /**
     * 移動速度を設定
     * @param speed 移動速度
     */
    public void setSpeed(double speed) {
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
    }
    
    /**
     * 移動速度を返す
     * @return 移動速度
     */
    public double getSpeed() {
    	return this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
    }

    //Entityの属性の設定
    @Override
    protected void registerAttributes() {
    	super.registerAttributes();
    	//最大HP
    	this.setMaxHP(2.0D);
    	//動く速さ
    	this.setSpeed(0.25D);
    	//ノックバック耐性。0.0でノックバック無効
    	this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
    	//索敵距離
    	this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
    }

	////////////////////////
	/// Getters & Helpers
	////////////////////////
    /** 座標を直接指定して、その位置ベクトルを返す
     * @param x : X座標
     * @param y : Y座標
     * @param z : Z座標
     * @return
     */
    //public Vec3 pos(double x, double y, double z) return new Vec3d(x,y,z);
    
    /**
     * 目の位置より少し下の位置ベクトルを返す
     * @return the position of the LivingEntity, with y slightly below the eye of the creature.
     */
    public final Vec3d pos() { // TODO: rename to eyePos
    	return THShotLib.pos_Living(this);
    } // Remove not used: Vec3d pos(LivingEntity living)

    /**
     * 向いている方向ベクトルを返す。
     * @return
     */
    public Vec3d look() { // TODO: rename to lookingTowards or lookingAt
    	return THShotLib.angle(this.rotationYaw, this.rotationPitch);
    }

    @Deprecated // TODO: Absolutely makes no sense, why not use getVecFromAngle()
    public static Vec3d angle(float yaw, float pitch) {
    	return THShotLib.angle(yaw, pitch);
    }
    
	////////////////////////
	/// Motion Control
	////////////////////////
	//落下させる
    @Override
	public void fall(float distance, float damageMultiplier) {
		if (getFlyingHeight() > 0) {
			super.fall(distance, damageMultiplier);
		}
    }
    
    //落下しているときの処理
    @Override
    protected void updateFallState(double y, boolean onGround, BlockState state, BlockPos pos) {}

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    @Override
    public void travel(Vec3d towards) {
    	// moveEntityWithHeading(strafe, forward) -> 
    	// travel(new Vec3d((double)this.moveStrafing, (double)this.moveVertical, (double)this.moveForward))
		if (getFlyingHeight() <= 0) {
			super.travel(towards);
			return;
		}
    	this.moveRelative(0.02F, towards);
    	
    	// Copied from FlyingEntity
		if (this.isInWater()) {
			this.moveRelative(0.02F, towards);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale((double) 0.8F));
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

	         this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, towards);
	         this.move(MoverType.SELF, this.getMotion());
	         this.setMotion(this.getMotion().scale((double)f));
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
    
	////////////////////////
	/// Custom Properties
	////////////////////////
    /**
     * ターゲットとの間に取る間合いを設定する
     * @param distance ターゲットとの間に取る間合い
     */
	public void setAttackDistance(double distance) {
		attackDistance = distance;
	}
    
    /**
     * ターゲットとの間に取る間合いを返す
     * @return ターゲットとの間合いの距離
     */
	public double getAttackDistance() {
		return attackDistance;
	}
    
    /**
     * 近づいたら敵対化する距離を設定する
     * @param distance 近づいたら敵対化する距離
     */
	public void setDetectionDistance(double distance) {
		detectionDistance = distance;
	}
    
    /**
     * 近づいたら敵対化する距離を返す
     * @return 近づいたら敵対化する距離
     */
	public double getDetectionDistance() {
		return detectionDistance;
	}
    
    /**
     * 周りの妖精を敵対化させるかどうか
     * @return 敵対化させられるならtrue
     */
	@Override
	public boolean canFairyCall() {
		return false;
	}
    
    /**
     * 妖精を敵対化させられる最大の距離を返す
     * @return 妖精を敵対化させられる最大の距離
     */
	protected double getFairyCallDistance() {
		return 10.0D;
	}
    
    /**
     * スペルカードを宣言するモーションを始める
     */
    protected void setSpellCardAttack()
    {
    	setSpellCardMotion((byte)-30);
    }
    
	public LivingEntity getTarget() {
		// if (THKaguyaConfig.danmakuLevel <= 0) return null;

		LivingEntity entityToAttack = this.getAttackTarget();
		if (entityToAttack instanceof LivingEntity) {
			return (LivingEntity) entityToAttack;
		}

		return null;
	}

	//////////////////////
	/// Callbacks
	//////////////////////
    /**
     * 使用しているスペルカードNoを返す
     * @return スペルカードNo
     */
	public int getUsingSpellCardNo() {
		return -1;
	}
    
    /**
     * スペルカードを使用中かどうかを返す
     * @return スペルカード使用中ならtrue
     */
	public boolean isSpellCardAttack() {
		return getUsingSpellCardNo() >= 0;
	}

    /**
     * 種族１を返す
     * @return 種族ID
     */
    @Override
	public int getSpecies_1() {
		return SPECIES_OTHERS;
	}
    
    /**
     * 種族２を返す
     * @return 種族ID
     */
    @Override
	public int getSpecies_2() {
		return SPECIES_OTHERS;
	}
    
    /**
     * 移動をやめ、見ている方向も一定にする
     */
    protected void setStopStart()
    {
    	stop = true;
    	moveTimer = 0;
    	moveTimerMax = 0;
    }
    
    /**
     * 移動停止を終了させる
     */
    protected void setStopEnd()
    {
    	stop = false;
    }
    
    /**
     * 停止中かどうかを返す
     * @return 停止中ならtrue
     */
    public boolean isStop()
    {
    	return stop;
    }
    

    
    /**
     * ベクトルで指定した方向に指定の速度で、指定の時間の間動く
     * @param move 移動する方向を表すベクトル
     * @param speed 移動速度
     * @param time 移動時間
     */
    protected void move(Vec3d move, double speed, int time) {
    	moveVec = move.scale(speed);
    	moveTimer = time;
    	moveTimerMax = time;
    }
    
    /**
     * 右方向に指定の速度で、指定の時間の間動く
     * @param speed 移動速度
     * @param time 移動時間
     */
    protected void moveRight(double speed, int time) {
    	moveVec = THShotLib.getVecFromAngle(rotationYaw + 90F, rotationPitch);
    	move(moveVec, speed, time);
    }
    
    /**
     * 左方向に指定の速度で、指定の時間の間動く
     * @param speed 移動速度
     * @param time 移動時間
     */
    protected void moveLeft(double speed, int time)
    {
    	moveVec = THShotLib.getVecFromAngle(rotationYaw - 90F, rotationPitch);
    	move(moveVec, speed, time);
    }
    
    /**
     * 前方に指定の速度で、指定の時間の間動く
     * @param speed 移動速度
     * @param time 移動時間
     */
    protected void moveForward(double speed, int time)
    {
    	moveVec = THShotLib.getVecFromAngle(rotationYaw, rotationPitch);
    	move(moveVec, speed, time);
    }
    
    /**
     * 後方に指定の速度で、指定の時間の間動く
     * @param speed 移動速度
     * @param time 移動時間
     */
    protected void moveBack(double speed, int time)
    {
    	moveVec = THShotLib.getVecFromAngle(rotationYaw + 180, rotationPitch);
    	move(moveVec, speed, time);
    }
    
    /**
     * 攻撃を次の段階に移す処理
     * @param nextPattern 次の攻撃パターンID
     * @param interval 次の攻撃を開始するまでの時間
     * @param maxHP 次の攻撃パターン時の最大HP
     * @param invincibleTime 無敵時間
     */
    protected void moveDanmakuAttack(int nextPattern, int interval, float maxHP, int invincibleTime) {
		/*setDanmakuPattern((byte)nextPattern);
		attackCounter = -interval;
		deathTime = 0;
		if(!world.isRemote) {
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHP);
			setHealth(maxHP);
		}
		attackInterval = invincibleTime;
		THShotLib.danmakuRemove(this, 40.0D, "Enemy", true);
		isSpellCardMode = false;
		if(circle != null) {
			circle.setDead();
		}*/
    }
    

    
    public int getSpellCardCircleColor()
    {
    	return CIRCLE_COLOR_RED;
    }
    
    /**
     * スペルカードを宣言する
     * @param spellCardNo
     */
    protected void useSpellCard(int spellCardNo) {
		/* if (entityToAttack instanceof LivingEntity) {
			THKaguyaLib.spellCardDeclaration(this.world, this, (LivingEntity)this.entityToAttack, spellCardNo, 0, THKaguyaConfig.danmakuLevel, !isSpellCardMode);
		}

		if(!isSpellCardMode) {
			isSpellCardMode = true;
	    	//スペルカードの魔法陣を出現させる
	    	circle = new EntitySpellCardCircle(world, this, 16 + getSpellCardCircleColor(), -100);
	    	if(!world.isRemote)
	    	{
	    		world.spawnEntityInWorld(circle);
	    	}
			this.setSpellCardAttack();
		}*/
    }
    
    //毎tick呼ばれる
    @Override
	public void baseTick() {
		if (ticksExisted > lastTime) {
			super.baseTick();
		}
	}
    
	/** 毎tick呼ばれる処理 */
    @Override
    public void tick() {
        //ピースフルなら消滅させる
		if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
			this.remove();
			return;
		}
  
    	//弾幕レベルが０なら、攻撃の対象を持たない
    	//if(THKaguyaConfig.danmakuLevel <= 0) this.setAttackTarget(null);
    	
    	//時が止まっているなら処理を行わない
    	if(ticksExisted <= lastTime) {
    		this.setMotion(Vec3d.ZERO);
    		setPosition(prevPosX, prevPosY, prevPosZ);
    		return;
    	} else {
			//時が止まっていないなら通常の処理を行う
			super.tick();
		}
    	
		if (getHealth() <= 0.0F) {
			return;
		}
    	
		if (attackInterval > 0) {
			attackInterval--;
		}

		if (isSpellCardAttack()) {
			if (attackCounter == -20)
				setSpellCardAttack();
		}
    	
    	//スペルカードモーション中ならモーションを進める
		if (getSpellCardMotion() < 0) {
			if (!world.isRemote) {
				setSpellCardMotion((byte) (getSpellCardMotion() + 2));
			}
		}
        
		if (moveTimer > 0) {
			double moveRate = Math.cos((double) moveTimer / (double) moveTimerMax * Math.PI / 2.0D);
			if (!world.isRemote) {
				this.move(MoverType.SELF, moveVec.scale(moveRate));
			}
			moveTimer--;
		}

        if(getFlyingHeight() > 0)
        {
	    	//高さを調整する
	    	int heightCount = 0;
	    	//地面から何ｍ上空にいるか調べる
	    	while(world.isAirBlock(new BlockPos(posX, posY - heightCount, posZ)) && heightCount < 8) {
	    		heightCount++;
	    	}
	    	
	    	double delta_motionY = 0D;
	    	//ターゲットがいて地面から離れる高さ以上にいるなら
	    	if(this.getAttackTarget() != null && heightCount > getFlyingHeight()) {
	    		//ターゲットの高さと今の高さを比較
	    		double distance = this.getAttackTarget().posY - this.posY;

	    		delta_motionY = distance * 0.0006D;
			} else if (heightCount < getFlyingHeight()) { // ターゲットがいないななら
				if (heightCount >= getFlyingHeight()) {
					delta_motionY = -0.0006D;
				} else if (heightCount < getFlyingHeight()) {
					delta_motionY = 0.006D;
				}
			}

	    	this.setMotion(this.getMotion().add(0, delta_motionY, 0));

			if (!stop) {
				this.move(MoverType.SELF, THShotLib.gravity(this.getMotion().y));
			}
        }

        LivingEntity targetVictim = this.getAttackTarget();
    	//ターゲットがいるが、死んでいるなら
		if (targetVictim != null && targetVictim.removed) {
			targetVictim = null;
			this.setAttackTarget(targetVictim); // ターゲットから外す
		}

    	double entityToAttackableLength = this.getAttackDistance();//攻撃する射程

    	//ターゲットがいないなら
		if (targetVictim == null) {
			AxisAlignedBB aabb = (new AxisAlignedBB(this.getPosition())).grow(entityToAttackableLength, entityToAttackableLength, entityToAttackableLength);
			for (PlayerEntity potentialVictim: this.world.getEntitiesWithinAABB(PlayerEntity.class, aabb)) {
				//敵対範囲内にいるプレイヤーを探す
				if (this.canEntityBeSeen(potentialVictim)) {	// Rikka's Fix: Only targets directly visible player
					targetVictim = potentialVictim;
					this.setAttackTarget(targetVictim);
					break;
				}
			}

            //プレイヤーがいるなら
            if (targetVictim != null) {
            	//妖精を敵対化させられるなら、周りの妖精を敵対化させる
            	if(canFairyCall())
            	{
            		//範囲内のEntityをリストで取得
	            	List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, 
	            			this.getBoundingBox().grow(getFairyCallDistance()));//指定範囲内のEntityをリストに登録
					for (int i = 0; i < list.size(); i++) {
						Entity entity1 = (Entity) list.get(i);
						// 弾幕MOBなら
						// TODO: Needs EntityTHFairy
						/*if (entity1 instanceof EntityTHFairy) {
							MobEntity danmakuMob = (MobEntity) entity1;
							// 妖精のターゲットがいないなら
							if (targetVictim == null) {
								// 妖精のターゲットをこの弾幕MOBと同じターゲットにする
								danmakuMob.setAttackTarget(targetVictim);
							}
						}*/
					}
            	}
                //this.aggroCooldown = 20;
            }
        }
    	
    	//射程圏内にターゲットがいるなら
    	if(targetVictim != null && !removed /*&& entityToAttack.getDistanceSqToEntity(this) < entityToAttackableLength * entityToAttackableLength*/)
    	{

    		//ターゲットの方を向かせる
    		double xDistance = targetVictim.posX - posX;
    		double yDistance = (targetVictim.posY + targetVictim.getEyeHeight()) - (posY + this.getEyeHeight());
    		double zDistance = targetVictim.posZ - posZ;
    		float angleXZ = - ((float)Math.atan2(xDistance, zDistance)) / 3.141593F * 180F;
			float angleY  = -(float)Math.atan2( yDistance, Math.sqrt(xDistance * xDistance + zDistance * zDistance)) / 3.141593F * 180F;
    			
			if (!stop) {
				setRotation(angleXZ, angleY);
				// TODO: Check head rotation fix
				this.setRotationYawHead(angleXZ);
			}
        	
    		//ターゲットを目視できるなら
			if (canEntityBeSeen(targetVictim) && this.getDistance(targetVictim) < entityToAttackableLength) {
				// ターゲットの見失いカウントを０にする
				lostTarget = 0;
    			//弾幕難易度が０でなければ弾幕攻撃をする
				int level = 5;//THKaguyaConfig.danmakuLevel;
				if (level != 0 && attackCounter >= 0) {
					danmakuPattern(level);
				}

				// 攻撃カウントを増やす
				attackCounter++;
			} else {
    			//ターゲットを目視できないなら
    			//move(THShotLib.getVecFromAngle(rand.nextFloat() + 360F, rand.nextFloat() * 360F), getSpeed() * 10, 30);
    			//ターゲットの見失いカウントを増やす
    			lostTarget++;
    			//ターゲットを１０秒目視できないなら
				if (lostTarget > 200) {
					// ターゲットをなしにする
					targetVictim = null;
					this.setAttackTarget(targetVictim);
				}
    		}
    		
			if (moveTimer <= 0 && lostTarget > 0) {
				// moveRight(getSpeed(), 10);
				move(THShotLib.getVecFromAngle(rand.nextFloat() + 360F, rand.nextFloat() * 360F), getSpeed(), 30);
			}
        	
			if (moveTimer <= 0 && targetVictim != null && entityToAttackableLength > 0) {
				double toTargetDistance = this.getDistance(targetVictim);
				double rate;

				if (toTargetDistance < entityToAttackableLength) {
					rate = 1.0D - (toTargetDistance / entityToAttackableLength);
					moveBack(getSpeed() * 0.3D * rate, 15);
				} else {
					rate = (toTargetDistance / entityToAttackableLength);
					moveForward(getSpeed() * 0.5D * rate, 15);
				}
			}
    	}
    	
    	
    	//最後に動いた時間として、その時間を保存
		if (ticksExisted > lastTime) {
			lastTime = ticksExisted;
		}
    }
    
    /**
     * 無敵状態を取得する
     * 攻撃移行時の時間の間は無敵
     * @return [ true : 無敵状態 ] [ false : 無敵ではない]
     */
	@Override
	public boolean isInvulnerable() {
		if (attackInterval > 0) {
			return true;
		} else {
			return super.isInvulnerable();
		}
	}
    
    //ダメージを受けたときの処理
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (super.attackEntityFrom(damageSource, applyPotionDamageCalculations(damageSource, damage))
				&& damageSource.getTrueSource() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) damageSource.getTrueSource();
			// ノックバック耐性が高い
			this.setMotion(this.getMotion().scale(0.0001D));
			if (this.isRidingOrBeingRiddenBy(entity)) {
				if (entity instanceof PlayerEntity)// entity != this)
					this.setAttackTarget(entity);

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
    }
    
    // TODO: Unused shit
    /*
	//衝突処理
	public void hitCheck(float damage) {
	    //始点（現在地）
    	Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
    	//終点（現在地に移動量を足した点）
    	Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
        //始点と終点からブロックとの当たりを取得
    	//MovingObjectPosition movingObjectPosition = worldObj.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);
    	MovingObjectPosition movingObjectPosition = world.func_147447_a(vec3d, vec3d1, false, true, true);
    	vec3d = Vec3.createVectorHelper(posX, posY, posZ);
    	vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
    	//何らかのブロックに当たっているなら
        if (movingObjectPosition != null)
        {
        	THShotLib.playShotSound(this);
        	//終点を当たった点に変更
        	vec3d1 = Vec3.createVectorHelper(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
        }
        
        movingObjectPosition = hitEntityCheck(movingObjectPosition, vec3d, vec3d1);
        
        if (movingObjectPosition != null)
        {
        	//当たった場合の処理をする
            onImpact(movingObjectPosition, damage);
        }

	}
	
	//Entityとの当たり判定をとる
	public MovingObjectPosition hitEntityCheck(MovingObjectPosition movingObjectPosition, Vec3 vec3d, Vec3 vec3d1)
	{
        Entity entity = null;//実際に当たったことにするEntity
    	double d = 0.0D;//そのEntityまでの仮の距離
		float hitSize = 0.9F;//getSize() * 0.5F;
    	//ここから移動量分の線分を作り、それに弾の大きさの２倍の肉付けをし直方体を作る。それに当たったEntityをリスト化する\\
        List list = world.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(hitSize, hitSize, hitSize));//指定範囲内のEntityをリストに登録
		
    	for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);//entity1にリストの先端のentityを保存
        	//entity1が、当たり判定を取らない　または　entity1が使用者　または　飛んで25カウント以下？　または　EntityTHShotならパス
            if ( entity1.canBeCollidedWith())
        	{
            	{
	        		//判定を弾の大きさに変更
	            	AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(hitSize, hitSize, hitSize);
	            	MovingObjectPosition movingObjectPosition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
	        		//この判定で当たっているなら
	            	if (movingObjectPosition1 != null)
	            	{
	        			//当たっているならここからその点までの距離を取得
	            		double d1 = vec3d.distanceTo(movingObjectPosition1.hitVec);
	        			//今までの一番近くにいるなら、一番近いEntityを更新する
	            		if (d1 < d || d == 0.0D)
	            		{
	                		entity = entity1;
	                		d = d1;
	            		}
	        		}
            	}
        	}
        }

    	//当たったEntityがいるなら、当たったEntityをMovingObjectPositionで登録
        if (entity != null)
        {
        	
            movingObjectPosition = new MovingObjectPosition(entity);
        }
		
        return movingObjectPosition;
	}
	
	//ブロックやEntityに当たった時の処理
    protected void onImpact(MovingObjectPosition movingObjectPosition, float damage)
    {
    	//当たった時の処理
    	if (!world.isRemote)
    	{
    		Entity hitEntity = movingObjectPosition.entityHit;
        
    		//当たったEntityがいるなら
    		if ( hitEntity != null )
        	{
        		{
        			float damageRate = 1.0F;
        			{
        				if(!hitEntity.attackEntityFrom(DamageSource.causeMobDamage(this), damage));
        			}
        		}
			}
    	}
    }*/
    
    //EntityCreatureでの邪魔な処理を無効化
    // TODO: check this, updateEntityActionState is now final in MobEntity
    /*@Override
	protected void updateEntityActionState() {
		if (entityToAttack == null) {
			super.updateEntityActionState();
		}
	}*/
    
    /*@Override
    protected void updateLeashedState()
    {
    	
    }
    
    @Override
    public void setPathToEntity(PathEntity par1PathEntity)
    {
    }*/
    
    /*@Override
    public void onLivingUpdate()
    {
    	if(ticksExisted > lastTime)
    	{
    		super.onLivingUpdate();
    	}
    }*/
    
    //AIが有効ならtrueを返す
    /*@Override
    protected boolean isAIEnabled()
    {
    	return ticksExisted > lastTime;
    }*/
    
    /*@Override
    protected void updateEntityActionState()
    {
    	if(ticksExisted > lastTime)
    	{
    		super.updateEntityActionState();
    	}
    }*/
    
    /*@Override
    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
    	if(ticksExisted > lastTime)
    	{
    		super.faceEntity(par1Entity, par2, par3);
    	}
    }*/
    
    /**
     * Sets the position of the entity, keeps yaw/pitch,  and updates the 'last' variables
     */
    /*public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        if(ticksExisted > lastTime)
        {
        	super.setPositionAndUpdate(par1, par3, par5);
        }
    }*/

    //弾幕パターンの記述
	protected void danmakuPattern(int level) {
		// Callback
	}
    
    //死んでいるときに呼ばれる
    @Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		// 周囲の妖精を巻き込む
		if (this.deathTime == 7) {
			THShotLib.explosionEffect2(world, posX, posY, posZ, 1.0F + deathTime * 0.1F);
			THShotLib.banishExplosion(this, 5.0F, 5.0F);
		}
	}
    
    //倒れたときに落とすアイテム
	@Override
	protected void dropLoot(DamageSource ds, boolean hasBeenAttackedByPlayer) {
		// dropFewItems(boolean hasBeenAttackedByPlayer, int lootingLevel) ->
		// dropLoot(DamageSource ds, boolean hasBeenAttackedByPlayer)
		//スペルカードを使用していたなら、そのスペルカードを落とす
		if (getUsingSpellCardNo() >= 0 && hasBeenAttackedByPlayer) {
			this.entityDropItem(Items.DIAMOND);
			// TODO need THKaguyaItems.spell_card
			// this.entityDropItem(new ItemStack(THKaguyaItems.spell_card, 1, getUsingSpellCardNo()), 0.0F);
		}
    }
	
	private void dropItem(int type, Vec3d pos, Vec3d angle) {
		// TODO need EntityTHItem
		//EntityTHItem item = new EntityTHItem(world, pos.xCoord, pos.yCoord, pos.zCoord, angle, (byte) type);
		ItemStack droppedItem = new ItemStack(Items.PORKCHOP, 2);
		Vec3d eyePos = this.pos();
		Entity itemEntity = new ItemEntity(this.world, eyePos.x, eyePos.y, eyePos.z, droppedItem);
		
		if (!world.isRemote) {
			world.addEntity(itemEntity);
		}
	}
	
	/**
	 * パワーアップアイテム 小を落とす
	 * @param pos   : 落とす場所
	 * @param angle : 落とす方向 
	 */
	public void dropPowerUpItem(Vec3d pos, Vec3d angle) {
		this.dropItem(1, pos, angle);
	}
	
	/**
	 * パワーアップアイテム 小を落とす
	 * @param num 落とす数
	 */
	public void dropPowerUpItem(int num) {
		for (int i = 1; i <= num; i++) {
			dropPowerUpItem(this.pos(), THShotLib.angle_LimitRandom(new Vec3d(0.0D, 1.0D, 0.0D), 90F));
		}
	}
	
	/**
	 * 得点アイテムを落とす
	 * @param pos   : 落とす場所
	 * @param angle : 落とす方向 
	 */
	public void dropPointItem(Vec3d pos, Vec3d angle) {
		this.dropItem(4, pos, angle);
	}
	
	/**
	 * 得点アイテムを落とす
	 * @param num 落とす数
	 */
	public void dropPointItem(int num) {
		for (int i = 1; i <= num; i++) {
			dropPointItem(this.pos(), THShotLib.angle_LimitRandom(new Vec3d(0.0D, 1.0D, 0.0D), 90F));
		}
	}
	
	/**
	 * スペルカードアイテムを落とす
	 * @param pos   : 落とす場所
	 * @param angle : 落とす方向 
	 */
	public final void dropSpellCardItem(Vec3d pos, Vec3d angle) {
		this.dropItem(10, pos, angle);
	}
	
	/**
	 * スペルカードアイテムを落とす
	 * @param num 落とす数
	 */
	public final void dropSpellCardItem(int num) {
		for (int i = 1; i <= num; i++) {
			dropSpellCardItem(this.pos(), THShotLib.angle_LimitRandom(new Vec3d(0.0D, 1.0D, 0.0D), 90F));
		}
	}
	
	/**
	 * エクステンドアイテムを落とす
	 * @param pos   : 落とす場所
	 * @param angle : 落とす方向 
	 */
	public final void dropExtendItem(Vec3d pos, Vec3d angle) {
		this.dropItem(11, pos, angle);
	}
	
	/**
	 * エクステンドアイテムを落とす
	 * @param num 落とす数
	 */
	public final void dropExtendItem(int num) {
		for (int i = 1; i <= num; i++) {
			dropExtendItem(this.pos(), THShotLib.angle_LimitRandom(new Vec3d(0.0D, 1.0D, 0.0D), 90F));
		}
	}
	
	/**
	 * 弾アイテムを落とす。弾幕形状の設定もできる
	 * @param shotID : 弾ID。ItemTHShot.BUTTERFLY という感じで指定
	 * @param stackNumber : スタック数
	 * @param number : 弾幕の弾数
	 * @param speed : 形状ごとの速度に、形状速度 * speed * 0.03　を加算する
	 * @param color : THShotLib.REDなどで指定。全８色 + RANDOM + RAINBOW
	 * @param gravity : 重力加速度。gravity * 0.003の速度を毎tick下方向に加速させる
	 * @param special : 特殊な弾。THShotLib.BOUND01などで指定
	 * @param form : 弾幕系状。0で一点、1で前方ランダム、2で扇状、3で全方位、4で球状、5でリング
	 * @return 作成されたEntityItem
	 */
	public final ItemEntity dropShotItem(int shotID, int stackNumber, int number, int speed, int color, int gravity, int special, int form) {
        //ItemStack shot = new ItemStack(THKaguyaItems.shot_item, stackNumber, shotID);
        ItemStack shot = new ItemStack(Items.BAT_SPAWN_EGG, stackNumber);
		
	    CompoundNBT nbt = shot.getOrCreateTag();
	    nbt.putShort("Number", (short)number);
		nbt.putByte("Speed", (byte)(speed % 64));
		nbt.putByte("Color", (byte)(color % 16));
		nbt.putByte("Gravity", (byte)(gravity % 16));
		nbt.putInt("Special", special);
	    nbt.putByte("DanmakuForm", (byte)(form % 8));

	    return this.entityDropItem(shot, 0.0F);
	}
}
