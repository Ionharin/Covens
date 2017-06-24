package zabi.minecraft.covens.common.crafting.ritual;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualHighMoon;
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualPerception;
import zabi.minecraft.covens.common.crafting.ritual.rituals.RitualSandsTime;
import zabi.minecraft.covens.common.lib.Reference;

public class ModRituals {
	public static void registerAll() {
		Ritual.REGISTRY.register(
				new RitualHighMoon(
						of(new ItemStack[] {new ItemStack(Items.COAL)}), 
						nop(), 
						100, circles(GlyphType.NORMAL, null, null), 1000, 0));
		Ritual.REGISTRY.register(
				new Ritual(of(new ItemStack(Items.APPLE)), 
						of(new ItemStack[] {new ItemStack(Items.COAL)}), 
						10, circles(GlyphType.NETHER, GlyphType.NETHER, null), 0, 5)
				.setRegistryName(Reference.MID, "test"));
		Ritual.REGISTRY.register(
				new RitualSandsTime(
						of(new ItemStack(Blocks.SAND)), 
						nop(), 
						24000, circles(GlyphType.NORMAL, GlyphType.NORMAL, GlyphType.ENDER), 1000, 5));
		Ritual.REGISTRY.register(
					new RitualPerception(of(new ItemStack(Blocks.GLOWSTONE), new ItemStack(Items.DRAGON_BREATH)), 
							nop(), 24000, 
							circles(GlyphType.ENDER, GlyphType.ENDER, null)
							, 500, 8)
			);
	}
	
	public static NonNullList<ItemStack> of(ItemStack... list) {
		if (list==null||list.length==0) return nop();
		return NonNullList.<ItemStack>func_193580_a(ItemStack.EMPTY, list);
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
}
