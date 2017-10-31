package zabi.minecraft.covens.common.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.inventory.GuiHandler;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;

public class BlockBarrel extends BlockHorizontal implements ITileEntityProvider {
	
	public static final PropertyWood WOOD_TYPE = new PropertyWood("wood", WoodType.class, Arrays.asList(WoodType.values()));
	
	private static final AxisAlignedBB bounding_box_NS = new AxisAlignedBB(0.1875D, 0.0D, 0.03125D, 0.8125D, 0.625D, 0.96875D);
	private static final AxisAlignedBB bounding_box_WE = new AxisAlignedBB(0.03125D, 0.0D, 0.1875D, 0.96875D, 0.625D, 0.8125D);

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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing f = state.getValue(FACING);
		if (f == EnumFacing.EAST || f == EnumFacing.WEST) return bounding_box_WE;
		return bounding_box_NS;
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) return true;
		TileEntityBarrel barrel = (TileEntityBarrel) world.getTileEntity(pos);
//		if (barrel.hasRecipe()) {
//			return false; //Don't do anything if a recipe is cooking or another one is ready
//		}
		world.notifyBlockUpdate(pos, state, state, 3);
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandlerItem itemHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			IFluidHandler barrelHandler = barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidStack fluidInItem = itemHandler.drain(Fluid.BUCKET_VOLUME, false);
			FluidStack fluidInBarrel = barrelHandler.drain(Fluid.BUCKET_VOLUME, false);
			if ((fluidInBarrel!=null && fluidInBarrel.amount>0) && (fluidInItem==null || fluidInItem.amount==0 || (fluidInItem.isFluidEqual(fluidInBarrel) && fluidInItem.amount<Fluid.BUCKET_VOLUME))) {
				itemHandler.fill(barrelHandler.drain(Fluid.BUCKET_VOLUME, true), true);
				player.setHeldItem(hand, itemHandler.getContainer());
			} else if (fluidInItem!=null && fluidInItem.amount>0 && fluidInItem.getFluid()!=null && (fluidInBarrel==null || fluidInBarrel.amount==0 || (fluidInBarrel.amount<Fluid.BUCKET_VOLUME && fluidInBarrel.isFluidEqual(fluidInItem)))) {
				barrelHandler.fill(itemHandler.drain(Fluid.BUCKET_VOLUME, true), true);
				player.setHeldItem(hand, itemHandler.getContainer());
			}
			return true;
		} 
		
//		if (stack.isEmpty()) {
//			IFluidHandler barrelHandler = barrel.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
//			FluidStack cont = barrelHandler.drain(Fluid.BUCKET_VOLUME, false);
//			TextComponentTranslation message = null;
//			if (cont==null || cont.amount==0 || cont.getFluid()==null) message = new TextComponentTranslation("tile.barrel.empty");
//			else message = new TextComponentTranslation("tile.barrel.full", cont.getLocalizedName(), cont.amount, Fluid.BUCKET_VOLUME);
//			player.sendStatusMessage(message, true);
//			return true;
//		}
		player.openGui(Covens.INSTANCE, GuiHandler.IDs.BARREL.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (((TileEntityBarrel)world.getTileEntity(pos)).hasRecipe()) {
			world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, pos.getX(), pos.getY()+1, pos.getZ(), 0, 0, 0);
		}
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
