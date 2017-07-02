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
	public static ItemBlock altar,chimney,cauldron, log_yew, log_juniper, log_elder, yewPlanks, juniperPlanks, elderPlanks;
	public static ItemSeeds helleboreSeeds, aconitumSeeds, sagebrushSeeds, chrysanthemumSeeds;
	public static ItemCardinalStone waystone;
	public static ItemFlowers flowers;
	public static ItemBrewBase brew_drinkable, brew_splash, brew_gas, brew_lingering;
	public static ItemMisc misc;
	public static ItemRitualKnife ritual_knife;
	public static ItemEerieSeeds eerie_seeds;
	public static ItemModLeaves leaves_yew, leaves_elder, leaves_juniper;
	
	public static void registerAll() {
		chalk = new ItemChalk();
		flowers = new ItemFlowers();
		altar = new ItemBlock(ModBlocks.altar);
		chimney = new ItemBlock(ModBlocks.chimney);
		cauldron = new ItemBlock(ModBlocks.cauldron);
		log_yew = new ItemBlock(ModBlocks.log_yew);
		log_juniper = new ItemBlock(ModBlocks.log_juniper);
		log_elder = new ItemBlock(ModBlocks.log_elder);
		waystone = new ItemCardinalStone();
		eerie_seeds = new ItemEerieSeeds();
		misc = new ItemMisc();
		brew_drinkable = new ItemBrewDrinkable();
		brew_splash = new ItemBrewSplash();
		brew_gas = new ItemBrewGas();
		ritual_knife = new ItemRitualKnife();
		brew_lingering = new ItemBrewLingering();
		leaves_elder = new ItemModLeaves(ModBlocks.leaves_elder);
		leaves_yew = new ItemModLeaves(ModBlocks.leaves_yew);
		leaves_juniper = new ItemModLeaves(ModBlocks.leaves_juniper);
		yewPlanks = new ItemBlock(ModBlocks.yewPlanks);
		juniperPlanks = new ItemBlock(ModBlocks.juniperPlanks);
		elderPlanks = new ItemBlock(ModBlocks.elderPlanks);
		
		altar.setRegistryName(ModBlocks.altar.getRegistryName());
		chimney.setRegistryName(ModBlocks.chimney.getRegistryName());
		cauldron.setRegistryName(ModBlocks.cauldron.getRegistryName());
		log_yew.setRegistryName(ModBlocks.log_yew.getRegistryName());
		log_juniper.setRegistryName(ModBlocks.log_juniper.getRegistryName());
		log_elder.setRegistryName(ModBlocks.log_elder.getRegistryName());
		leaves_juniper.setRegistryName(ModBlocks.leaves_juniper.getRegistryName());
		leaves_yew.setRegistryName(ModBlocks.leaves_yew.getRegistryName());
		leaves_elder.setRegistryName(ModBlocks.leaves_elder.getRegistryName());
		yewPlanks.setRegistryName(ModBlocks.yewPlanks.getRegistryName());
		juniperPlanks.setRegistryName(ModBlocks.juniperPlanks.getRegistryName());
		elderPlanks.setRegistryName(ModBlocks.elderPlanks.getRegistryName());
		
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
				flowers, misc, eerie_seeds, brew_drinkable, brew_splash, brew_gas, brew_lingering, waystone, ritual_knife
				);
		itemRegistry.registerAll(log_elder, log_juniper, log_yew, leaves_elder, leaves_yew, leaves_juniper, yewPlanks, juniperPlanks, elderPlanks);
	}
}
