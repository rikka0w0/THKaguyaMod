package thKaguyaMod.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import rikka.librikka.model.loader.ISimpleItemDataProvider;
import thKaguyaMod.THKaguyaCore;

public final class ModelDataProvider extends BlockStateProvider implements ISimpleItemDataProvider {
	public static final Map<IItemProvider, ResourceLocation> simpleTextureItems = new HashMap<>();
	
	public ModelDataProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, THKaguyaCore.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleTextureItems.entrySet().forEach((entry) -> {
			Item item = entry.getKey().asItem();
			ResourceLocation resLoc = entry.getValue();
			if (entry.getKey() instanceof Item) {
				resLoc = new ResourceLocation(resLoc.getNamespace(), "item/" + resLoc.getPath());
			} else if (entry.getKey() instanceof Block) {
				resLoc = new ResourceLocation(resLoc.getNamespace(), "block/" + resLoc.getPath());
			}
			registerSimpleItem(item, resLoc);
		});
	}

	public static void setTextureName(IItemProvider itemProvider, String texture) {
		simpleTextureItems.put(itemProvider, new ResourceLocation(texture));
	}

    //////////////////////
    /// 1.15 compatibility layer
    //////////////////////
    protected ModelDataProvider models() {
    	return this;
    }
   
	@Override
	public BlockModelBuilder getBuilderImpl(String path) {
		return this.getBuilder(path);
	}

	@Override
	public ExistingFileHelper existingFileHelper() {
		return this.existingFileHelper;
	}

	@Override
	public String getModId() {
		return THKaguyaCore.MODID;
	}
}
