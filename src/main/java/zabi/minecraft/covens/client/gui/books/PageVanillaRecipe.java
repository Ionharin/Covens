package zabi.minecraft.covens.client.gui.books;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import zabi.minecraft.covens.client.gui.GuiBook;

public class PageVanillaRecipe extends PageImage {

	public static final ResourceLocation grid = new ResourceLocation("covens", "textures/gui/book/grid.png");
	
	IRecipe recipe = null;
	
	public PageVanillaRecipe(String title, String chapter, IRecipe recipe, ITextComponent text) {
		super(title, chapter, grid, text, new int[] {0, 0, 0, 0, 54, 116, 54, 116, 54, 116});
		if (!recipe.canFit(3, 3)) Log.w("Recipe "+recipe.getRegistryName().toString()+" cannot fit in a 3x3 grid and it's not goona work well");
		this.recipe = recipe;
	}

	@Override
	public void render(GuiBook gui, int mouseX, int mouseY) {
		super.render(gui, mouseX, mouseY);
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int sx = (sr.getScaledWidth() - 52)/2;
		int sy = ((sr.getScaledHeight() - pageHeight + 2) / 2) + topSpacing;
		ItemStack hovered = null;
		for (int index=0;index<recipe.getIngredients().size();index++) {
			Ingredient in = recipe.getIngredients().get(index);
			int minX = sx + (18*(index/3));
			int minY = sy + (18*(index%3));
			ItemStack stack = in.equals(Ingredient.EMPTY)?ItemStack.EMPTY:in.getMatchingStacks()[0];
			RenderHelper.enableGUIStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, minX, minY);
			if (!stack.isEmpty()) {
				if (mouseX>minX && mouseX<minX+18) {
					if (mouseY>minY && mouseY<minY+18) {
						hovered = stack;
					}
				}
			}
		}
		RenderHelper.enableGUIStandardItemLighting();
		ItemStack out = recipe.getRecipeOutput();
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(out, sx+18, sy+77);
		if (mouseX>sx+18 && mouseX<sx+36) {
			if (mouseY>sy+76 && mouseY<sy+94) {
				hovered = out;
			}
		}
		if (hovered!=null) {
			gui.drawHoveringText(hovered.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL), mouseX, mouseY);
		}
	}
}