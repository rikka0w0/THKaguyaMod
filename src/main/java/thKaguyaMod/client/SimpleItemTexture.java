package thKaguyaMod.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class SimpleItemTexture {
	public static final Map<IItemProvider, ResourceLocation> simpleTextureItems = new HashMap<>();
	
	public static void forEach(BiConsumer<IItemProvider, ResourceLocation> consumer) {
		simpleTextureItems.entrySet().forEach((entry) -> consumer.accept(entry.getKey(), entry.getValue()));
	}

	public static void register(IItemProvider itemProvider) {
		register(itemProvider, itemProvider.asItem().getRegistryName());
	}

	public static void register(IItemProvider itemProvider, String texture) {
		ResourceLocation resLoc = texture.contains(":") ? new ResourceLocation(texture) :
			new ResourceLocation(itemProvider.asItem().getRegistryName().getNamespace(), texture);
		register(itemProvider, resLoc);
	}

	public static void register(IItemProvider itemProvider, ResourceLocation resLoc) {
		simpleTextureItems.put(itemProvider, resLoc);
	}
}
