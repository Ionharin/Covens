package zabi.minecraft.covens.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModItems {
	
	public static ItemChalk chalk;
	public static ItemBlock altar,chimney,cauldron, log_yew, log_juniper, log_elder, yewPlanks, juniperPlanks, elderPlanks, goblet;
	public static ItemBlock candle_plate, barrel, sapling;
	public static ItemSeeds helleboreSeeds, aconitumSeeds, sagebrushSeeds, chrysanthemumSeeds;
	public static ItemCardinalStone cardinal_stone;
	public static ItemFlowers flowers;
	public static ItemBrewBase brew_drinkable, brew_splash, brew_gas, brew_lingering;
	public static ItemMisc misc;
	public static ItemRitualKnife ritual_knife;
	public static ItemEerieSeeds eerie_seeds;
	public static ItemModLeaves leaves_yew, leaves_elder, leaves_juniper;
	public static ItemBroom broom;
	public static ItemSoulString soulstring;
	public static ItemCandle candle;
	
	public static void registerAll() {
		Log.i("Creating Items");
		chalk = new ItemChalk();
		flowers = new ItemFlowers();
		altar = new ItemBlock(ModBlocks.altar);
		chimney = new ItemBlock(ModBlocks.chimney) {
			@Override
			public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
				tooltip.add(TextFormatting.GRAY+TextFormatting.ITALIC.toString()+I18n.format("tile.chimney.hint"));
			}
		};
		barrel = new ItemBlock(ModBlocks.barrel) {
			
			@Override
			public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
				if (this.isInCreativeTab(tab)) {
					for (int i=0;i<BlockBarrel.WoodType.values().length;i++) items.add(new ItemStack(this,1,i));
				}
			}
			
			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return super.getUnlocalizedName(stack)+"."+BlockBarrel.WoodType.values()[stack.getMetadata()].getName();
			}
		};
		cauldron = new ItemBlock(ModBlocks.cauldron);
		log_yew = new ItemBlock(ModBlocks.log_yew);
		log_juniper = new ItemBlock(ModBlocks.log_juniper);
		log_elder = new ItemBlock(ModBlocks.log_elder);
		goblet = new ItemBlock(ModBlocks.goblet);
		cardinal_stone = new ItemCardinalStone();
		eerie_seeds = new ItemEerieSeeds();
		misc = new ItemMisc();
		brew_drinkable = new ItemBrewDrinkable();
		brew_splash = new ItemBrewBase("brew_splash");
		brew_gas = new ItemBrewBase("brew_gas");
		brew_lingering = new ItemBrewBase("brew_lingering");
		ritual_knife = new ItemRitualKnife();
		leaves_elder = new ItemModLeaves(ModBlocks.leaves_elder);
		leaves_yew = new ItemModLeaves(ModBlocks.leaves_yew);
		leaves_juniper = new ItemModLeaves(ModBlocks.leaves_juniper);
		yewPlanks = new ItemBlock(ModBlocks.planks_yew);
		juniperPlanks = new ItemBlock(ModBlocks.planks_juniper);
		elderPlanks = new ItemBlock(ModBlocks.planks_elder);
		candle_plate = new ItemBlock(ModBlocks.candle_plate);
		broom = new ItemBroom();
		soulstring = new ItemSoulString();
		candle = new ItemCandle();
		sapling = new ItemBlock(ModBlocks.sapling) {
			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return super.getUnlocalizedName(stack)+"_"+EnumSaplingType.values()[stack.getMetadata()].getName();
			}
		};
		
		altar.setRegistryName(ModBlocks.altar.getRegistryName());
		chimney.setRegistryName(ModBlocks.chimney.getRegistryName());
		cauldron.setRegistryName(ModBlocks.cauldron.getRegistryName());
		log_yew.setRegistryName(ModBlocks.log_yew.getRegistryName());
		log_juniper.setRegistryName(ModBlocks.log_juniper.getRegistryName());
		log_elder.setRegistryName(ModBlocks.log_elder.getRegistryName());
		leaves_juniper.setRegistryName(ModBlocks.leaves_juniper.getRegistryName());
		leaves_yew.setRegistryName(ModBlocks.leaves_yew.getRegistryName());
		leaves_elder.setRegistryName(ModBlocks.leaves_elder.getRegistryName());
		yewPlanks.setRegistryName(ModBlocks.planks_yew.getRegistryName());
		juniperPlanks.setRegistryName(ModBlocks.planks_juniper.getRegistryName());
		elderPlanks.setRegistryName(ModBlocks.planks_elder.getRegistryName());
		goblet.setRegistryName(ModBlocks.goblet.getRegistryName());
		candle_plate.setRegistryName(ModBlocks.candle_plate.getRegistryName());
		barrel.setRegistryName(ModBlocks.barrel.getRegistryName());
		sapling.setRegistryName(ModBlocks.sapling.getRegistryName());
		
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
		
		sapling.setHasSubtypes(true);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Item> evt) {
		Log.i("Registering items");
		IForgeRegistry<Item> itemRegistry = evt.getRegistry();
		itemRegistry.registerAll(chalk, altar, chimney, cauldron, aconitumSeeds, helleboreSeeds, sagebrushSeeds, chrysanthemumSeeds,
				flowers, misc, eerie_seeds, brew_drinkable, brew_splash, brew_gas, brew_lingering, cardinal_stone, ritual_knife, broom,
				goblet, candle_plate, soulstring, barrel, candle, sapling
				);
		itemRegistry.registerAll(log_elder, log_juniper, log_yew, leaves_elder, leaves_juniper, leaves_yew, elderPlanks, juniperPlanks, yewPlanks);
	}
}
