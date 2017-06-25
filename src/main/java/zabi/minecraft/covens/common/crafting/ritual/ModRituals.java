package zabi.minecraft.covens.common.crafting.ritual;

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
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualHighMoon;
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualPerception;
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualSandsTime;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModRituals {
	
	private static final RitualHighMoon high_moon_ritual = new RitualHighMoon(of(new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.NETHERBRICK)), nop(), 100, circles(GlyphType.NORMAL, null, null), 2000, 0);
	private static final RitualSandsTime sand_time_ritual = new RitualSandsTime(of(new ItemStack(Blocks.SAND), new ItemStack(Blocks.DIAMOND_ORE)), nop(), 24000, circles(GlyphType.NORMAL, GlyphType.NORMAL, GlyphType.ENDER), 4000, 4);
	private static final RitualPerception perception_ritual = new RitualPerception(of(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.GOLDEN_CARROT)), nop(), 24000, circles(GlyphType.ENDER, GlyphType.ENDER, null), 1500, 5);
	private static final Ritual test_ritual = new Ritual(of(new ItemStack(Items.APPLE)), of(new ItemStack(Items.APPLE)), 20, circles(GlyphType.NORMAL, null, null), 10000, 0);
	
	
	public static void registerAll() {
		test_ritual.setRegistryName(Reference.MID, "test");
		high_moon_ritual.setRegistryName(Reference.MID, "high_moon");
		sand_time_ritual.setRegistryName(Reference.MID, "timeSand");
		perception_ritual.setRegistryName(Reference.MID, "perception");
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
		ritualRegistry.registerAll(high_moon_ritual, sand_time_ritual, test_ritual, perception_ritual);
	}
}
