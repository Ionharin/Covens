package zabi.minecraft.covens.common.block;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.tileentity.TileEntityCrystalBall;
import zabi.minecraft.covens.common.tileentity.TileEntityCrystalBall.EnumCrystalBallResult;

public class BlockCrystalBall extends Block implements ITileEntityProvider {

	public BlockCrystalBall() {
		super(Material.GLASS);
		this.setUnlocalizedName("crystal_ball");
		this.setRegistryName(Reference.MID, "crystal_ball");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setLightOpacity(0);
		this.setLightLevel(0.3f);
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCrystalBall();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand == EnumHand.OFF_HAND) return false;
		if (worldIn.isRemote) return true;
		TileEntityCrystalBall te = (TileEntityCrystalBall) worldIn.getTileEntity(pos);
		EnumCrystalBallResult result = te.tryAddItem(playerIn.getHeldItemMainhand());
		if (result == EnumCrystalBallResult.BLOCK) return false;
		if (result == EnumCrystalBallResult.FORTUNE) return te.fortune(playerIn);
		if (result == EnumCrystalBallResult.INSERT_ITEM) {
			playerIn.getHeldItemMainhand().setCount(playerIn.getHeldItemMainhand().getCount()-1);
			return true;
		}
		if (result == EnumCrystalBallResult.SPECTATE) return te.spectate(playerIn);
		return false;
	}
	
}
