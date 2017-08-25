package zabi.minecraft.covens.client.jei.categories;

import java.util.Arrays;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.lib.Reference;

public class BrewingCategory implements IRecipeCategory<BrewingWrapper> {
	
	public static IDrawable bg;
	
	public static final String UID = Reference.MID+":cauldron";
	
	public BrewingCategory(IGuiHelper igh) {
		ResourceLocation rl = new ResourceLocation(Reference.MID, "textures/gui/jei_brewing.png");
		bg = igh.createDrawable(rl, 0, 0, 91, 40, 91, 40);
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
	public void setRecipe(IRecipeLayout l, BrewingWrapper w, IIngredients ingredients) {
		IGuiItemStackGroup is = l.getItemStacks();
		is.init(0, true, w.positive?18:9, 0);
		is.set(0, Arrays.asList(w.input));
		is.init(1, false, 62, 19);
		is.set(1, ingredients.getOutputs(ItemStack.class).get(0));
		if (!w.positive) {
			is.init(2, true, 27, 0);
			is.set(2, BrewingWrapper.eye);
		}
	}
}
