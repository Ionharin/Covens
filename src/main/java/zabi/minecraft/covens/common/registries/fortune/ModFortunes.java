package zabi.minecraft.covens.common.registries.fortune;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.registries.fortune.fortunes.FortuneMeetZombie;

public class ModFortunes {
	
	private static Fortune zombie;
	
	public static void registerAll() {
		Log.i("Creating fortunes");
		MinecraftForge.EVENT_BUS.register(new ModFortunes());
		zombie = new FortuneMeetZombie(10, "meet_zombie", Reference.MID);
	}
	
	@SubscribeEvent
	public void onRegisterFortunes(RegistryEvent.Register<Fortune> evt) {
		Log.i("Registering fortunes");
		evt.getRegistry().registerAll(
				zombie
		);
	}
	
}
