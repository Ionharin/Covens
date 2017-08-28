package zabi.minecraft.covens.client.jei.categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import zabi.minecraft.covens.common.registries.threads.SpinningThreadRecipe;

public class SpinnerWrapper implements IRecipeWrapper {
	
	Ingredient[] input;
	ItemStack output;
	
	public SpinnerWrapper(SpinningThreadRecipe recipe) {
		input = recipe.getInputs();
		output = recipe.getResult();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<List<ItemStack>> list = new ArrayList<List<ItemStack>>();
		for (Ingredient i:input) list.add(Arrays.asList(i.getMatchingStacks()));
		ingredients.setInputLists(ItemStack.class, list);
		ingredients.setOutput(ItemStack.class, output);
	}
	
	
	
}
