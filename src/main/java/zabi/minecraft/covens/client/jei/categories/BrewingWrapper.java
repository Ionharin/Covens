package zabi.minecraft.covens.client.jei.categories;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.item.ItemBrewBase;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class BrewingWrapper implements IRecipeWrapper {
	
	static ItemStack eye = new ItemStack(Items.FERMENTED_SPIDER_EYE);
	ItemStack input;
	BrewData out;
	boolean positive;
	
	public BrewingWrapper(BrewingAdapter recipe) {
		input = recipe.getInput();
		out = new BrewData(0);
		out.addEffectToBrew(new CovenPotionEffect(recipe.getOutput(), recipe.getDuration(), 0));
		positive = recipe.isPositive();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, positive?Arrays.asList(input):Arrays.asList(input, eye));
		ingredients.setOutputLists(ItemStack.class, Arrays.asList(getListFor(out)));
	}
	
	public static List<ItemStack> getListFor(BrewData data) {
		return Arrays.asList(
				ItemBrewBase.getBrewStackWithData(ModItems.brew_drinkable, data),
				ItemBrewBase.getBrewStackWithData(ModItems.brew_splash, data),
				ItemBrewBase.getBrewStackWithData(ModItems.brew_lingering, data),
				ItemBrewBase.getBrewStackWithData(ModItems.brew_gas, data)
			);
	}
}
