package zabi.minecraft.covens.common.item;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.block.ModBlocks;

public class ModItems {
	
	public static ItemChalk chalk;
	public static ItemBlock altar;
	
	public static void registerAll() {
		chalk = new ItemChalk();
		
		altar = new ItemBlock(ModBlocks.altar);
		GameRegistry.register(altar, ModBlocks.altar.getRegistryName());
	}
}
