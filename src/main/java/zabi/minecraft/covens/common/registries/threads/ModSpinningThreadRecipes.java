package zabi.minecraft.covens.common.registries.threads;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.item.ModItems;

public class ModSpinningThreadRecipes {

	public static SpinningThreadRecipe web, soulstring;
	
	public static void registerAll() {
		Log.i("Creating spinning thread recipes");
		MinecraftForge.EVENT_BUS.register(new ModSpinningThreadRecipes());
		
		Ingredient string = Ingredient.fromItem(Items.STRING);
		
		web = new SpinningThreadRecipe(Reference.MID, "spider_web", new ItemStack(Blocks.WEB), string, string, string);
		soulstring = new SpinningThreadRecipe(Reference.MID, "soulstring", new ItemStack(ModItems.soulstring), string, string, Ingredient.fromStacks(new ItemStack(ModItems.misc, 1, 5)), Ingredient.fromStacks(new ItemStack(ModItems.flowers, 1, 1)));
	}
	
	@SubscribeEvent
	public void registerSpinningThreadRecipes(RegistryEvent.Register<SpinningThreadRecipe> evt) {
		Log.i("Registering spinning thread recipes");
		evt.getRegistry().registerAll(web, soulstring);
	}
}
