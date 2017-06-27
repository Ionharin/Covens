package zabi.minecraft.covens.common.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModItems {
	
	public static ItemChalk chalk;
	public static ItemBlock altar,chimney,cauldron;
	public static ItemSeeds helleboreSeeds, aconitumSeeds, sagebrushSeeds, chrysanthemumSeeds;
	public static ItemEgressStone waystone;
	public static ItemFlowers flowers;
	public static ItemBrewBase brew_drinkable, brew_splash, brew_gas, brew_lingering;
	public static ItemMisc misc;
	public static ItemEerieSeeds eerie_seeds;
	
	public static void registerAll() {
		chalk = new ItemChalk();
		flowers = new ItemFlowers();
		altar = new ItemBlock(ModBlocks.altar);
		chimney = new ItemBlock(ModBlocks.chimney);
		cauldron = new ItemBlock(ModBlocks.cauldron);
		waystone = new ItemEgressStone();
		eerie_seeds = new ItemEerieSeeds();
		misc = new ItemMisc();
		brew_drinkable = new ItemBrewDrinkable();
		brew_splash = new ItemBrewSplash();
		brew_gas = new ItemBrewGas();
		brew_lingering = new ItemBrewLingering();
		altar.setRegistryName(ModBlocks.altar.getRegistryName());
		chimney.setRegistryName(ModBlocks.chimney.getRegistryName());
		cauldron.setRegistryName(ModBlocks.cauldron.getRegistryName());
		
		helleboreSeeds = new ItemSeeds(ModBlocks.hellebore, Blocks.DIRT);
		aconitumSeeds = new ItemSeeds(ModBlocks.aconitum, Blocks.DIRT);
		sagebrushSeeds = new ItemSeeds(ModBlocks.sagebrush, Blocks.DIRT);
		chrysanthemumSeeds = new ItemSeeds(ModBlocks.chrysanthemum, Blocks.DIRT);
		
		aconitumSeeds.setCreativeTab(ModCreativeTabs.herbs);
		helleboreSeeds.setCreativeTab(ModCreativeTabs.herbs);
		sagebrushSeeds.setCreativeTab(ModCreativeTabs.herbs);
		chrysanthemumSeeds.setCreativeTab(ModCreativeTabs.herbs);
		
		ModBlocks.hellebore.setSeeds(new ItemStack(helleboreSeeds,1,0)).setDropType(new ItemStack(flowers, 1, 1));
		ModBlocks.aconitum.setSeeds(new ItemStack(aconitumSeeds)).setDropType(new ItemStack(flowers, 1, 0));
		ModBlocks.sagebrush.setSeeds(new ItemStack(sagebrushSeeds)).setDropType(new ItemStack(flowers, 1, 2));
		ModBlocks.chrysanthemum.setSeeds(new ItemStack(chrysanthemumSeeds)).setDropType(new ItemStack(flowers, 1, 3));
		
		helleboreSeeds.setUnlocalizedName("hellebore_seeds");
		aconitumSeeds.setUnlocalizedName("aconitum_seeds");
		sagebrushSeeds.setUnlocalizedName("sagebrush_seeds");
		chrysanthemumSeeds.setUnlocalizedName("chrysanthemum_seeds");

		helleboreSeeds.setRegistryName(Reference.MID, "hellebore_seeds");
		aconitumSeeds.setRegistryName(Reference.MID, "aconitum_seeds");
		sagebrushSeeds.setRegistryName(Reference.MID, "sagebrush_seeds");
		chrysanthemumSeeds.setRegistryName(Reference.MID, "chrysanthemum_seeds");
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Item> evt) {
		Log.i("Registering items for "+Reference.NAME);
		IForgeRegistry<Item> itemRegistry = evt.getRegistry();
		itemRegistry.registerAll(chalk, altar, chimney, cauldron, helleboreSeeds, aconitumSeeds, sagebrushSeeds, chrysanthemumSeeds,
				flowers, misc, eerie_seeds, brew_drinkable, brew_splash, brew_gas, brew_lingering, waystone
				);
	}
}
