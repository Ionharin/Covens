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
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class BrewingWrapper implements IRecipeWrapper {
	
	static ItemStack eye = new ItemStack(Items.FERMENTED_SPIDER_EYE);
	ItemStack input;
	BrewData out, out_inv;
	
	public BrewingWrapper(BrewIngredient recipe) {
		input = recipe.getInput();
		out = new BrewData(0);
		out.addEffectToBrew(new CovenPotionEffect(recipe.getResult(), recipe.getDuration(), 0));
		if (recipe.getOpposite()!=null) {
			out_inv = new BrewData(0);
			out_inv.addEffectToBrew(new CovenPotionEffect(recipe.getOpposite(), recipe.getDurationOpposite(), 0));
		} else {
			out_inv = new BrewData(0);
			out_inv.addEffectToBrew(new CovenPotionEffect(recipe.getResult(), recipe.getDuration(), 0));
			out_inv.spoil();
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Arrays.asList(input, eye));
		ingredients.setOutputLists(ItemStack.class, Arrays.asList(getListFor(out), getListFor(out_inv)));
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
