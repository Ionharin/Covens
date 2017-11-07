package zabi.minecraft.covens.common.item;

import java.util.List;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.block.BlockModCrop;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;

@Mod.EventBusSubscriber
public class ModItems {
	
	public static Item chalk, altar, chimney, cauldron, aconitumSeeds, helleboreSeeds, sagebrushSeeds, chrysanthemumSeeds,
		flowers, misc, eerie_seeds, brew_drinkable, brew_splash, /*brew_gas,*/ brew_lingering, cardinal_stone, ritual_knife, broom,
		goblet, candle_plate, soulstring, barrel, candle, sapling, thread_spinner, spell_page, grimoire, /*witch_hat, witch_cloak,*/
		log_elder, log_juniper, log_yew, leaves_elder, leaves_juniper, leaves_yew, planks_elder, planks_juniper, planks_yew, 
		crystal_ball, ritual_candle, talisman_ruby_orb, talisman_diamond_star, talisman_emerald_pendant, talisman_watching_eye,
		talisman_aquamarine_crown, moonbell;
	
	public static void registerAll() {
		Log.i("Creating Items");
		MinecraftForge.EVENT_BUS.register(new ModItems());
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
			
			@Override
			public int getMetadata(int damage) {
				return damage;
			}
			
			@Override
			public boolean getHasSubtypes() {
				return true;
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
		brew_lingering = new ItemBrewBase("brew_lingering");
		ritual_knife = new ItemRitualKnife();
		leaves_elder = new ItemModLeaves((BlockLeaves) ModBlocks.leaves_elder);
		leaves_yew = new ItemModLeaves((BlockLeaves) ModBlocks.leaves_yew);
		leaves_juniper = new ItemModLeaves((BlockLeaves) ModBlocks.leaves_juniper);
		planks_yew = new ItemBlock(ModBlocks.planks_yew);
		planks_juniper = new ItemBlock(ModBlocks.planks_juniper);
		planks_elder = new ItemBlock(ModBlocks.planks_elder);
		candle_plate = new ItemBlock(ModBlocks.candle_plate);
		broom = new ItemBroom();
		soulstring = new ItemSoulString();
		candle = new ItemCandle();
		sapling = new ItemBlock(ModBlocks.sapling) {
			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return super.getUnlocalizedName(stack)+"_"+EnumSaplingType.values()[stack.getMetadata()].getName();
			}
			
			@Override
			public int getMetadata(int damage) {
				return damage;
			}
			
			@Override
			public boolean getHasSubtypes() {
				return true;
			}
		};
		thread_spinner = new ItemBlock(ModBlocks.thread_spinner);
		spell_page = new ItemSpellPage();
		grimoire = new ItemGrimoire();
//		witch_hat = new ItemWitchRobes(0, EntityEquipmentSlot.HEAD, "witch_hat");
//		witch_cloak = new ItemWitchRobes(1, EntityEquipmentSlot.CHEST, "witch_cloak");
		crystal_ball = new ItemBlock(ModBlocks.crystal_ball);
		ritual_candle = new ItemBlock(ModBlocks.ritual_candle);
		moonbell = new ItemBlock(ModBlocks.moonbell);
		
		altar.setRegistryName(ModBlocks.altar.getRegistryName());
		chimney.setRegistryName(ModBlocks.chimney.getRegistryName());
		cauldron.setRegistryName(ModBlocks.cauldron.getRegistryName());
		log_yew.setRegistryName(ModBlocks.log_yew.getRegistryName());
		log_juniper.setRegistryName(ModBlocks.log_juniper.getRegistryName());
		log_elder.setRegistryName(ModBlocks.log_elder.getRegistryName());
		leaves_juniper.setRegistryName(ModBlocks.leaves_juniper.getRegistryName());
		leaves_yew.setRegistryName(ModBlocks.leaves_yew.getRegistryName());
		leaves_elder.setRegistryName(ModBlocks.leaves_elder.getRegistryName());
		planks_yew.setRegistryName(ModBlocks.planks_yew.getRegistryName());
		planks_juniper.setRegistryName(ModBlocks.planks_juniper.getRegistryName());
		planks_elder.setRegistryName(ModBlocks.planks_elder.getRegistryName());
		goblet.setRegistryName(ModBlocks.goblet.getRegistryName());
		candle_plate.setRegistryName(ModBlocks.candle_plate.getRegistryName());
		barrel.setRegistryName(ModBlocks.barrel.getRegistryName());
		sapling.setRegistryName(ModBlocks.sapling.getRegistryName());
		thread_spinner.setRegistryName(ModBlocks.thread_spinner.getRegistryName());
		crystal_ball.setRegistryName(ModBlocks.crystal_ball.getRegistryName());
		ritual_candle.setRegistryName(ModBlocks.ritual_candle.getRegistryName());
		moonbell.setRegistryName(ModBlocks.moonbell.getRegistryName());
		
		talisman_aquamarine_crown = new ItemTalisman(4, 35, "aquamarine_crown", EntityEquipmentSlot.HEAD);
		talisman_diamond_star = new ItemTalisman(1, 18, "adamantine_star_ring", EntityEquipmentSlot.OFFHAND);
		talisman_emerald_pendant = new ItemTalisman(0, 18, "emerald_pendant", EntityEquipmentSlot.CHEST);
		talisman_ruby_orb = new ItemTalisman(6, 30, "ruby_orb", EntityEquipmentSlot.OFFHAND);
		talisman_watching_eye = new ItemTalisman(0, 18, "watching_eye", EntityEquipmentSlot.CHEST);
		
		helleboreSeeds = new ItemSeeds(ModBlocks.hellebore, Blocks.DIRT);
		aconitumSeeds = new ItemSeeds(ModBlocks.aconitum, Blocks.DIRT);
		sagebrushSeeds = new ItemSeeds(ModBlocks.sagebrush, Blocks.DIRT);
		chrysanthemumSeeds = new ItemSeeds(ModBlocks.chrysanthemum, Blocks.DIRT);
		
		aconitumSeeds.setCreativeTab(ModCreativeTabs.herbs);
		helleboreSeeds.setCreativeTab(ModCreativeTabs.herbs);
		sagebrushSeeds.setCreativeTab(ModCreativeTabs.herbs);
		chrysanthemumSeeds.setCreativeTab(ModCreativeTabs.herbs);
		
		((BlockModCrop)ModBlocks.hellebore).setSeeds(new ItemStack(helleboreSeeds,1,0)).setDropType(new ItemStack(flowers, 1, 1));
		((BlockModCrop)ModBlocks.aconitum).setSeeds(new ItemStack(aconitumSeeds)).setDropType(new ItemStack(flowers, 1, 0));
		((BlockModCrop)ModBlocks.sagebrush).setSeeds(new ItemStack(sagebrushSeeds)).setDropType(new ItemStack(flowers, 1, 2));
		((BlockModCrop)ModBlocks.chrysanthemum).setSeeds(new ItemStack(chrysanthemumSeeds)).setDropType(new ItemStack(flowers, 1, 3));
		
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
	public void registerItems(RegistryEvent.Register<Item> evt) {
		Log.i("Registering items");
		IForgeRegistry<Item> itemRegistry = evt.getRegistry();
		itemRegistry.registerAll(chalk, altar, chimney, cauldron, aconitumSeeds, helleboreSeeds, sagebrushSeeds, chrysanthemumSeeds,
				flowers, misc, eerie_seeds, brew_drinkable, brew_splash, /*brew_gas,*/ brew_lingering, cardinal_stone, ritual_knife, broom,
				goblet, candle_plate, soulstring, barrel, candle, sapling, thread_spinner, spell_page, grimoire, /*witch_hat, witch_cloak,*/
				crystal_ball, ritual_candle, talisman_aquamarine_crown, talisman_diamond_star, talisman_emerald_pendant,
				talisman_ruby_orb, talisman_watching_eye, moonbell
				);
		itemRegistry.registerAll(log_elder, log_juniper, log_yew, leaves_elder, leaves_juniper, leaves_yew, planks_elder, planks_juniper, planks_yew);
	}
}
