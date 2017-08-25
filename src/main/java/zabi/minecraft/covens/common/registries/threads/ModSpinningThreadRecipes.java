package zabi.minecraft.covens.common.registries.threads;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

public class ModSpinningThreadRecipes {

	public static SpinningThreadRecipe web;
	
	public static void registerAll() {
		Log.i("Creating spinning thread recipes");
		web = new SpinningThreadRecipe(Reference.MID, "spider_web", new ItemStack(Blocks.WEB), Ingredient.fromItem(Items.STRING), Ingredient.fromItem(Items.STRING), Ingredient.fromItem(Items.STRING));
	}
	
	@SubscribeEvent
	public static void registerSpinningThreadRecipes(RegistryEvent.Register<SpinningThreadRecipe> evt) {
		Log.i("Registering spinning thread recipes");
		evt.getRegistry().registerAll();
	}
}
