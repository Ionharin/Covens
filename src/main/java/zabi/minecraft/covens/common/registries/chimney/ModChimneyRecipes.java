package zabi.minecraft.covens.common.registries.chimney;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModChimneyRecipes {
	
	private static ChimneyRecipe cloudy_oil, oak_spirit, oak_spirit2, birch_soul, acacia_essence, spruce_heart;
	
	public static void registerAll() {
		Log.i("Creating chimney recipes");
		cloudy_oil = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,3)), new ItemStack(ModItems.misc, 1, 1));
		oak_spirit = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,0)), new ItemStack(ModItems.misc, 1, 2));
		oak_spirit2 = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,5)), new ItemStack(ModItems.misc, 1, 2));
		birch_soul = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,2)), new ItemStack(ModItems.misc, 1, 3));
		acacia_essence = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,4)), new ItemStack(ModItems.misc, 1, 4));
		spruce_heart = new ChimneyRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SAPLING,1,1)), new ItemStack(ModItems.misc, 1, 5));
		cloudy_oil.setRegistryName(Reference.MID, "cloudy_oil");
		oak_spirit.setRegistryName(Reference.MID, "oak_spirit");
		oak_spirit2.setRegistryName(Reference.MID, "oak_spirit2");
		birch_soul.setRegistryName(Reference.MID, "birch_soul");
		acacia_essence.setRegistryName(Reference.MID, "acacia_essence");
		spruce_heart.setRegistryName(Reference.MID, "spruce_heart");
	}
	
	@SubscribeEvent
	public static void registerChimneyRecipes(RegistryEvent.Register<ChimneyRecipe> evt) {
		Log.i("Registering chimney recipes");
		evt.getRegistry().registerAll(cloudy_oil, oak_spirit, oak_spirit2, birch_soul, acacia_essence, spruce_heart);
	}
}
