package zabi.minecraft.covens.common.block;

public class ModBlocks {
	
	public static BlockCircleGlyph glyphs;
	public static BlockWitchAltar altar;
	
	public static void registerAll() {
		glyphs = new BlockCircleGlyph();
		altar = new BlockWitchAltar();
	}
}
