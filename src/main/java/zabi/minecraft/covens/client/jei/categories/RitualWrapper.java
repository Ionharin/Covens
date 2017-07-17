package zabi.minecraft.covens.client.jei.categories;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class RitualWrapper implements IRecipeWrapper {
	
	List<ItemStack> input, output;
	private int circles, powerStart, powerTick;
	
	private static IDrawable centerGlyph, circle1, circle2, circle3;
	
	public RitualWrapper(Ritual ritual, IGuiHelper igh) {
		Log.i("wrapping "+ritual.getRegistryName().toString());
		output = ritual.getOutput();
		input = ritual.getInput();
		circles = ritual.getCircles();
		powerStart = ritual.getRequiredStartingPower();
		powerTick = ritual.getRunningPower();
		if (centerGlyph==null) {
			centerGlyph = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_ritual_0.png"), 0, 0, 34, 34, 34, 34);
			circle1 = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_ritual_1.png"), 0, 0, 34, 34, 34, 34);
			circle2 = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_ritual_2.png"), 0, 0, 34, 34, 34, 34);
			circle3 = igh.createDrawable(new ResourceLocation(Reference.MID, "textures/gui/jei_ritual_3.png"), 0, 0, 34, 34, 34, 34);
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, input);
		if (!output.isEmpty()) ingredients.setOutputs(ItemStack.class, output);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer fr = minecraft.fontRenderer;
		String powerFlatDesc = I18n.format("jei.ritual.power.flat", powerStart);
		String powerTickDesc = I18n.format("jei.ritual.power.tick", powerTick*20);
		int mult = (int) (powerTick>0?3.1:2);
		if (powerStart>0) fr.drawString(powerFlatDesc, (recipeWidth-fr.getStringWidth(powerFlatDesc))/2, recipeHeight-mult*fr.FONT_HEIGHT, 0, false);
		if (powerTick>0) fr.drawString(powerTickDesc, (recipeWidth-fr.getStringWidth(powerTickDesc))/2, recipeHeight-2*fr.FONT_HEIGHT, 0, false);
		
		int requiredCircles = circles & 3;
		GlyphType typeFirst = BlockCircleGlyph.GlyphType.values()[circles>>2 & 3];
		GlyphType typeSecond = BlockCircleGlyph.GlyphType.values()[circles>>4 & 3];
		GlyphType typeThird = BlockCircleGlyph.GlyphType.values()[circles>>6 & 3];
		color(GlyphType.GOLDEN);
		
		int dx = 53, dy = 25;
		
		centerGlyph.draw(minecraft, dx, dy);
		color(typeFirst);
		circle1.draw(minecraft, dx, dy);
		if (requiredCircles>0) {
			color(typeSecond);
			circle2.draw(minecraft, dx, dy);
		}
		if (requiredCircles>1) {
			color(typeThird);
			circle3.draw(minecraft, dx, dy);
		}
	}
	
	private void color(GlyphType gt) {
		switch (gt) {
		case ENDER:
			GlStateManager.color(0.5f, 0f, 0.5f);
			break;
		case GOLDEN: 
			GlStateManager.color(1f, 1f, 0f);
			break;
		case NETHER:
			GlStateManager.color(0.8f, 0f, 0f);
			break;
		case NORMAL:
			GlStateManager.color(0.9f, 0.9f, 0.9f);
		}
	}
	
	
}
