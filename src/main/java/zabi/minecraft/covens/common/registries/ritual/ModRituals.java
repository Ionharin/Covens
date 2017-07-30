package zabi.minecraft.covens.common.registries.ritual;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualEnderGate;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualEnderStream;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualHighMoon;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualIdentification;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualPerception;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualRedirection;
import zabi.minecraft.covens.common.registries.ritual.rituals.RitualSandsTime;

@Mod.EventBusSubscriber
public class ModRituals {
	
	private static RitualHighMoon high_moon_ritual = null;
	private static RitualSandsTime sand_time_ritual = null;
	private static RitualPerception perception_ritual = null;
	private static Ritual charging_ritual_cardinal_stone = null, charging_riutual_talisman = null;
	private static RitualEnderGate ender_gate = null;
	private static RitualEnderStream ender_stream = null;
	private static RitualRedirection redirection_ritual = null;
	private static RitualIdentification identification_ritual = null;
	
	
	public static void registerAll() {
		high_moon_ritual = new RitualHighMoon(of(new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.NETHERBRICK)), nop(), 100, circles(GlyphType.NORMAL, null, null), 2000, 0);
		sand_time_ritual = new RitualSandsTime(of(new ItemStack(Blocks.SAND), new ItemStack(Blocks.DIAMOND_ORE)), nop(), 24000, circles(GlyphType.NORMAL, GlyphType.NORMAL, GlyphType.ENDER), 4000, 4);
		perception_ritual = new RitualPerception(of(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GOLDEN_CARROT)), nop(), -1, circles(GlyphType.ENDER, GlyphType.ENDER, null), 1500, 5);
		charging_ritual_cardinal_stone = new Ritual(of(new ItemStack(ModItems.cardinal_stone), new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST)), of(new ItemStack(ModItems.cardinal_stone,1,1)), 80, circles(GlyphType.NORMAL, null, null), 2000, 0);
		ender_gate = new RitualEnderGate(of(new ItemStack(ModItems.cardinal_stone,1,2), new ItemStack(Items.ENDER_PEARL)), nop(), 100, circles(GlyphType.ENDER, null, null), 1000, 5);
		ender_stream = new RitualEnderStream(of(new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.ENDER_EYE), new ItemStack(ModItems.cardinal_stone,1,2), new ItemStack(Items.GOLD_INGOT)), nop(), -1, circles(GlyphType.ENDER,GlyphType.ENDER,GlyphType.ENDER), 0, 15);
		charging_riutual_talisman = new Ritual(of(new ItemStack(ModItems.misc, 1, 8), new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(ModItems.misc,1,6)), of(new ItemStack(ModItems.misc,1,9)), 80, circles(GlyphType.NORMAL, null, null), 3000, 0);
		redirection_ritual = new RitualRedirection(of(new ItemStack(Items.ENDER_PEARL), new ItemStack(ModItems.cardinal_stone,1,2)), nop(), -1, circles(GlyphType.NORMAL, GlyphType.ENDER, null), 2000, 6);
		identification_ritual = new RitualIdentification(of(new ItemStack(ModItems.soulstring,1,1), new ItemStack(Items.GOLD_NUGGET), new ItemStack(Items.PAPER)), nop(), 100, circles(GlyphType.NORMAL, null, null), 2000, 0);
		
		high_moon_ritual.setRegistryName(Reference.MID, "high_moon");
		sand_time_ritual.setRegistryName(Reference.MID, "time_sands");
		perception_ritual.setRegistryName(Reference.MID, "perception");
		charging_ritual_cardinal_stone.setRegistryName(Reference.MID, "charging_1");
		charging_riutual_talisman.setRegistryName(Reference.MID, "charging_talisman");
		ender_gate.setRegistryName(Reference.MID, "ender_gate");
		ender_stream.setRegistryName(Reference.MID, "ender_stream");
		identification_ritual.setRegistryName(Reference.MID, "identification");
	}
	
	public static NonNullList<ItemStack> of(ItemStack... list) {
		if (list==null||list.length==0) return nop();
		return NonNullList.<ItemStack>from(ItemStack.EMPTY, list);
	}
	public static NonNullList<ItemStack> nop() {
		return NonNullList.<ItemStack>create();
	}
	
	public static int circles(GlyphType small, BlockCircleGlyph.GlyphType medium, BlockCircleGlyph.GlyphType big) {
		if (small==null) throw new IllegalArgumentException("Cannot have the smaller circle missing");
		if (medium==null && big!=null) throw new IllegalArgumentException("Cannot have null middle circle when a big circle is present");
		if (small==GlyphType.GOLDEN || medium==GlyphType.GOLDEN || big==GlyphType.GOLDEN) throw new IllegalArgumentException("No golden circles allowed");
		int circleNum = 0;
		if (medium!=null) circleNum++;
		if (big!=null) circleNum++;
		return circleNum|small.ordinal()<<2|(medium==null?0:medium.ordinal()<<4)|(big==null?0:big.ordinal()<<6);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Ritual> evt) {
		Log.i("Registering rituals for "+Reference.NAME);
		IForgeRegistry<Ritual> ritualRegistry = evt.getRegistry();
		ritualRegistry.registerAll(high_moon_ritual, sand_time_ritual, perception_ritual, charging_ritual_cardinal_stone, ender_gate, ender_stream, charging_riutual_talisman, redirection_ritual, identification_ritual);
	}
}
