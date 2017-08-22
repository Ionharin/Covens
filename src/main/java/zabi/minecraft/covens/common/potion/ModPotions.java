package zabi.minecraft.covens.common.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.potion.potions.PotionDisrobing;
import zabi.minecraft.covens.common.potion.potions.PotionSkinRotting;
import zabi.minecraft.covens.common.potion.potions.PotionTinting;

@Mod.EventBusSubscriber
public class ModPotions {
	
	public static ModPotion disrobing, tinting, skin_rotting;
	
	public static void registerAll() {
		Log.i("Creating potions");
		disrobing = new PotionDisrobing(0xb40eff, "disrobing");
		tinting = new PotionTinting(0xffff99, "tinting");
		skin_rotting = new PotionSkinRotting(0x766d1b, "skin_rotting");
	}
	
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> evt) {
		Log.i("Registering potions");
		IForgeRegistry<Potion> reg = evt.getRegistry();
		reg.registerAll(disrobing, tinting, skin_rotting);
	}
}
