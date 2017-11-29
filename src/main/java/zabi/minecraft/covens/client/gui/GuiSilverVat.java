package zabi.minecraft.covens.client.gui;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.inventory.ContainerSilverVat;
import zabi.minecraft.covens.common.tileentity.TileEntitySilverVat;

public class GuiSilverVat extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/gui/silver_vat.png");
	
	private TileEntitySilverVat te;
	long bubbleTime;

	public GuiSilverVat(Container inventorySlotsIn, TileEntitySilverVat te) {
		super(inventorySlotsIn);
		this.te=te;
		this.xSize=176;
		this.ySize=166;
		bubbleTime = te.getWorld().getTotalWorldTime();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft+14, guiTop+41, 176, 0, te.getAcidLevel(), 4);
		if (isWorking()) {
			int progress = (int) ((bubbleTime - te.getWorld().getTotalWorldTime()) % 27);
			drawTexturedModalRect(
					guiLeft+82, guiTop+54+progress, 
					176, 31+progress, 
					11, 27);
		} else {
			bubbleTime = te.getWorld().getTotalWorldTime();
		}
	}
	
	private boolean isWorking() {
		
		ContainerSilverVat csv = (ContainerSilverVat) this.inventorySlots;
		
		return !csv.getSlot(2).getStack().isEmpty()
				&&
				csv.getSlot(0).getStack().getCount()<64
				&&
				csv.getSlot(1).getStack().getCount()<64
				&&
				csv.getSlot(3).getStack().getCount()<64
				&&
				csv.acid[0]>0;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.renderHoveredToolTip(mouseX-guiLeft, mouseY-guiTop);
	}

}
