package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.inventory.GuiHandler;

import zabi.minecraft.covens.common.lib.Reference;

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
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(Covens.INSTANCE, GuiHandler.IDs.BOOK_TEST.ordinal(), worldIn, 0, 0, 0);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
