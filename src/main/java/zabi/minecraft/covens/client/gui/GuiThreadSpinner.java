package zabi.minecraft.covens.client.gui;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.inventory.ContainerThreadSpinner;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;

public class GuiThreadSpinner extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/gui/thread_spinner.png");
	
	public GuiThreadSpinner(Container inventorySlotsIn, TileEntityThreadSpinner te) {
		super(inventorySlotsIn);
		this.xSize=176;
		this.ySize=166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		ContainerThreadSpinner c = (ContainerThreadSpinner) this.inventorySlots;
		double progress = (double) c.data_a[0] / (double) (TileEntityThreadSpinner.MAX_TICKS - 10);
		drawTexturedModalRect(guiLeft+85, guiTop+33, 176, 0, (int)(22*progress), 17);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.renderHoveredToolTip(mouseX-guiLeft, mouseY-guiTop);
	}

}
