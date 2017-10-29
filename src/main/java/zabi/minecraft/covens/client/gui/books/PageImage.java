package zabi.minecraft.covens.client.gui.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import zabi.minecraft.covens.client.gui.GuiBook;

public class PageImage extends Page {
	
	private ResourceLocation image;
	private ITextComponent text;
	private int[] data;

	public PageImage(String title, String chapter, ResourceLocation image, ITextComponent text, int[] data) {
		super(title, chapter);
		this.image = image;
		this.text = text;
		this.data = data;
	}

	@Override
	public void render(GuiBook gui, int mouseX, int mouseY) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int sx = (sr.getScaledWidth() - maxPixelsWidthPerLine)/2;
		int sy = ((sr.getScaledHeight() - pageHeight) / 2) + topSpacing;
		if (data.length==10) {
			Minecraft.getMinecraft().renderEngine.bindTexture(image);
			int w = data[6];
			int h = data[7];
			int imgTabulation = 10;
			int px = sx + imgTabulation;
			int maxImgWidth = maxPixelsWidthPerLine - (imgTabulation*2);
			if (w>maxImgWidth) {
				w = maxImgWidth;
				h = h * w / data[6];
			} else {
				px += (maxImgWidth-w)/2;
			}
			Gui.drawScaledCustomSizeModalRect(px+data[0], sy+data[1], data[2], data[3], data[4], data[5], w, h, data[8], data[9]);
			String content = TextFormatting.ITALIC+GuiUtilRenderComponents.splitText(text, maxPixelsWidthPerLine, fr, false, false).stream().findFirst().orElse(new TextComponentString("")).getFormattedText();
			fr.drawString(content, sx+(maxPixelsWidthPerLine-fr.getStringWidth(content))/2, sy+data[1]+h+1+fr.FONT_HEIGHT, 0, false);
		} else {
			fr.drawString("Incorrect data parameters, expected array size 8 (x,y,ux,uy,uw,uh,w,h,tw,th)", 0, 0, 0xaa0000, true);
		}
	}

}
