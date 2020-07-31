package thKaguyaMod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;

//FMLにロードさせるためのアノテーション

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import thKaguyaMod.client.ClientProxy;
/*
import thKaguyaMod.entity.living.VillagerRinnosuke;
import thKaguyaMod.event.THKaguyaPlayerDeathEventHandler;
*/
import thKaguyaMod.init.THKaguyaBlocks;
//import thKaguyaMod.init.THKaguyaConfig;
import thKaguyaMod.init.THKaguyaItems;
//import thKaguyaMod.tileentity.TileEntityDivineSpirit;

//Entityに関するレジストリ
//前初期化, 初期化のイベント
//プロキシシステムのためのアノテーション
//言語に関するレジストリ

/** 五つの難題MOD+のコアクラス */
@Mod(THKaguyaCore.MODID)
public class THKaguyaCore {
	public static final String MODID = "thkaguyamod";
	// クライアント側とサーバー側で異なるインスタンスを生成
	public static final CommonProxy proxy = DistExecutor.runForDist(()->()->new ClientProxy(), ()->()->new CommonProxy());

	public static THKaguyaCore instance = null;

	public THKaguyaCore() {
		if (THKaguyaCore.instance == null)
			THKaguyaCore.instance = this;
		else
			throw new IllegalStateException("Mod thkaguyamod has already been initialized!");

	}
	
	/*==================ブロックレンダーID====================*/
	public static int blockRenderId;
	
	/*========================= GUI===========================*/
	/** 弾幕作業台GUI */
	public static final int guiDanmakuCraftingID			=  0;
	/** レーザー弾幕作業台GUI */
	public static final int guiDanmakuCraftingLaserID	=  1;
	//public static int guiSpellCardID = 2;
	/** 守矢神社出張早苗さんGUI */
	public static final int guiMerchantSanaeID			=  2;
	/** 打ち出の小槌GUI */
	public static final int guiMiracleMalletID			=  3;
	
	/*====================防具インデックス====================*/
	public static int hinezumiIndex; 
	public static int marisaIndex;
	public static int suwakoIndex;
	
	/** 村人ID 霖之助 */
	//public static int villagerRinnosukeId = 932;
	//public static VillagerRinnosuke rinnosuke;
	
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public final static class ModEventBusHandler {   	
    	// @SubscribeEvent
    	// public static void newRegistry(RegistryEvent.NewRegistry event) {}
    	
    	@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    		//ブロックの初期設定
    		THKaguyaBlocks.setBlocks();
    		//ブロックの登録
    		proxy.registerBlocks();
    	}
    	
    	@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
    		//アイテムの初期設定
    		THKaguyaItems.setItems();
    		//アイテムを登録
    		proxy.registerItems(event.getRegistry());
    	}
    	
    	@SubscribeEvent
    	public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    		//TileEntityの登録
    		/*
    		TileEntityDivineSpirit.teType = 
    				TileEntityType.Builder.create(TileEntityDivineSpirit::new, validBlocks)
    				.build(null);
    		TileEntityDivineSpirit.teType.setRegistryName(MODID + "_divine_spirit");
    		event.getRegistry().register(TileEntityDivineSpirit.teType);*/
    	}
    	
//    	@SubscribeEvent
//    	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
//    		BlockRegistry.registerContainers(event.getRegistry());
//    	}
    	
    	@SubscribeEvent
    	public static void onCommonSetup(FMLCommonSetupEvent event) {

    	}
    }
	/** 前処理 */
	/*@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		THKaguyaConfig.setConfig(event);
		
		blockRenderId = proxy.getNewRenderType();
		
		proxy.registerSpellCards();


		

		
		//GUIの登録
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		//エンティティの登録
		proxy.registerEntitys();
		
		//妖精などの弾幕パターンを登録
		proxy.registerDanmakuPattern();
		
	}*/
	
	/** 初期化処理 */
	/*@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{	
		rinnosuke = new VillagerRinnosuke();
		//VillagerRegistry.instance().registerVillagerId(villagerRinnosukeId);
		VillagerRegistry.instance().registerVillageTradeHandler(villagerRinnosukeId, rinnosuke);
		
		//スポーン設定
		proxy.registerEntitySpawn();
		
		//ディスペンサーの発射アイテムの登録
		proxy.registerDispenser();
		
		proxy.registerChestItem();
		
		// サーバー側は何もしない, クライアント側ではレンダーの登録が行われる
		proxy.registerRenderers();
		
		
		//レシピの登録
		THKaguyaRecipe.setAllRecipes();
		if(THKaguyaConfig.recipelessItemsRecipe)
		{
			THKaguyaRecipe.setRecipelessItemsRecipe();
		}
		
		MinecraftForge.EVENT_BUS.register(new THKaguyaPlayerDeathEventHandler());
		//MinecraftForge.EVENT_BUS.register(new THKaguyaTimeStopEventHandler());
		
	}*/
}