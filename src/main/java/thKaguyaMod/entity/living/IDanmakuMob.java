package thKaguyaMod.entity.living;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;

public interface IDanmakuMob extends IDanmakuMobDefinition {
	///////////////////////////
	/// Danmaku State
	///////////////////////////
	//攻撃の段階
	/** 攻撃の意思がない状態を表す */
	public static final int NOT_ATTACK 	  = 0;
	/** 通常攻撃１ */
	public static final int NORMAL_ATTACK01 = 1;
	/** 通常攻撃２ */
	public static final int NORMAL_ATTACK02 = 2;
	/** 通常攻撃３ */
	public static final int NORMAL_ATTACK03 = 3;
	public static final int NORMAL_ATTACK04 = 4;
	public static final int NORMAL_ATTACK05 = 5;
	public static final int NORMAL_ATTACK06 = 6;
	public static final int NORMAL_ATTACK07 = 7;
	public static final int NORMAL_ATTACK08 = 8;
	public static final int NORMAL_ATTACK09 = 9;
	public static final int NORMAL_ATTACK10 =10;
	public static final int NORMAL_ATTACK11 =11;
	public static final int NORMAL_ATTACK12 =12;
	public static final int NORMAL_ATTACK13 =13;
	public static final int NORMAL_ATTACK14 =14;
	public static final int NORMAL_ATTACK15 =15;
	public static final int NORMAL_ATTACK16 =16;
	public static final int NORMAL_ATTACK17 =17;
	public static final int NORMAL_ATTACK18 =18;
	public static final int NORMAL_ATTACK19 =19;
	public static final int NORMAL_ATTACK20 =20;
	/** スペルカード１ */
	public static final int SPELLCARD_ATTACK01 = 101;
	/** スペルカード２ */
	public static final int SPELLCARD_ATTACK02 = 102;
	/** スペルカード３ */
	public static final int SPELLCARD_ATTACK03 = 103;
	public static final int SPELLCARD_ATTACK04 = 104;
	public static final int SPELLCARD_ATTACK05 = 105;
	public static final int SPELLCARD_ATTACK06 = 106;
	public static final int SPELLCARD_ATTACK07 = 107;
	public static final int SPELLCARD_ATTACK08 = 108;
	public static final int SPELLCARD_ATTACK09 = 109;
	public static final int SPELLCARD_ATTACK10 = 110;
	public static final int SPELLCARD_ATTACK11 = 111;
	public static final int SPELLCARD_ATTACK12 = 112;
	public static final int SPELLCARD_ATTACK13 = 113;
	public static final int SPELLCARD_ATTACK14 = 114;
	public static final int SPELLCARD_ATTACK15 = 115;
	public static final int SPELLCARD_ATTACK16 = 116;
	public static final int SPELLCARD_ATTACK17 = 117;
	public static final int SPELLCARD_ATTACK18 = 118;
	public static final int SPELLCARD_ATTACK19 = 119;
	public static final int SPELLCARD_ATTACK20 = 120;
	public static final int SPELLCARD_ATTACK21 = 121;
	public static final int SPELLCARD_ATTACK22 = 122;
	public static final int SPELLCARD_ATTACK23 = 123;
	public static final int SPELLCARD_ATTACK24 = 124;
	public static final int SPELLCARD_ATTACK25 = 125;
	public static final int SPELLCARD_ATTACK26 = 126;
	/** 攻撃の終了を表す(倒れた) */
	public static final int ATTACK_END = 127;
	
	///////////////////////////
	/// Method prototypes
	///////////////////////////

	//周りの妖精を呼び出すことができるか
	boolean canFairyCall();

	int getFlyingHeight();

	double getAttackDistance();

	default double getDetectionDistance() {
		LivingEntity entity = (LivingEntity) this;
		double detectionDistance = entity.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getValue();
		return detectionDistance;
	}
	
	default double getSpeed() {
		return 0.25D;
	}

	void shotDamakuTick(boolean isTargetVisible);

	default int getDanmakuPattern() {
		return NORMAL_ATTACK01;	// TODO: Fix this
	}
	
	default int getUsingSpellCardNo() {
		return 0;
	}
}
