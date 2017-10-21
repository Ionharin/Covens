package zabi.minecraft.covens.common.registries.brewing.environmental;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.init.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.integration.Lilliputian;
import zabi.minecraft.covens.common.potion.ModPotions;

public class ModEnvironmentalPotionEffects {
	public static EnvironmentalPotionEffect planting, extinguishing, harvest, growth;

	@SubscribeEvent
	public void registerEnvironmentalEffects(RegistryEvent.Register<EnvironmentalPotionEffect> evt) {
		Log.i("Registering environmental effects");
		
		evt.getRegistry().registerAll(planting, extinguishing, harvest, growth);
	}

	public static void registerAll() {
		Log.i("Creating environmental effects");
		MinecraftForge.EVENT_BUS.register(new ModEnvironmentalPotionEffects());
		
		planting = new PlantingEffect(ModPotions.planting);
		extinguishing = new ExtinguishFireEffect(ModPotions.extinguish_fire);
		harvest = new HarvestEffect(ModPotions.harvest);
		if (Lilliputian.grow!=null) growth = new GrowthEffect(Lilliputian.grow);
		else growth = new GrowthEffect(MobEffects.INSTANT_HEALTH);
	}
}
