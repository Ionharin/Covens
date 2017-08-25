package zabi.minecraft.covens.client.jei.categories;

import java.util.Arrays;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.registries.chimney.ChimneyRecipe;

public class ChimneyWrapper implements IRecipeWrapper {
	
	ItemStack[] input;
	ItemStack output;
	
	public ChimneyWrapper(ChimneyRecipe recipe) {
		input = recipe.getInput();
		output = recipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, Arrays.asList(Arrays.asList(input)));
		ingredients.setOutput(ItemStack.class, output);
	}
	
	
	
}
