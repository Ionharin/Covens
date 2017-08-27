package zabi.minecraft.covens.common.registries.spell;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;
import zabi.minecraft.covens.common.registries.spell.spells.SpellMagnet;

public class ModSpells {
	
	public static Spell magnet;
	
	public static void registerAll() {
		MinecraftForge.EVENT_BUS.register(new ModSpells());
		magnet = new SpellMagnet(0x999999, EnumSpellType.PROJECTILE_ENTITY, "magnet", Reference.MID);
	}
	
	@SubscribeEvent
	public void onSpellRegistration(RegistryEvent.Register<Spell> evt) {
		evt.getRegistry().registerAll(
				magnet
		);
	}
	
}
