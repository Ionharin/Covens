package zabi.minecraft.covens.client.jei.categories;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;

public class BrewingAdapter {
	
	private static ArrayList<BrewingAdapter> recipes = new ArrayList<BrewingAdapter>();
	
	private BrewIngredient ingr;
	private boolean positive;
	
	private BrewingAdapter(BrewIngredient recipe, boolean getPositive) {
		this.ingr = recipe;
		this.positive = getPositive;
	}
	
	public ItemStack[] getInput() {
		return ingr.getInput();
	}
	
	public Potion getOutput() {
		return positive?ingr.getResult():ingr.getOpposite();
	}
	
	public int getDuration() {
		return positive?ingr.getDuration():ingr.getDurationOpposite();
	}
	
	public static void add(BrewIngredient ingredient) {
		recipes.add(new BrewingAdapter(ingredient, true));
		if (ingredient.getOpposite()!=null) recipes.add(new BrewingAdapter(ingredient, false));
	}
	
	public boolean isPositive() {
		return positive;
	}
	
	public static ArrayList<BrewingAdapter> addAll(Collection<BrewIngredient> ingredients) {
		recipes.clear();
		ingredients.forEach(i -> {
			add(i);
		});
		return recipes;
	}
	
}
