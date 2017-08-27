package zabi.minecraft.covens.common.potion;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.potion.potions.PotionBlockProjectiles;
import zabi.minecraft.covens.common.potion.potions.PotionDisrobing;
import zabi.minecraft.covens.common.potion.potions.PotionExtinguishFire;
import zabi.minecraft.covens.common.potion.potions.PotionPlanting;
import zabi.minecraft.covens.common.potion.potions.PotionSkinRotting;
import zabi.minecraft.covens.common.potion.potions.PotionTinting;

public class ModPotions {
	
	public static ModPotion disrobing, tinting, skin_rotting, extinguish_fire, planting, arrow_block;
	
	public static void registerAll() {
		Log.i("Creating potions");
		MinecraftForge.EVENT_BUS.register(new ModPotions());
		
		disrobing = new PotionDisrobing(0xb40eff, "disrobing");
		tinting = new PotionTinting(0xffff99, "tinting");
		skin_rotting = new PotionSkinRotting(0x766d1b, "skin_rotting");
		extinguish_fire = new PotionExtinguishFire(0x008080, "extinguish_fire");
		planting = new PotionPlanting(0x45c91c, "planting");
		arrow_block = new PotionBlockProjectiles(0x6f12bf, "block_arrows");
	}
	
	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> evt) {
		Log.i("Registering potions");
		evt.getRegistry().registerAll(disrobing, tinting, skin_rotting, extinguish_fire, planting, arrow_block);
	}
}
