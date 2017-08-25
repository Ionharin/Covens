package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.Covens;
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
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem(hand).getMetadata()==1) {
			if (world.isRemote) {
				if (world.getTileEntity(pos) instanceof IAltarUser) {
					Covens.proxy.setupRenderHUD(world, pos);
				} else {
					Covens.proxy.setupRenderHUD(null, null);
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
