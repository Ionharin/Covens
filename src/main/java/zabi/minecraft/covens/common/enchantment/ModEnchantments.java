package zabi.minecraft.covens.common.enchantment;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEnchantments {
	
	public static Enchantment soul_siphon, curse_resilience, spell_shielding;

	public static void registerAll() {
		MinecraftForge.EVENT_BUS.register(new ModEnchantments());
		Log.i("Creating Enchantments");
		soul_siphon = new EnchantmentTalismans(Rarity.UNCOMMON, "soul_siphon");
		curse_resilience = new EnchantmentTalismans(Rarity.COMMON, "curse_resilience");
		spell_shielding = new EnchantmentTalismans(Rarity.UNCOMMON, "spell_shielding");
	}
	
	@SubscribeEvent
	public void registerEnchantment(RegistryEvent.Register<Enchantment> evt) {
		Log.i("Registering Enchantments");
		evt.getRegistry().registerAll(soul_siphon, curse_resilience, spell_shielding);
	}
}
