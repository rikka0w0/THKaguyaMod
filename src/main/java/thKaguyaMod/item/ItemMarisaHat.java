package thKaguyaMod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.ModelDataProvider;
import thKaguyaMod.client.model.ModelMarisaHat;

public class ItemMarisaHat extends ArmorItem {
	/*
	 * 魔理沙の帽子
	 * かぶると通常よりアイテムとの当たり判定が大きくなる
	 */
    public ItemMarisaHat(IArmorMaterial material) {
    	super(material, EquipmentSlotType.HEAD, (new Item.Properties())
    			.group(ItemGroupTHKaguya.main)
    			.maxDamage(40)
    			.setNoRepair());
    	this.setRegistryName("marisa_hat");
    	ModelDataProvider.setTextureName(this, "thkaguyamod:marisa_hat");//テクスチャの指定
    }

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return "thkaguyamod:textures/armor/marisa_hat.png";
	}

	@Override
	public int getItemEnchantability() {
        return 0;
    }

	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
	   return false;
	}

	@Override
	public void onArmorTick(ItemStack itemStack, World world, PlayerEntity player) {
		List<?> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().expand(5.0D, 5.0D, 5.0D));
		for(int k = 0; k < list.size(); k++) {
			Entity entity = (Entity)list.get(k);
			if(entity instanceof ItemEntity) { //アイテムがあるなら
				ItemEntity item = (ItemEntity)entity;
				item.onCollideWithPlayer(player);
			}
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
			return (A) new ModelMarisaHat<>(0.4F, 64, 32);
		} else {
			return null;
		}
	}
}
