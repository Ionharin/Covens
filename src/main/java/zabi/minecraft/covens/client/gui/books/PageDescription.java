package zabi.minecraft.covens.client.gui.books;

import java.util.ArrayList;
import java.util.List;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.ITextComponent;
import zabi.minecraft.covens.client.gui.GuiBook;

public class PageDescription extends Page {
	
	private List<ITextComponent> lines;

	private PageDescription(String title, String chapter, List<ITextComponent> content) {
		super(title, chapter);
		lines=content;
		Log.d("Page:\n\n");
		for (ITextComponent tc:lines) {
			Log.d("line: "+tc.getFormattedText());
		}
	}

	@Override
	public void render(GuiBook gui, int mouseX, int mouseY) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int sx = ((sr.getScaledWidth() - maxPixelsWidthPerLine)/2) + 3;
		int sy = ((sr.getScaledHeight() - pageHeight) / 2) + topSpacing;
		int dy = 0;
		for (ITextComponent tc:lines) {
			fr.drawString(tc.getFormattedText(), sx, sy + dy, 0);
			dy+=fr.FONT_HEIGHT+1;
		}
	}
	
	public static List<PageDescription> createPages(String title, String chapter, ITextComponent content) {
		ArrayList<PageDescription> result = new ArrayList<PageDescription>();
		List<ITextComponent> lines = GuiUtilRenderComponents.splitText(content, maxPixelsWidthPerLine, Minecraft.getMinecraft().fontRenderer, true, true);
		while (!lines.isEmpty()) {
			result.add(new PageDescription(title, chapter, splice(lines)));
		}
		return result;
	}

	private static List<ITextComponent> splice(List<ITextComponent> lines) { //Can probably optimize with better algorithm
		List<ITextComponent> result = new ArrayList<ITextComponent>(15);
		while (!lines.isEmpty() && result.size()<linesPerPage) {
			result.add(lines.remove(0));
		}
		return result;
	}

}
