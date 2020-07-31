package thKaguyaMod.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags.Blocks;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.ModelDataProvider;

/** 仏の御石の鉢
    石の複合ツール 剣、ツルハシ、スコップ、斧の役割を持つ
	ただし、剣のガード、スコップの雪回収等の細かな機能はない
	石だから、金やダイヤの採取も不可
 */
public class ItemBuddhaStoneBowl extends ToolItem {
	private static IItemTier itemTier = ItemTier.STONE;
	
	/** 仏の御石の鉢 */
	public ItemBuddhaStoneBowl() {
		/** ブロックの壊し安さを設定。　大きいほど壊れやすい。*/
		super(4.0F, 4.0F, itemTier, null, //ダメージ、素材、？
				(new Item.Properties())
    			.group(ItemGroupTHKaguya.main)
				.maxDamage(itemTier.getMaxUses()));
		this.setRegistryName("buddha_stone_bowl");
		ModelDataProvider.setTextureName(this, "thkaguyamod:hotoke_hachi");
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return this.efficiency; // Ignore effectiveBlocks in the constructor
	}

	/** アイテムを発光させる。
	 * 
	 *  @return trueなら発光する
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
   	public boolean hasEffect(ItemStack itemStack) {
		return true;
    }

	/** Entityに当たったときの処理 */
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return true;
	}

	/** ブロックを破壊したときの処理 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		return true;
	}
	
	/** 採取可能なブロックかどうかを返す */
	@Override
    public boolean canHarvestBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		int hl = this.getTier().getHarvestLevel(); // Harvest level
        return Blocks.OBSIDIAN.contains(block) 
        		? hl == 3 
        		: (!Blocks.STORAGE_BLOCKS_DIAMOND.contains(block) && !Blocks.ORES_DIAMOND.contains(block) 
        				? (!Blocks.STORAGE_BLOCKS_EMERALD.contains(block) && !Blocks.ORES_EMERALD.contains(block)
        				? (!Blocks.STORAGE_BLOCKS_GOLD.contains(block) && !Blocks.ORES_GOLD.contains(block)
        				? (!Blocks.STORAGE_BLOCKS_IRON.contains(block) && !Blocks.ORES_IRON.contains(block)
        				? (!Blocks.STORAGE_BLOCKS_LAPIS.contains(block) && !Blocks.ORES_LAPIS.contains(block)
        				? (!Blocks.STORAGE_BLOCKS_REDSTONE.contains(block) && !Blocks.ORES_REDSTONE.contains(block)
        				? (blockState.getMaterial() == Material.ROCK 
        				? true 
        						: (blockState.getMaterial() == Material.IRON 
        						? true : blockState.getMaterial() == Material.ANVIL)) 
        						: hl >= 2)	// STORAGE_BLOCKS_LAPIS
        						: hl >= 1)	// STORAGE_BLOCKS_LAPIS
        						: hl >= 1)	// STORAGE_BLOCKS_IRON
        						: hl >= 2)	// STORAGE_BLOCKS_GOLD
        						: hl >= 2)	// STORAGE_BLOCKS_EMERALD
        						: hl >= 2); // STORAGE_BLOCKS_DIAMOND
    }
	
	/** エンチャントできない。　できるとかなりのチートアイテム化する。*/
	@Override
	public int getItemEnchantability() {
        return 0;
    }
	
	/** Forgeの追加メソッド　エンチャントブックの使用を許可するか */
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
}