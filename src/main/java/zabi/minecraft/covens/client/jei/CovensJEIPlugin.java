package zabi.minecraft.covens.client.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.client.jei.categories.BrewingAdapter;
import zabi.minecraft.covens.client.jei.categories.BrewingCategory;
import zabi.minecraft.covens.client.jei.categories.BrewingWrapper;
import zabi.minecraft.covens.client.jei.categories.ChimneyCategory;
import zabi.minecraft.covens.client.jei.categories.ChimneyWrapper;
import zabi.minecraft.covens.client.jei.categories.RitualCategory;
import zabi.minecraft.covens.client.jei.categories.RitualWrapper;
import zabi.minecraft.covens.client.jei.categories.SpinnerCategory;
import zabi.minecraft.covens.client.jei.categories.SpinnerWrapper;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;
import zabi.minecraft.covens.common.registries.chimney.ChimneyRecipe;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.registries.threads.SpinningThreadRecipe;

@JEIPlugin
public class CovensJEIPlugin implements IModPlugin {
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RitualCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new ChimneyCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BrewingCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new SpinnerCategory(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(Ritual.class, new RitualWrapperFactory(registry.getJeiHelpers().getGuiHelper()), RitualCategory.UID);
		registry.addRecipes(Ritual.REGISTRY.getValues(), RitualCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.chalk, 1, BlockCircleGlyph.GlyphType.GOLDEN.ordinal()), RitualCategory.UID);
		
		registry.handleRecipes(ChimneyRecipe.class, i -> new ChimneyWrapper(i), ChimneyCategory.UID);
		registry.addRecipes(ChimneyRecipe.REGISTRY.getValues(), ChimneyCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.chimney), ChimneyCategory.UID);
		
		registry.handleRecipes(BrewingAdapter.class, i -> new BrewingWrapper(i), BrewingCategory.UID);
		registry.addRecipes(BrewingAdapter.addAll(BrewIngredient.REGISTRY.getValues()), BrewingCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.cauldron), BrewingCategory.UID);
		
		registry.handleRecipes(SpinningThreadRecipe.class, i -> new SpinnerWrapper(i), SpinnerCategory.UID);
		registry.addRecipes(SpinningThreadRecipe.REGISTRY.getValues(), SpinnerCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.thread_spinner), SpinnerCategory.UID);
	}
	
	protected static class RitualWrapperFactory implements IRecipeWrapperFactory<Ritual> {
		
		private IGuiHelper igh;
		
		public RitualWrapperFactory(IGuiHelper igh) {
			this.igh=igh;
		}
		
		@Override
		public IRecipeWrapper getRecipeWrapper(Ritual recipe) {
			return new RitualWrapper(recipe,igh);
		}
	}
}
