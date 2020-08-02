package thKaguyaMod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.SimpleItemTexture;

public class ItemFireRatRobe extends ArmorItem {
	//火鼠の皮衣
	//炎に耐性のある衣 着ても持っても炎に対しては強い
    
    public ItemFireRatRobe(IArmorMaterial armorMaterial) {
    	super(armorMaterial, EquipmentSlotType.CHEST, //鎧の素材を、独自のものにしたいなら、EnumArmorMaterialを独自のものに変更すればいいはず
    			(new Item.Properties())
    			.group(ItemGroupTHKaguya.main)
    			.maxDamage(0) //アイテムの耐久設定。　0なら無限
    			);
    	this.setRegistryName("firerat_robe");
    	SimpleItemTexture.register(this, "thkaguyamod:hinezumi");//テクスチャの指定
    }

	//アーマーのテクスチャを指定
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return "thkaguyamod:textures/armor/hinezumi.png";
	}
	
	/*public void takenFromCrafting(EntityPlayer player, ItemStack itemstack, IInventory iinventory)
    {
    	itemstack.addEnchantment(Enchantment.fireProtection, 5);//fireProtection　レベル５を付加
    	//他のエンチャント効果を付けたいのなら、Enchantment.javaを参照
    	//ちなみにShiftでアイテムを回収した場合にエンチャントがつかないのは、このメソッドのバグ？
    }*/
	
	//左クリックでEntityに当たった場合に呼び出されるメソッド
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    	target.setFire(20); //当たったEntityに着火　数値が大きいほど長くなるのかな？
        return true;
    }
	//右クリック時にとる行動。
	@Override
	public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK; //ガードの構えをする
    }
	
	//右クリックを押している時間の最大時間
	@Override
	public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }
	
	/** 右クリックを押した瞬間の処理
	 *  @param itemStack : 右クリックを押したItemStack
	 *  @param world     : ワールド
	 *  @param player    : 右クリックを押したプレイヤー
	 *  @return 右クリックを押したItemStackを返す
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		player.setActiveHand(hand);
    	player.extinguish();//使用者の炎を消す
        return ActionResult.newResult(ActionResultType.SUCCESS, itemStack);
    }

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!stack.isEnchanted()) {
			tooltip.add(Enchantments.FIRE_PROTECTION.getDisplayName(5));
		}
	}

	//インベントリにある限り常時呼び出されるメソッド
	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
    	if(!itemStack.isEnchanted()) //エンチャントがついていないなら、炎耐性５を付与（救済処置）
    		itemStack.addEnchantment(Enchantments.FIRE_PROTECTION, 5);
	}
    
	//アイテムを発光させるか。 trueなら発光
	@Override
	@OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
		return true;
    }

	/** エンチャントできない。　できるとかなりのチートアイテム化する。*/
	@Override
	public int getItemEnchantability() {
        return 0;
    }

	//Forgeの追加メソッド　エンチャントブックの使用を許可するか
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
	
	//アーマースロットに入っているときに呼び出される（Forge追加）
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 0, false, false));
		player.extinguish();//着用者の炎を消す
    }
	
}