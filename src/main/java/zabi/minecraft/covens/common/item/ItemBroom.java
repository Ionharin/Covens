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
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemBroom extends Item {

	public ItemBroom() {
		this.setRegistryName(Reference.MID, "broom");
		this.setUnlocalizedName("broom");
		this.setHasSubtypes(true);
		this.setCreativeTab(ModCreativeTabs.products);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			items.add(new ItemStack(this,1,0)); //Mundane woods
			items.add(new ItemStack(this,1,1)); //Elder
			items.add(new ItemStack(this,1,2)); //Juniper
			items.add(new ItemStack(this,1,3)); //Yew
		}
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.getBlockState(pos).getBlock().equals(ModBlocks.glyphs)) {
			world.setBlockToAir(pos);
		} else {
			spawnBroom(world, pos, side);
			player.getHeldItem(hand).setCount(0);
		}
		return EnumActionResult.SUCCESS;
	}

	private void spawnBroom(World world, BlockPos pos, EnumFacing side) {
		
	}
	
}
