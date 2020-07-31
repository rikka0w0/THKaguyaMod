package thKaguyaMod.item;

import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import thKaguyaMod.ItemGroupTHKaguya;
import thKaguyaMod.client.ModelDataProvider;

//蓬莱の玉　　なにもできないアイテム
public enum HouraiPearl implements IStringSerializable {
	RED, BLUE, GREEN, YELLOW, PURPLE, AQUA, ORANGE, WHITE;

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public String registryName() {
		return "hourai_pearl_" + getName();
	}

	public Item newItem() {
		Item item = new Item((new Item.Properties())
				.group(ItemGroupTHKaguya.main)
				.maxDamage(0));

		item.setRegistryName(registryName());
		ModelDataProvider.setTextureName(item, "material/" + registryName());

		return item;
	}
}
