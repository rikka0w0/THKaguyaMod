package thKaguyaMod.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

import rikka.librikka.model.loader.ISimpleItemDataProvider;
import thKaguyaMod.THKaguyaCore;

@OnlyIn(Dist.CLIENT)
public final class ModelDataProvider extends BlockStateProvider implements ISimpleItemDataProvider {
	public ModelDataProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, THKaguyaCore.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		SimpleItemTexture.forEach((key, resLoc) -> {
			if (key instanceof Item) {
				resLoc = new ResourceLocation(resLoc.getNamespace(), "item/" + resLoc.getPath());
			} else if (key instanceof Block) {
				resLoc = new ResourceLocation(resLoc.getNamespace(), "block/" + resLoc.getPath());
			}
			registerSimpleItem(key.asItem(), resLoc);
		});
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
