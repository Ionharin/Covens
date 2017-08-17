package zabi.minecraft.covens.common.block;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.lib.Log;

@Mod.EventBusSubscriber
public class ModBlocks {
	
	public static BlockCircleGlyph glyphs;
	public static BlockWitchAltar altar;
	public static BlockChimney chimney;
	public static BlockCauldron cauldron;
	public static BlockModCrop hellebore, aconitum, sagebrush, chrysanthemum;
	public static BlockModLog log_elder, log_yew, log_juniper;
	public static BlockModLeaves leaves_elder, leaves_yew, leaves_juniper;
	public static BlockPlanks yewPlanks, juniperPlanks, elderPlanks;
	public static BlockConfiningAsh confining_ash;
	public static BlockGoblet goblet;
	public static BlockCandlePlate candle_plate;
	public static BlockBarrel barrel;
	
	public static void registerAll() {
		Log.i("Creating Blocks");
		glyphs = new BlockCircleGlyph();
		altar = new BlockWitchAltar();
		chimney = new BlockChimney();
		cauldron = new BlockCauldron();
		hellebore = new BlockModCrop("hellebore");
		aconitum = new BlockModCrop("aconitum");
		sagebrush = new BlockModCrop("sagebrush");
		chrysanthemum = new BlockModCrop("chrysanthemum");
		confining_ash = new BlockConfiningAsh();
		goblet = new BlockGoblet();
		candle_plate = new BlockCandlePlate();
		
		log_elder = new BlockModLog("elder");
		log_yew = new BlockModLog("yew");
		log_juniper = new BlockModLog("juniper");
		
		leaves_elder = new BlockModLeaves("elder");
		leaves_juniper = new BlockModLeaves("juniper");
		leaves_yew = new BlockModLeaves("yew");
		
		yewPlanks = new BlockPlanks("yew");
		juniperPlanks = new BlockPlanks("juniper");
		elderPlanks = new BlockPlanks("elder");
		
		barrel = new BlockBarrel();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		Log.i("Registering blocks");
		IForgeRegistry<Block> blockRegistry = evt.getRegistry();
		blockRegistry.registerAll(glyphs, altar, chimney, cauldron, hellebore, aconitum, sagebrush, chrysanthemum, 
				log_elder, log_juniper, log_yew, leaves_elder, leaves_juniper, leaves_yew, elderPlanks, juniperPlanks, 
				yewPlanks, confining_ash, goblet, candle_plate, barrel);
	}
}
