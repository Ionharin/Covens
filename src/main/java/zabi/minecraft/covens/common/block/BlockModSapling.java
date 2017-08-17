package zabi.minecraft.covens.common.block;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;

public class BlockModSapling extends BlockBush implements IGrowable {
	
	public BlockModSapling() {
		this.setRegistryName(Reference.MID, "witch_sapling");
		this.setUnlocalizedName("sapling");
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		if (this.getCreativeTabToDisplayOn()==itemIn) {
			items.add(new ItemStack(this,1,0));
			items.add(new ItemStack(this,1,1));
			items.add(new ItemStack(this,1,2));
		}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

	}

}
