package zabi.minecraft.covens.common.block;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;

public class BlockSilverVat extends BlockHorizontal {

	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.6875, 0.875);
	private static final PropertyBool HANGING = PropertyBool.create("hanging");
	private static final PropertyBool HANGING_EXT = PropertyBool.create("extended");
	
	public BlockSilverVat() {
		super(Material.ROCK);
		this.setRegistryName(Reference.MID, "silver_vat");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setUnlocalizedName("silver_vat");
		this.setDefaultState(blockState.getBaseState()
				.withProperty(FACING, EnumFacing.SOUTH)
				.withProperty(HANGING, false)
				.withProperty(HANGING_EXT, false));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HANGING, HANGING_EXT);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState()
				.withProperty(FACING, placer.getHorizontalFacing())
				.withProperty(HANGING, !world.isAirBlock(pos.up()) && world.getBlockState(pos.up()).getBlockFaceShape(world, pos.up(), EnumFacing.DOWN)==BlockFaceShape.SOLID)
				.withProperty(HANGING_EXT, world.isAirBlock(pos.up()) && !world.isAirBlock(pos.up(2)) && world.getBlockState(pos.up(2)).getBlockFaceShape(world, pos.up(2), EnumFacing.DOWN)==BlockFaceShape.SOLID);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(HANGING, !worldIn.isAirBlock(pos.up()) && worldIn.getBlockState(pos.up()).getBlockFaceShape(worldIn, pos.up(), EnumFacing.DOWN)==BlockFaceShape.SOLID)
				.withProperty(HANGING_EXT, worldIn.isAirBlock(pos.up()) && !worldIn.isAirBlock(pos.up(2)) && worldIn.getBlockState(pos.up(2)).getBlockFaceShape(worldIn, pos.up(2), EnumFacing.DOWN)==BlockFaceShape.SOLID);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (fromPos.equals(pos.up())) worldIn.setBlockState(pos, getActualState(state, worldIn, pos), 2);
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
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

}
