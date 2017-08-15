package zabi.minecraft.covens.common.registries;

import net.minecraftforge.oredict.OreDictionary;
import zabi.minecraft.covens.common.block.ModBlocks;

public class OreDict {
	public static void registerAll() {
		OreDictionary.registerOre("logWood", ModBlocks.log_elder);
		OreDictionary.registerOre("logWood", ModBlocks.log_juniper);
		OreDictionary.registerOre("logWood", ModBlocks.log_yew);
		OreDictionary.registerOre("plankWood", ModBlocks.elderPlanks);
		OreDictionary.registerOre("plankWood", ModBlocks.juniperPlanks);
		OreDictionary.registerOre("plankWood", ModBlocks.yewPlanks);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_elder);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_juniper);
		OreDictionary.registerOre("treeLeaves", ModBlocks.leaves_yew);
	}
}
