package zabi.minecraft.covens.common.block;

import java.util.Random;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.client.particle.ParticleSmallFlame;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.item.ModCreativeTabs;

public class BlockCandle extends Block {
	
	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(7D/16D, 0, 7D/16D, 9D/16D, 7D/16D, 9D/16D);

	public BlockCandle() {
		super(Material.SPONGE);
		this.setUnlocalizedName("ritual_candle");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setRegistryName(Reference.MID, "ritual_candle");
		this.setLightOpacity(0);
		this.setLightLevel(0.4f);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return bounding_box;
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
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		ParticleSmallFlame flame = new ParticleSmallFlame(worldIn, pos.getX()+0.5D+rand.nextGaussian()*0.005, pos.getY()+0.54, pos.getZ()+rand.nextGaussian()*0.005+0.5D, 0, 0, 0, 0.06f);
		Covens.proxy.spawnParticle(flame);
	}
}
