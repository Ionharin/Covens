package zabi.minecraft.covens.common.item;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemFlowers extends Item {
	
	public static final String[] names = new String[] {"aconitum", "hellebore", "sagebrush", "chrysanthemum"};
	
	public ItemFlowers() {
		this.setUnlocalizedName("flowers");
		this.setRegistryName(Reference.MID, "flowers");
		this.setHasSubtypes(true);
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.flower."+names[stack.getMetadata()];
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			for (int i=0;i<names.length;i++) {
				list.add(new ItemStack(this, 1, i));
			}
		}
	}
	
}
