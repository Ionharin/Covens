package zabi.minecraft.covens.client.gui.books;

import java.util.HashMap;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

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

	private static final ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/blocks/altar_formed_side_blue.png");
	
	protected static void initBooks() {
		Log.i("Loading books");
		
		Book testBook = new Book("test book");
		testBook.addPage(new PageImage("image test", "test chapter", texture, new TextComponentString("test dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), new int[] {0,0,0,0,16,16,256,256,16,16}));
		testBook.addPages(PageDescription.createPages("test pages", "test chapter", new TextComponentString("Double, double toil and trouble; Fire burn, and caldron bubble. Fillet of a fenny snake, In the caldron boil and bake; Eye of newt, and toe of frog, Wool of bat, and tongue of dog, Adder's fork, and blind-worm's sting, Lizard's leg, and owlet's wing")));
		testBook.addPage(new PageVanillaRecipe("test recipe", "test chapter 2", CraftingManager.REGISTRY.getObject(new ResourceLocation("stone_sword")), new TextComponentString("")));
		testBook.addPage(new PageVanillaRecipe("test recipe", "test chapter 2", CraftingManager.REGISTRY.getObject(new ResourceLocation("dark_oak_door")), new TextComponentString("")));
		testBook.addPage(new PageVanillaRecipe("test recipe", "test chapter 2", CraftingManager.REGISTRY.getObject(new ResourceLocation("shears")), new TextComponentString("")));
		testBook.addPage(new PageVanillaRecipe("test recipe", "test chapter 2", CraftingManager.REGISTRY.getObject(new ResourceLocation("minecart")), new TextComponentString("")));
		testBook.addPage(new PageVanillaRecipe("test recipe", "test chapter 2", CraftingManager.REGISTRY.getObject(new ResourceLocation("lead")), new TextComponentString("")));
		
		
		books.put("testBook", testBook);
	}
	
}
