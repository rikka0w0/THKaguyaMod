package thKaguyaMod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.ModelDataProvider;

public class ItemKappaCap extends ArmorItem {
	//河童の帽子
	//着用すると水中での移動が速くなる
	//水中にいないとどんどん壊れる
    public ItemKappaCap(IArmorMaterial material) {
    	super(material, EquipmentSlotType.HEAD, (new Item.Properties())
    			.group(ItemGroupTHKaguya.main)
    			.maxDamage(500)
    			.setNoRepair());
    	this.setRegistryName("kappa_cap");
    	ModelDataProvider.setTextureName(this, "thkaguyamod:kappa_cap");
    }

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return "thkaguyamod:textures/armor/hinezumi.png";
	}

	//エンチャントの可否
	@Override
	public int getItemEnchantability() {
        return 0;//エンチャント不可
    }
	
	//アイテムを大きく表示する
	//public boolean isFull3D() {return true;}
		
	//Forgeの追加メソッド　エンチャントブックの使用を許可するか
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
	   return false;
	}

	@Override
	public void onArmorTick(ItemStack itemStack, World world, PlayerEntity player) {
    	if(player.isInWater())//水中にいるなら
    	{
    		Vec3d motion = player.getMotion();
    		double motionX = motion.x;
    		double motionY = motion.y;
    		double motionZ = motion.z;
    		
    		motionX *= 1.2D; //速度を1.2倍する
    		motionY *= motionY > 0.0D ? 1.2D : 1;
    		motionZ *= 1.2D;

    		double maxSpeed = 0.5D;
			if (motionX > maxSpeed) {
				motionX = maxSpeed;
			} else if (motionX < -maxSpeed) {
				motionX = -maxSpeed;
			}
			if (motionY > maxSpeed) {
				motionY = maxSpeed;
			}
			if (motionZ > maxSpeed) {
				motionZ = maxSpeed;
			} else if (motionZ < -maxSpeed) {
				motionZ = -maxSpeed;
			}

			player.setMotion(motionX, motionY, motionZ);

			if (itemStack.isDamaged()) {
				itemStack.damageItem(-3, player, (entity) -> {});// 耐久を3増やす
			}
		} else { // 地上にいるなら
			if (player.isWet()) { // 濡れているなら
				itemStack.damageItem(-1, player, (entity) -> {});// 耐久を1増やす
			} else {

				if (itemStack.getDamage() < 498) {
					itemStack.damageItem(1, player, (entity) -> {});// 耐久を1減らす
				}
			}
		}
	}
}
