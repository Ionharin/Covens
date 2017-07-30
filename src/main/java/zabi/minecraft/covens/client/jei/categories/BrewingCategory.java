package zabi.minecraft.covens.client.jei.categories;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.lib.Reference;

public class BrewingCategory implements IRecipeCategory<BrewingWrapper> {
	
	private IDrawable bg;
	
	public static final String UID = Reference.MID+":cauldron";
	
	public BrewingCategory(IGuiHelper igh) {
		bg = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_brewing.png"), 0, 0, 51, 51, 51, 51);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("jei.category.brewing");
	}

	@Override
	public String getModName() {
		return Reference.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return bg;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BrewingWrapper recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 2, 2);
		recipeLayout.getItemStacks().set(0, recipeWrapper.input);
		List<List<ItemStack>> out = ingredients.getOutputs(ItemStack.class);
		recipeLayout.getItemStacks().init(1, false, 2, 31);
		recipeLayout.getItemStacks().set(1, out.get(0));
		recipeLayout.getItemStacks().init(2, false, 31, 31);
		recipeLayout.getItemStacks().set(2, out.get(1));
		recipeLayout.getItemStacks().init(3, true, 31, 2);
		recipeLayout.getItemStacks().set(3, BrewingWrapper.eye);
	}
}
