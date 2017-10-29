package zabi.minecraft.covens.client.gui.books;

import java.util.ArrayList;
import java.util.List;

public class Book {

	private String title;
	private ArrayList<Page> pages = new ArrayList<Page>();
	
	public Book(String title) {
		this.setTitle(title);
	}

	public ArrayList<Page> getPages() {
		return pages;
	}
	
	public void addPage(Page page) {
		pages.add(page);
	}
	
	public void addPages(List<? extends Page> pages) {
		for (Page p:pages) addPage(p);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
