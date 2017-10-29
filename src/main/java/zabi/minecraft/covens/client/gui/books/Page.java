package zabi.minecraft.covens.client.gui.books;

import zabi.minecraft.covens.client.gui.GuiBook;

public abstract class Page {

	protected static final int maxPixelsWidthPerLine = 118, linesPerPage = 16, pageHeight = 160, topSpacing = 10;
	
	private String chapter, title;
	
	public Page(String title, String chapter) {
		this.chapter = chapter;
		this.title = title;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public abstract void render(GuiBook gui, int mouseX, int mouseY);

}
