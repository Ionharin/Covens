package zabi.minecraft.covens.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;

public class OreDict {
	public static void registerAll() {
		OreDictionary.registerOre("logWood", ModBlocks.log_elder);
		OreDictionary.registerOre("logWood", ModBlocks.log_juniper);
		OreDictionary.registerOre("logWood", ModBlocks.log_yew);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_elder);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_juniper);
		OreDictionary.registerOre("plankWood", ModBlocks.planks_yew);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_elder);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_juniper);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_yew);
		OreDictionary.registerOre("treeSapling", ModBlocks.sapling);
		
		OreDictionary.registerOre("ingotSilver", new ItemStack(ModItems.misc,1,16));
		OreDictionary.registerOre("nuggetSilver", new ItemStack(ModItems.misc,1,17));
	}
}
