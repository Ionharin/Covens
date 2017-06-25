package zabi.minecraft.covens.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityChimney;

public class GuiChimney extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/gui/chimney.png");
	
	private TileEntityChimney te;

	public GuiChimney(Container inventorySlotsIn, TileEntityChimney te) {
		super(inventorySlotsIn);
		this.te=te;
		this.xSize=175;
		this.ySize=165;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te!=null) {
			if (te.getStackInSlot(0).isEmpty()) {
				drawTexturedModalRect(guiLeft+51, guiTop+34, 176, 0, 16, 16);
			}
			String name = te.getName();
			int left = (xSize-fontRenderer.getStringWidth(name))/2;
			fontRenderer.drawString(name, this.guiLeft+left, this.guiTop+15, 3216909);
		}
	}

}
