package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.lib.Reference;

public class ModCreativeTabs extends CreativeTabs {
	
	private ItemStack icon;
	
	public ModCreativeTabs(String label) {
		super(Reference.MID+"."+label);
	}

	public static ModCreativeTabs herbs, rituals;
	
	public static void registerTabs() {
		herbs = new ModCreativeTabs("herbs");
		rituals = new ModCreativeTabs("rituals");
	}
	
	public static void registerIcons() {
		herbs.setTabIconItem(new ItemStack(ModItems.flowers));
		rituals.setTabIconItem(new ItemStack(ModItems.chalk,1,1));
	}

	@Override
	public ItemStack getTabIconItem() {
		return icon;
	}
	
	public void setTabIconItem(ItemStack stack) {
		icon = stack;
	}
	
}
