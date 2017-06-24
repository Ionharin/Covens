package zabi.minecraft.covens.common.crafting.chimney;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Reference;

public class ModChimneyRecipes {
	public static void registerAll() {
		
		ChimneyRecipe def = new ChimneyRecipe(ItemStack.EMPTY, new ItemStack(ModItems.misc, 1, 2), true, false);
		ChimneyRecipe.setDefault(def);
		
		GameRegistry.register(
				def, 
				new ResourceLocation(Reference.MID, "cloudy_oil")
			);
		
		GameRegistry.register(
				new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,0), new ItemStack(ModItems.misc, 1, 2), true, false),
				new ResourceLocation(Reference.MID, "oak_fume")
			);
	}
}
