package thKaguyaMod.entity.living;

public interface IDanmakuMob {
	//弾幕MOBの種族
	
	//人間に属す種族
	/** 種族：人間 */
	public static final int SPECIES_HUMAN = 0;
	/** 種族：半人 */
	public static final int SPECIES_HUMAN_HALF = 1;
	/** 種族：月人 */
	public static final int SPECIES_HUMAN_LUNARIAN = 2;
	/** 種族：仙人 */
	public static final int SPECIES_HUMAN_HERMIT = 3;
	/** 種族：尸解仙*/
	public static final int SPECIES_HUMAN_SHIKAISEN = 4;
	/** 種族：天人 */
	public static final int SPECIES_HUMAN_CELESTIALS = 5;
	/** 種族：聖人 */
	public static final int SPECIES_HUMAN_SAINT = 6;
	/** 種族：小人 */
	public static final int SPECIES_HUMAN_INCHLINGS = 7;
	/** 種族：蓬莱人 */
	public static final int SPECIES_HUMAN_HOURAI = 8;
	
	//神に属す種族
	/** 種族：神 */
	public static final int SPECIES_GOD = 32;
	/** 種族：現人神 */
	public static final int SPECIES_GOD_ARAHITOGAMI = 33;
	/** 種族：八咫烏 */
	public static final int SPECIES_GOD_YATAGARASU = 34;
	
	//閻魔
	/** 種族：閻魔 */
	public static final int SPECIES_ENMA = 48;
	
	//妖精に属す種族
	/** 種族：妖精 */
	public static final int SPECIES_FAIRY = 64;
	/** 種族：氷精 */
	public static final int SPECIES_FAIRY_ICE = 65;
	
	//幽霊に属す種族
	/** 種族：幽霊 */
	public static final int SPECIES_PHANTOM = 92;
	/** 種族：半霊 */
	public static final int SPECIES_PHANTOM_HALF = 93;
	/** 種族：亡霊 */
	public static final int SPECIES_PHANTOM_GHOST = 94;
	/** 種族：神霊 */
	public static final int SPECIES_PHANTOM_DIVINESPIRIT = 95;
	/** 種族：船幽霊 */
	public static final int SPECIES_PHANTOM_SHIP = 96;
	/** 種族：キョンシー */
	public static final int SPECIES_PHANTOM_JIANGSHI = 97;
	
	//式神に属す種族
	/** 種族：式神 */
	public static final int SPECIES_SHIKIGAMI = 108;
	/** 種族：人形 */
	public static final int SPECIES_SHIKIGAMI_DOLL = 109;
	/** 種族：使い魔 */
	public static final int SPECIES_SHIKIGAMI_FAMILIAR = 110;
	
	//妖怪に属す種族
	/** 種族：妖怪 */
	public static final int SPECIES_YOUKAI = 128;//妖怪
	/** 種族：魔法使い */
	public static final int SPECIES_YOUKAI_MAGICIAN = 129;//魔法使い
	/** 種族：妖獣 */
	public static final int SPECIES_YOUKAI_BEAST = 130;//妖獣
	/** 種族：吸血鬼 */
	public static final int SPECIES_YOUKAI_VAMPIRE = 131;//吸血鬼
	/** 種族：鬼 */
	public static final int SPECIES_YOUKAI_ONI = 132;//鬼
	/** 種族：河童 */
	public static final int SPECIES_YOUKAI_KAPPA = 133;//河童
	/** 種族：烏天狗 */
	public static final int SPECIES_YOUKAI_TENGU_CROW = 134;//烏天狗
	/** 種族：白狼天狗 */
	public static final int SPECIES_YOUKAI_TENGU_WHITEWOLF = 135;//白狼天狗
	/** 種族：死神 */
	public static final int SPECIES_YOUKAI_SHINIGAMI = 136;//死神
	/** 種族：雪女 */
	public static final int SPECIES_YOUKAI_YUKIONNA = 137;//雪女
	/** 種族：妖蟲 */
	public static final int SPECIES_YOUKAI_BUG = 139;//妖蟲
	/** 種族：夜雀 */
	public static final int SPECIES_YOUKAI_YOSUZUME = 140;//夜雀
	/** 種族：妖怪兎 */
	public static final int SPECIES_YOUKAI_RABBIT = 142;//妖怪兎
	/** 種族：付喪神 */
	public static final int SPECIES_YOUKAI_TUKUMOGAMI = 143;//付喪神
	/** 種族：釣瓶落とし */
	public static final int SPECIES_YOUKAI_TURUBEOTOSHI = 144;//釣瓶落とし
	/** 種族：橋姫 */
	public static final int SPECIES_YOUKAI_HASHIHIME = 145;//橋姫
	/** 種族：覚 */
	public static final int SPECIES_YOUKAI_SATORI = 146;//覚
	/** 種族：入道 */
	public static final int SPECIES_YOUKAI_NYUUDOU = 147;//入道
	/** 種族：鵺 */
	public static final int SPECIES_YOUKAI_NUE = 148;//鵺
	/** 種族：人魚 */
	public static final int SPECIES_YOUKAI_MERMAID = 149;//人魚
	/** 種族：天邪鬼 */
	public static final int SPECIES_YOUKAI_AMANOJAKU = 150;//天邪鬼
	
	//龍神
	/** 種族：龍神 */
	public static final int SPECIES_DRAGON = 192;
	
	//その他の種族
	/** 種族：その他種族 */
	public static final int SPECIES_OTHERS = 255;
	
	// Method prototypes
	int getSpecies_1();	// Primary Species
	/**
	 * @return Secondary Species, -1 if not applicable.
	 */
	default int getSpecies_2() {
		return -1;
	};
	
    /**
     * 人間ならtrueを返す
     * @return 人間ならtrue
     */
	default boolean isHuman() {
		return getSpecies_1() < SPECIES_GOD || getSpecies_2() < this.SPECIES_GOD;
	}
    
    /**
     * 幽霊ならtrueを返す
     * @return 幽霊ならtrue
     */
	default boolean isPhantom() {
		return (getSpecies_1() >= SPECIES_PHANTOM && getSpecies_1() < SPECIES_SHIKIGAMI)
				|| (getSpecies_2() >= SPECIES_PHANTOM && getSpecies_2() < SPECIES_SHIKIGAMI);
	}
    
    /**
     * 妖精ならtrueを返す
     * @return 妖精ならtrue
     */
	default boolean isFairy() {
		return getSpecies_1() == SPECIES_FAIRY || getSpecies_2() == SPECIES_FAIRY;
	}
    
    /**
     * 妖怪ならtrueを返す
     * @return 妖怪ならtrue
     */
	default boolean isYoukai() {
		return (getSpecies_1() >= SPECIES_YOUKAI && getSpecies_1() < SPECIES_DRAGON)
				|| (getSpecies_2() >= SPECIES_YOUKAI && getSpecies_2() < SPECIES_DRAGON);
	}
    
    /**
     * ある種族であるか返す
     * @param species 種族
     * @return ある種族であるならtrue
     */
	default boolean isInSpecies(int species) {
		return getSpecies_1() == species || getSpecies_2() == species;
	}
}
