package thKaguyaMod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.ModelDataProvider;
import thKaguyaMod.client.model.ModelSuwakoHat;

public class ItemKerobou extends ArmorItem {
	//諏訪子の帽子
	//物理的なダメージを受けたとき、確定で相手にもダメージを与える
    public ItemKerobou(IArmorMaterial material) {
    	super(material, EquipmentSlotType.HEAD, (new Item.Properties())
    			.group(ItemGroupTHKaguya.main)
    			.maxDamage(620) //アイテムの耐久設定
    			.setNoRepair());
    	this.setRegistryName("suwako_hat");
    	ModelDataProvider.setTextureName(this, "thkaguyamod:kerobou");//テクスチャの指定
    }

	//アーマーのテクスチャを指定
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return "thkaguyamod:textures/armor/suwako_hat.png";
	}

	//エンチャントの可否
	@Override
	public int getItemEnchantability() {
        return 0;//エンチャント不可
    }

	//Forgeの追加メソッド　エンチャントブックの使用を許可するか
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
	   return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!stack.isEnchanted()) {
			tooltip.add(Enchantments.THORNS.getDisplayName(10));
		}
	}
	
	//Forgeの追加メソッド　アーマースロットに入っているときに呼び出される
	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!itemStack.isEnchanted()) {
			itemStack.addEnchantment(Enchantments.THORNS, 10);
		}
	}

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    /**
     * Override this method to have an item handle its own armor rendering.
     * 
     * @param  entityLiving  The entity wearing the armor 
     * @param  itemStack  The itemStack to render the model of 
     * @param  armorSlot  0=head, 1=torso, 2=legs, 3=feet
     * 
     * @return  A ModelBiped to render instead of the default
     */
    @SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		if (armorSlot == EquipmentSlotType.HEAD) {
			return (A) new ModelSuwakoHat<>(0.4F, 64, 32);
		} else {
			return null;
		}
	}
}
