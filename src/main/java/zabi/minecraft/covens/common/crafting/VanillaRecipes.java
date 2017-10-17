package zabi.minecraft.covens.common.crafting;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.QuickItemStacks;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;

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
				new OreIngredient("dyeYellow")
				);
		
		GameRegistry.addShapelessRecipe(rl("eerie_seeds"), null, new ItemStack(ModItems.eerie_seeds,4), 
				Ingredient.fromItem(Items.WHEAT_SEEDS),
				Ingredient.fromStacks(new ItemStack(ModItems.misc,1,3)),
				Ingredient.fromStacks(new ItemStack(ModItems.flowers,1,0)),
				Ingredient.fromItem(Items.EGG)
				);
		GameRegistry.addShapedRecipe(rl("chimney"), null, new ItemStack(ModBlocks.chimney), " c ", "ccc", "cbc", 'c', Blocks.COBBLESTONE, 'b', Items.BUCKET);
		GameRegistry.addShapedRecipe(rl("planks_elder"), null, new ItemStack(ModBlocks.planks_elder,4), "w", 'w', ModBlocks.log_elder);
		GameRegistry.addShapedRecipe(rl("planks_yew"), null, new ItemStack(ModBlocks.planks_yew,1), "w", 'w', ModBlocks.log_yew);
		GameRegistry.addShapedRecipe(rl("planks_juniper"), null, new ItemStack(ModBlocks.planks_juniper,4), "w", 'w', ModBlocks.log_juniper);
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
		GameRegistry.addShapedRecipe(rl("candle_plate"), null, new ItemStack(ModBlocks.candle_plate), 
				"f f", "fnf", "ntn",
				'n', Items.IRON_INGOT,
				'f', new ItemStack(ModItems.candle,1,0), 
				't', new ItemStack(ModItems.misc,1,9)
				);
		GameRegistry.addShapedRecipe(rl("clay_jar"), null, new ItemStack(ModItems.misc,8,10), 
				" t ", "ttt", "ttt",
				't', Items.CLAY_BALL
				);
		for (int i=0;i<BlockBarrel.WoodType.values().length;i++)
			GameRegistry.addShapedRecipe(rl("barrel_"+BlockBarrel.WoodType.values()[i].getName()), null, new ItemStack(ModBlocks.barrel,1,i), 
					"iwi", "wtw", "www",
					't', new ItemStack(ModItems.misc,1,9),
					'i', Items.IRON_INGOT,
					'w', getWoodFromType(i)
					);
		GameRegistry.addShapedRecipe(rl("candle"), null, new ItemStack(ModItems.candle), 
					"s", "w", "w",
					's', Items.STRING,
					'w', new ItemStack(ModItems.misc,1,7)
					);
		GameRegistry.addShapedRecipe(rl("grimoire"), null, new ItemStack(ModItems.grimoire), 
					"ltl", "lbl", "lal",
					'l', new ItemStack(ModItems.misc,1,15),
					't', QuickItemStacks.charged_talisman,
					'b', Items.ENCHANTED_BOOK,
					'a', new ItemStack(ModItems.misc,1,11)
				);
		GameRegistry.addShapedRecipe(rl("magic_leather"), null, new ItemStack(ModItems.misc,5,15), true,
				"lol", "bla", "lsl",
				'l', Items.LEATHER,
				'o', new ItemStack(ModItems.misc,1,2),
				'b', new ItemStack(ModItems.misc,1,3),
				'a', new ItemStack(ModItems.misc,1,4),
				's', new ItemStack(ModItems.misc,1,5)
			);
		GameRegistry.addShapedRecipe(rl("witch_hat"), null, new ItemStack(ModItems.witch_hat), 
				" l ", "l l", "lol", 
				'l', new ItemStack(ModItems.misc,1,15),
				'o', new OreIngredient("dyeBlack")
			);
		GameRegistry.addShapedRecipe(rl("witch_cloak"), null, new ItemStack(ModItems.witch_cloak), 
				"lll", "lll", "lol", 
				'l', new ItemStack(ModItems.misc,1,15),
				'o', new OreIngredient("dyeBlack")
			);
	}

	private static ItemStack getWoodFromType(int i) {
		if (i<4) return new ItemStack(Blocks.LOG,1,i);
		if (i<6) return new ItemStack(Blocks.LOG2,1,i-4);
		if (i==6) return new ItemStack(ModBlocks.log_elder);
		if (i==7) return new ItemStack(ModBlocks.log_juniper);
		if (i==8) return new ItemStack(ModBlocks.log_yew);
		return ItemStack.EMPTY;
	}

	private static void registerFurnaceRecipes() {
		ItemStack ash = new ItemStack(ModItems.misc,1,1);
		OreDictionary.getOres("treeSapling").stream()
		.map(s -> Block.getBlockFromItem(s.getItem()))
		.distinct().forEach(b -> GameRegistry.addSmelting(b, ash, 0));
		GameRegistry.addSmelting(new ItemStack(ModItems.misc,1,10), new ItemStack(ModItems.misc,1,0), 0);
	}
	
	private static ResourceLocation rl(String s) {
		return new ResourceLocation(Reference.MID, s);
	}
}
