package zabi.minecraft.covens.common.registries.spell;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;
import zabi.minecraft.covens.common.registries.spell.spells.SpellMagnet;
import zabi.minecraft.covens.common.registries.spell.spells.SpellPoke;

public class ModSpells {
	
	public static Spell magnet, poke;
	
	public static void registerAll() {
		MinecraftForge.EVENT_BUS.register(new ModSpells());
		magnet = new SpellMagnet(0xa5cec9, EnumSpellType.PROJECTILE_BLOCK, "magnet", Reference.MID);
		poke = new SpellPoke(0x6d1e10, EnumSpellType.PROJECTILE_ENTITY, "poke", Reference.MID);
	}
	
	@SubscribeEvent
	public void onSpellRegistration(RegistryEvent.Register<Spell> evt) {
		evt.getRegistry().registerAll(
				magnet, poke
		);
	}
	
}
