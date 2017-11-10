package zabi.minecraft.covens.client.gui.books;

import java.util.HashMap;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class BookManager {
	
	public static HashMap<String,Book> books = new HashMap<String,Book>(); 
	
	public static void registerPagesReloadListener() {
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(IResourceManager resourceManager) {
				initBooks();
			}
		});
	}

	
	protected static void initBooks() {
		Log.i("Loading books");
	}
	
}
