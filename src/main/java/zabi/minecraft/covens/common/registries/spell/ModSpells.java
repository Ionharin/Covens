package zabi.minecraft.covens.common.registries.spell;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;
import zabi.minecraft.covens.common.registries.spell.spells.SpellActivation;
import zabi.minecraft.covens.common.registries.spell.spells.SpellBlink;
import zabi.minecraft.covens.common.registries.spell.spells.SpellDestabilization;
import zabi.minecraft.covens.common.registries.spell.spells.SpellLesserBlinking;
import zabi.minecraft.covens.common.registries.spell.spells.SpellMagnet;
import zabi.minecraft.covens.common.registries.spell.spells.SpellPoke;
import zabi.minecraft.covens.common.registries.spell.spells.SpellSlowness;
import zabi.minecraft.covens.common.registries.spell.spells.SpellWater;

public class ModSpells {
	
	public static Spell magnet, poke, water, activation, slowness, lesser_blink, blink, explosion;
	
	public static void registerAll() {
		MinecraftForge.EVENT_BUS.register(new ModSpells());
		magnet = new SpellMagnet(0xa5cec9, EnumSpellType.PROJECTILE_BLOCK, "magnet", Reference.MID);
		poke = new SpellPoke(0x6d1e10, EnumSpellType.PROJECTILE_ALL, "poke", Reference.MID);
		water = new SpellWater(0x1644a7, EnumSpellType.PROJECTILE_BLOCK, "water", Reference.MID);
		activation = new SpellActivation(0x42b3bd, EnumSpellType.PROJECTILE_BLOCK, "activation", Reference.MID);
		slowness = new SpellSlowness(0x4d6910, EnumSpellType.PROJECTILE_ENTITY, "slowness", Reference.MID);
		lesser_blink = new SpellLesserBlinking(0x9042bd, EnumSpellType.INSTANT, "lesser_blink", Reference.MID);
		blink = new SpellBlink(0xcb33e7, EnumSpellType.PROJECTILE_BLOCK, "blink", Reference.MID);
		explosion = new SpellDestabilization(0x5e0505, EnumSpellType.PROJECTILE_ALL, "explosion", Reference.MID);
	}
	
	@SubscribeEvent
	public void onSpellRegistration(RegistryEvent.Register<Spell> evt) {
		evt.getRegistry().registerAll(
				magnet, poke, water, activation, slowness, lesser_blink, blink, explosion
		);
	}
	
}
