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
		GameRegistry.addShapedRecipe(rl("chimney"), null, new ItemStack(ModBlocks.chimney), " c ", "ccc", "cbc", 'c', Blocks.COBBLESTONE, 'b', Items.BUCKET);
		GameRegistry.addShapedRecipe(rl("planks_elder"), null, new ItemStack(ModBlocks.elderPlanks,4), "w", 'w', ModBlocks.log_elder);
		GameRegistry.addShapedRecipe(rl("planks_yew"), null, new ItemStack(ModBlocks.yewPlanks,4), "w", 'w', ModBlocks.log_yew);
		GameRegistry.addShapedRecipe(rl("planks_juniper"), null, new ItemStack(ModBlocks.juniperPlanks,4), "w", 'w', ModBlocks.log_juniper);
		GameRegistry.addShapedRecipe(rl("altar"), null, new ItemStack(ModBlocks.altar), 
				"ccc", "sos", "sos", 
				'c', new ItemStack(Blocks.CARPET,1,14),
				's', Blocks.STONE,
				'o', new ItemStack(ModItems.misc,1,2)
				);
		GameRegistry.addShapedRecipe(rl("talisman"), null, new ItemStack(ModItems.misc,1,8), 
				"ini", "ndn", "ini",
				'i', Items.IRON_INGOT,
				'n', Items.IRON_NUGGET,
				'd', Items.DIAMOND
				);
		GameRegistry.addShapedRecipe(rl("talisman_rot"), null, new ItemStack(ModItems.misc,1,8), 
				"nin", "idi", "nin",
				'i', Items.IRON_INGOT,
				'n', Items.IRON_NUGGET,
				'd', Items.DIAMOND
				);
		GameRegistry.addShapedRecipe(rl("chalk"), null, new ItemStack(ModItems.chalk), 
				"bw ", "wfw", " wb",
				'b', new ItemStack(Items.DYE,1,EnumDyeColor.WHITE.getDyeDamage()),
				'w', new ItemStack(ModItems.misc,1,7),
				'f', new ItemStack(ModItems.flowers,1,2)
				);
		GameRegistry.addShapelessRecipe(rl("cauldron"), null, new ItemStack(ModBlocks.cauldron), 
				Ingredient.fromItem(Items.CAULDRON),
				Ingredient.fromStacks(new ItemStack(ModItems.misc,1,5)),
				Ingredient.fromStacks(new ItemStack(ModItems.flowers,1,1)),
				Ingredient.fromStacks(new ItemStack(ModItems.misc,1,1))
				);
		GameRegistry.addShapedRecipe(rl("cardinal_stone"), null, new ItemStack(ModItems.cardinal_stone), 
				"asa", "ses", "sss",
				'a', new ItemStack(ModItems.misc,1,1),
				's', Blocks.MOSSY_COBBLESTONE,
				'e', new ItemStack(ModItems.misc,1,4)
				);
		GameRegistry.addShapedRecipe(rl("goblet"), null, new ItemStack(ModBlocks.goblet), 
				"n n", "ntn", " i ",
				'n', Items.IRON_NUGGET,
				'i', Items.IRON_INGOT,
				't', new ItemStack(ModItems.misc,1,9)
				);
	}

	private static void registerFurnaceRecipes() {
		GameRegistry.addSmelting(Blocks.SAPLING, new ItemStack(ModItems.misc,1,1), 0);
	}
	
	private static ResourceLocation rl(String s) {
		return new ResourceLocation(Reference.MID, s);
	}
}
