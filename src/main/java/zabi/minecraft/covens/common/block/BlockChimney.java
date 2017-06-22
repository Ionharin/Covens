package zabi.minecraft.covens.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;

public class BlockChimney extends Block implements ITileEntityProvider {

	public BlockChimney() {
		super(Material.ROCK);
		this.setHardness(2);
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setUnlocalizedName("chimney");
		GameRegistry.register(this, new ResourceLocation(Reference.MID, "chimney"));
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		Block below = worldIn.getBlockState(pos.down()).getBlock();
		return below.equals(Blocks.FURNACE) || below.equals(Blocks.LIT_FURNACE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
		if (worldIn.getBlockState(pos.down()).getBlock().equals(Blocks.LIT_FURNACE)) worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+0.4+rand.nextDouble()/5, pos.getY()+0.9, pos.getZ()+0.4+rand.nextDouble()/5, 0, 0.1, 0, new int[0]);
	}
	
}
