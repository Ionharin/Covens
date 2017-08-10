package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemCandle extends Item {
	
	public ItemCandle() {
		this.setRegistryName(Reference.MID, "candle");
		this.setUnlocalizedName("witch_candle");
		this.setCreativeTab(ModCreativeTabs.products);
		this.setHasSubtypes(true);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata()==1;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack)+(stack.getMetadata()==1?"_revealing":"");
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			items.add(new ItemStack(this));
			items.add(new ItemStack(this,1,1));
		}
	}
}
