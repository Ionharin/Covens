package zabi.minecraft.covens.client.jei.categories;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.lib.Reference;

public class ChimneyCategory implements IRecipeCategory<ChimneyWrapper> {
	
	private IDrawable bg;
	
	public static final String UID = Reference.MID+":chimney";
	
	public ChimneyCategory(IGuiHelper igh) {
		bg = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_chimney.png"), 0, 0, 48, 16, 48, 16);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("jei.category.chimney");
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
	public void setRecipe(IRecipeLayout recipeLayout, ChimneyWrapper recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, recipeWrapper.input);
		recipeLayout.getItemStacks().init(1, false, 36, 0);
		recipeLayout.getItemStacks().set(1, recipeWrapper.output);
	}
}
