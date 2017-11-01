package zabi.minecraft.covens.common.block;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.inventory.GuiHandler;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;

public class BlockThreadSpinner extends BlockHorizontal implements ITileEntityProvider {

	public BlockThreadSpinner() {
		super(Material.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
		this.setRegistryName(Reference.MID, "thread_spinner");
		this.setUnlocalizedName("thread_spinner");
		this.setCreativeTab(ModCreativeTabs.machines);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityThreadSpinner();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Covens.INSTANCE, GuiHandler.IDs.THREAD_SPINNER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		ItemStack stack = super.getPickBlock(state, target, world, pos, player);
		if (player.isSneaking()) {
			stack.setTagCompound(world.getTileEntity(pos).writeToNBT(new NBTTagCompound()));
		}
		return stack;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta]);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
