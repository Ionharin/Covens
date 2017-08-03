package zabi.minecraft.covens.common.block;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;

public class BlockBarrel extends BlockHorizontal implements ITileEntityProvider {
	
	public static final PropertyWood WOOD_TYPE = new PropertyWood("wood", WoodType.class, Arrays.asList(WoodType.values()));

	protected BlockBarrel() {
		super(Material.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH).withProperty(WOOD_TYPE, WoodType.OAK));
		this.setRegistryName(Reference.MID, "barrel");
		this.setUnlocalizedName("barrel");
		this.setHarvestLevel("axe", 0);
		this.setHardness(2.0f);
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setLightOpacity(0);
	}
	
	@Override
	public String getUnlocalizedName() {
		return super.getUnlocalizedName();
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		if (getCreativeTabToDisplayOn()==itemIn || getCreativeTabToDisplayOn()==CreativeTabs.SEARCH) {
			for (int i = 0; i < WoodType.values().length; i++) items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta]);
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBarrel();
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
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(WOOD_TYPE, WoodType.values()[meta]);
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, WOOD_TYPE);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te==null) return state;
		return state.withProperty(WOOD_TYPE, ((TileEntityBarrel)worldIn.getTileEntity(pos)).getType());
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		((TileEntityBarrel)worldIn.getTileEntity(pos)).setType(stack.getMetadata());
		if (worldIn.getTileEntity(pos)==null) Log.e("Null TE");
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	//###########################################################################################################
	
		public static class PropertyWood extends PropertyEnum<WoodType> {
			protected PropertyWood(String name, Class<WoodType> valueClass, Collection<WoodType> allowedValues) {
				super(name, valueClass, allowedValues);
			}
		}
		
		public static enum WoodType implements IStringSerializable {
			OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, BIG_OAK, ELDER, JUNIPER, YEW;
			
			@Override
			public String getName() {
				return this.name().toLowerCase();
			}
			
		}

	
}
