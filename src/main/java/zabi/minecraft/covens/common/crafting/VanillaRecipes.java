package zabi.minecraft.covens.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

public class VanillaRecipes {
	public static void registerAll() {
		Log.i("Registering recipes");
		registerFurnaceRecipes();
		registerCraftingRecipes();
	}
	
	private static void registerCraftingRecipes() {
		GameRegistry.addShapelessRecipe(rl("wax"), null, new ItemStack(ModItems.misc,2,7), 
				Ingredient.fromStacks(new ItemStack(ModItems.misc,1,1)),
				Ingredient.fromStacks(new ItemStack(Items.CLAY_BALL)),
				Ingredient.fromStacks(new ItemStack(Items.DYE,1,EnumDyeColor.YELLOW.getDyeDamage()))
				);
		
		GameRegistry.addShapedRecipe(rl("chimney"), null, new ItemStack(ModBlocks.chimney), " c ", "ccc", "c c", 'c', Blocks.COBBLESTONE);
	}

	private static void registerFurnaceRecipes() {
		GameRegistry.addSmelting(Blocks.SAPLING, new ItemStack(ModItems.misc,1,1), 0);
	}
	
	private static ResourceLocation rl(String s) {
		return new ResourceLocation(Reference.MID, s);
	}
}
