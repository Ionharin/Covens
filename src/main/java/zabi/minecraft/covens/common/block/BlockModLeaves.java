package zabi.minecraft.covens.common.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Reference;

public class BlockModLeaves extends BlockLeaves {

	public BlockModLeaves(String name) {
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
		this.setUnlocalizedName("leaves_"+name);
		this.setRegistryName(Reference.MID, "leaves_"+name);
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this));
	}

	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}

	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
		if (this.getBlockState().getBlock()==ModBlocks.leaves_elder && worldIn.rand.nextInt(chance) == 0) {
			spawnAsEntity(worldIn, pos, new ItemStack(Items.APPLE));//TODO
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DECAYABLE, ((meta)&1)==1).withProperty(CHECK_DECAY, ((meta)&2)>0);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta += (state.getValue(DECAYABLE)?1:0);
		meta += (state.getValue(CHECK_DECAY)?2:0);
		return meta;
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		if (!worldIn.isRemote && stack.getItem() instanceof ItemShears) {
			player.addStat(StatList.getBlockStats(this));
			spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
		} else {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.sapling;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		if (this==ModBlocks.leaves_elder) return 0;
		if (this==ModBlocks.leaves_juniper) return 1;
		if (this==ModBlocks.leaves_yew) return 2;
		return 3; //give a useless item
	}
	
	@Override
	protected int getSaplingDropChance(IBlockState state) {
		if (state.getBlock()==ModBlocks.leaves_juniper) return super.getSaplingDropChance(state)*3;
		return super.getSaplingDropChance(state);
	}
	
	public boolean isOpaqueCube(IBlockState state) {
		this.leavesFancy = Covens.proxy.isFancyGraphicsEnabled();
        return !this.leavesFancy;
    }
	
}
