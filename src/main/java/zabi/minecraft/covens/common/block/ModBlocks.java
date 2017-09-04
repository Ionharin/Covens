package zabi.minecraft.covens.common.block;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	
	public static Block glyphs, altar, chimney, cauldron, hellebore, aconitum, sagebrush, chrysanthemum, 
		log_elder, log_yew, log_juniper, leaves_elder, leaves_yew, leaves_juniper, planks_yew, planks_juniper, planks_elder,
		confining_ash, goblet, candle_plate, barrel, sapling, thread_spinner, crystal_ball;
	
	public static void registerAll() {
		Log.i("Creating Blocks");
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
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
		sapling = new BlockModSapling();
		
		log_elder = new BlockModLog("elder");
		log_yew = new BlockModLog("yew");
		log_juniper = new BlockModLog("juniper");
		
		leaves_elder = new BlockModLeaves("elder");
		leaves_juniper = new BlockModLeaves("juniper");
		leaves_yew = new BlockModLeaves("yew");
		
		planks_yew = new BlockPlanks("yew");
		planks_juniper = new BlockPlanks("juniper");
		planks_elder = new BlockPlanks("elder");
		
		barrel = new BlockBarrel();
		thread_spinner = new BlockThreadSpinner();
		crystal_ball = new BlockCrystalBall();
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> evt) {
		Log.i("Registering blocks");
		IForgeRegistry<Block> blockRegistry = evt.getRegistry();
		blockRegistry.registerAll(glyphs, altar, chimney, cauldron, hellebore, aconitum, sagebrush, chrysanthemum, 
				log_elder, log_juniper, log_yew, leaves_elder, leaves_juniper, leaves_yew, planks_elder, planks_juniper, 
				planks_yew, confining_ash, goblet, candle_plate, barrel, sapling, thread_spinner, crystal_ball);
	}
}
