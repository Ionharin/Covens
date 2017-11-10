package zabi.minecraft.covens.common.block;

import java.util.Random;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.tileentity.TileEntityRitualCandle;

public class BlockRitualCandle extends Block implements ITileEntityProvider {
	
	public static final PropertyBool lit = PropertyBool.create("lit");
	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(7D/16D, 0, 7D/16D, 9D/16D, 7D/16D, 9D/16D);

	public BlockRitualCandle() {
		super(Material.SPONGE);
		this.setUnlocalizedName("ritual_candle");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setRegistryName(Reference.MID, "ritual_candle");
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(lit, false));
	}

	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(lit)?5:0;
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntityRitualCandle te = (TileEntityRitualCandle) worldIn.getTileEntity(pos);
		if (te!=null && te.isLit()) {
			zabi.minecraft.covens.client.particle.ParticleSmallFlame flame = new zabi.minecraft.covens.client.particle.ParticleSmallFlame(worldIn, pos.getX()+0.5D+rand.nextGaussian()*0.005, pos.getY()+0.54, pos.getZ()+rand.nextGaussian()*0.005+0.5D, 0, 0, 0, 0.06f);
			Covens.proxy.spawnParticle(flame);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ritual_data")) ((TileEntityRitualCandle) worldIn.getTileEntity(pos)).setFromStack(stack);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean flag = false;
		TileEntityRitualCandle te = (TileEntityRitualCandle) worldIn.getTileEntity(pos);
		if (playerIn.getHeldItem(hand).getItem()==Items.FLINT_AND_STEEL) {
			flag = true;
		} else if (playerIn.getHeldItem(hand).getItem()==Items.FIRE_CHARGE) {
			flag=true;
			playerIn.getHeldItem(hand).shrink(1);
		}
		if (flag) {
			te.setLit(true);
			worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(lit, true));
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (state.getValue(lit)) return;
		super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRitualCandle();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, lit);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(lit)?1:0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(lit, meta==1);
	}
}
