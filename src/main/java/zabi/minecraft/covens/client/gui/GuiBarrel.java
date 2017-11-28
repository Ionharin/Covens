package zabi.minecraft.covens.client.gui;

import java.util.ArrayList;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zabi.minecraft.covens.common.inventory.ContainerBarrel;
import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;

public class GuiBarrel extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/gui/barrel.png");
	
	private TileEntityBarrel te;

	public GuiBarrel(Container inventorySlotsIn, TileEntityBarrel te) {
		super(inventorySlotsIn);
		this.te=te;
		this.xSize=176;
		this.ySize=166;
		te.getAltar(true);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		if (te!=null) {
			FluidStack fstack = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			drawTexturedModalRect(guiLeft+62, guiTop+17, 176, 32, 52, 16);
			if (fstack==null) {
				drawTexturedModalRect(guiLeft+80, guiTop+17, xSize, 0, 16, 16);
			} else {
				TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
				TextureAtlasSprite sprite = map.getTextureExtry(fstack.getFluid().getStill().toString());
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				for (int i=0;i<4;i++) drawTexturedModalRect(guiLeft+62+(16*i), guiTop+17, sprite, 16, 16);
			}
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te!=null) {

			int[] data = ((ContainerBarrel)inventorySlots).data_a;
			if (data[2]>0) {
				double progress = (double) data[0] / (double) data[2];
				if (progress>1) progress=1;
				int dy = (int)Math.ceil(31*progress);
				drawTexturedModalRect(guiLeft+29, guiTop + 27 + 31 - dy, 0, ySize + 31 - dy , 11, dy);
			}
			float absorption = 1;
			if (data[3]>0) absorption = (float) data[1] / (float) data[3];
			GlStateManager.pushMatrix();
			{
				GlStateManager.color(2*(1f - absorption), 2*absorption, 0f);
				drawTexturedModalRect(guiLeft+134, guiTop + 20, xSize, 16, 16, 16);
				GlStateManager.color(1f, 1f, 1f);
			}
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int[] data = ((ContainerBarrel)inventorySlots).data_a;
		this.renderHoveredToolTip(mouseX-guiLeft, mouseY-guiTop);
		if (mouseX>guiLeft+62 && mouseX<guiLeft+114 && mouseY>guiTop+17 && mouseY<guiTop+33) {
			FluidStack fstack = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
			if (fstack!=null) {
				ArrayList<String> tooltip = new ArrayList<String>(2);
				String color = fstack.getFluid().getTemperature()>600?TextFormatting.GOLD.toString():fstack.getFluid().getTemperature()<150?TextFormatting.AQUA.toString():TextFormatting.DARK_AQUA.toString();
				tooltip.add(color+fstack.getLocalizedName());
				tooltip.add(TextFormatting.GRAY+I18n.format("fluid.quantity.mB", fstack.amount));
				drawHoveringText(tooltip, mouseX-guiLeft, mouseY-guiTop);
			} else {
				drawHoveringText(I18n.format("fluid.none"), mouseX-guiLeft, mouseY-guiTop);
			}
		} else if (mouseX>guiLeft+134 && mouseX<guiLeft+150 && mouseY>guiTop+20 && mouseY<guiTop+36) {
			String text = TextFormatting.YELLOW+I18n.format("tile.barrel.altar.working");
			if (data[3]==0) text = TextFormatting.GREEN+I18n.format("tile.barrel.altar.none_required");
			else if (te.getAltar(false) == null) text = TextFormatting.DARK_RED+I18n.format("tile.barrel.altar.no_altar");
			else if (data[3]==data[1]) text = TextFormatting.GREEN+I18n.format("tile.barrel.altar.done");
			
			drawHoveringText(text, mouseX-guiLeft, mouseY-guiTop);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (te.isInvalid()) mc.player.closeScreen();
	}
	
}
