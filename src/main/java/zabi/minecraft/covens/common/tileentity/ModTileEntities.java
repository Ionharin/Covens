package zabi.minecraft.covens.common.tileentity;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.lib.Log;

public class ModTileEntities {
	public static void registerAll() {
		Log.i("Registering tile entities");
		GameRegistry.registerTileEntity(TileEntityGlyph.class, "covens:ritual_glyph");
		GameRegistry.registerTileEntity(TileEntityAltar.class, "covens:witch_altar");
		GameRegistry.registerTileEntity(TileEntityChimney.class, "covens:chimney");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "covens:cauldron");
		GameRegistry.registerTileEntity(TileEntityBarrel.class, "covens:barrel");
		GameRegistry.registerTileEntity(TileEntityThreadSpinner.class, "covens:thread_spinner");
		GameRegistry.registerTileEntity(TileEntityCrystalBall.class, "covens:crystal_ball");
		GameRegistry.registerTileEntity(TileEntityRitualCandle.class, "covens:ritual_candle");
		GameRegistry.registerTileEntity(TileEntitySilverVat.class, "covens:silver_vat");
	}
	
}
