package zabi.minecraft.covens.common.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class ModPotions {
	
	public static ModPotion disrobing;
	
	public static void registerAll() {
		disrobing = new PotionDisrobing(0xb40eff, "disrobing");
	}
	
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> evt) {
		IForgeRegistry<Potion> reg = evt.getRegistry();
		reg.registerAll(disrobing);
	}
}
