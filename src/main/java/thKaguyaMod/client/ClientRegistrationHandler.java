package thKaguyaMod.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import thKaguyaMod.THKaguyaCore;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = THKaguyaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationHandler {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper exfh = event.getExistingFileHelper();

		if (event.includeClient()) {
			generator.addProvider(new ModelDataProvider(generator, exfh));
		}
	}
}
