package thKaguyaMod;

import java.util.function.Supplier;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import thKaguyaMod.init.THKaguyaItems;

public class ItemGroupTHKaguya extends ItemGroup {
	/*=================クリエイティブタブ登録==================*/
	/** スペルカードタブ */
	public static final ItemGroup main = new ItemGroupTHKaguya(THKaguyaCore.MODID, () -> new ItemStack(THKaguyaItems.suwako_hat));
	//public static final ItemGroup tabSpellCard = new CreativeTabSpellCard("spellcard");

	private final Supplier<ItemStack> iconProvider;
	private ItemStack cachedIcon = null;

	public ItemGroupTHKaguya(String label, Supplier<ItemStack> iconProvider) {
		super(label);
		this.iconProvider = iconProvider;
	}

	@Override
	public ItemStack createIcon() {
		if (cachedIcon == null) {
			cachedIcon = iconProvider.get();
		}

		return cachedIcon;
	}
}
