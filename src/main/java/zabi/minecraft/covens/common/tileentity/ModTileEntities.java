package zabi.minecraft.covens.common.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

public class ModTileEntities {
	public static void registerAll() {
		Log.i("Registering tile entities for "+Reference.NAME);
		GameRegistry.registerTileEntity(TileEntityGlyph.class, "covens:ritual_glyph");
		GameRegistry.registerTileEntity(TileEntityAltar.class, "covens:witch_altar");
		GameRegistry.registerTileEntity(TileEntityChimney.class, "covens:chimney");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "covens:cauldron");
	}
	
}
