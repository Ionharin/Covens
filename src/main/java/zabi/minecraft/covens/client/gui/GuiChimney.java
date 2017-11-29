package zabi.minecraft.covens.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
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
		this.xSize=176;
		this.ySize=166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te!=null) {
			if (te.getInventory().getStackInSlot(0).isEmpty()) {
				drawTexturedModalRect(guiLeft+51, guiTop+34, 176, 0, 16, 16);
			}
			String name = I18n.format(te.getBlockType().getUnlocalizedName()+".name");
			int left = (xSize-fontRenderer.getStringWidth(name))/2;
			fontRenderer.drawString(name, this.guiLeft+left, this.guiTop+15, 3216909);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.renderHoveredToolTip(mouseX-guiLeft, mouseY-guiTop);
	}

}
