package zabi.minecraft.covens.common.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	public static void registerAll() {
		GameRegistry.registerTileEntity(TileEntityGlyph.class, "covens:ritual_glyph");
		GameRegistry.registerTileEntity(TileEntityAltar.class, "covens:witch_altar");
	}
}
