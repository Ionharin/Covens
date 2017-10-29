package zabi.minecraft.covens.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.client.gui.books.Book;
import zabi.minecraft.covens.client.gui.books.BookManager;

public class GuiBook extends GuiScreen {
	
	private static final ResourceLocation bookBG = new ResourceLocation("textures/gui/book.png");
	private static final int bgW = 146;
	private static final int bgH = 180;

	private Book book;
	private int currentPage = 0;
	
	public GuiBook(String bookName) {
		this(bookName,0);
	}
	
	public GuiBook(String bookName, int page) {
		book = BookManager.books.get(bookName);
		this.currentPage = page;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawBackground(0);
		if (book.getPages().size()<=currentPage) currentPage = 0;
		book.getPages().get(currentPage).render(this, mouseX, mouseY);
	}
	
	@Override
	public void drawBackground(int tint) {
//		super.drawBackground(tint);
		Minecraft.getMinecraft().renderEngine.bindTexture(bookBG);
		ScaledResolution sr = new ScaledResolution(mc);
		int x =  (sr.getScaledWidth() - bgW)/2;
		int y =  (sr.getScaledHeight() - bgH)/2;
		this.drawTexturedModalRect(x, y, 20, 1, bgW, bgH);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		currentPage++;
	}
	
}
