package zabi.minecraft.covens.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.item.ModItems;

public class VanillaRecipes {
	public static void registerAll() {
		GameRegistry.addSmelting(Blocks.SAPLING, new ItemStack(ModItems.misc,1,1), 0);
	}
}
