package zabi.minecraft.covens.common.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Reference;

public class ModItems {
	
	public static ItemChalk chalk;
	public static ItemBlock altar,chimney;
	public static ItemSeeds helleboreSeeds, aconitumSeeds, sagebrushSeeds, chrysanthemumSeeds;
	public static ItemFlowers flowers;
	public static ItemMisc misc;
	public static ItemEerieSeeds eerie_seeds;
	
	public static void registerAll() {
		chalk = new ItemChalk();
		flowers = new ItemFlowers();
		altar = new ItemBlock(ModBlocks.altar);
		chimney = new ItemBlock(ModBlocks.chimney);
		eerie_seeds = new ItemEerieSeeds();
		misc = new ItemMisc();
		GameRegistry.register(altar, ModBlocks.altar.getRegistryName());
		GameRegistry.register(chimney, ModBlocks.chimney.getRegistryName());
		
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
		
		GameRegistry.register(helleboreSeeds, new ResourceLocation(Reference.MID, "hellebore_seeds"));
		GameRegistry.register(aconitumSeeds, new ResourceLocation(Reference.MID, "aconitum_seeds"));
		GameRegistry.register(sagebrushSeeds, new ResourceLocation(Reference.MID, "sagebrush_seeds"));
		GameRegistry.register(chrysanthemumSeeds, new ResourceLocation(Reference.MID, "chrysanthemum_seeds"));
	}
}
