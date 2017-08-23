package zabi.minecraft.covens.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.tileentity.TileEntityCauldron;

public class BlockCauldron extends Block implements ITileEntityProvider {
	
	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(0, 0, 0, 1, 0.875, 1);
	private static final AxisAlignedBB wall_1 = new AxisAlignedBB(0.125, 0, 0.875, 1, 0.875, 1);
	private static final AxisAlignedBB wall_2 = new AxisAlignedBB(0, 0, 0.125, 0.125, 0.875, 1);
	private static final AxisAlignedBB wall_3 = new AxisAlignedBB(0.875, 0, 0, 1, 0.875, 0.875);
	private static final AxisAlignedBB wall_4 = new AxisAlignedBB(0, 0, 0, 0.875, 0.875, 0.125);
	private static final AxisAlignedBB floor = new AxisAlignedBB(0, 0, 0, 1, 0.125, 1);
	
	public static final PropertyBool FULL = PropertyBool.create("filled");

	public BlockCauldron() {
		super(Material.IRON);
		this.setUnlocalizedName("cauldron");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setRegistryName(Reference.MID, "cauldron");
		this.setDefaultState(blockState.getBaseState().withProperty(FULL, false));
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(1);
		this.setLightOpacity(0);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> list, Entity entityIn, boolean p_185477_7_) {
		addCollisionBoxToList(pos, entityBox, list, wall_1);
		addCollisionBoxToList(pos, entityBox, list, wall_2);
		addCollisionBoxToList(pos, entityBox, list, wall_3);
		addCollisionBoxToList(pos, entityBox, list, wall_4);
		addCollisionBoxToList(pos, entityBox, list, floor);
	}
	
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 0;
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCauldron();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FULL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FULL, meta==1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FULL)?1:0;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hand==EnumHand.MAIN_HAND && worldIn.getBlockState(pos.down()).getBlock().equals(Blocks.FIRE) && state.getValue(FULL)) {
			TileEntityCauldron cauldron = (TileEntityCauldron) worldIn.getTileEntity(pos);
			ItemStack result = cauldron.getResult();
			BrewData data = BrewData.getDataFromStack(result);
			int cost = data.getCost();
			boolean hasPower = cauldron.consumePower(cost, true);
			boolean hasItems = cauldron.getHasItems();
			if (hasItems) {
				if (playerIn.getHeldItem(hand).getItem().equals(Items.GLASS_BOTTLE)) {
					if (!hasPower) {
						playerIn.sendStatusMessage(new TextComponentTranslation("brew.failure.power", cost), true);
						return false;
					} else {
						if (!playerIn.isCreative()) playerIn.getHeldItem(hand).setCount(playerIn.getHeldItem(hand).getCount()-1);
						cauldron.emptyContents(); //TODO make it chance dependent on player brewing level instead of always true
						cauldron.consumePower(cost, false);
						worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, result));
						worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.8F, 1f, false);
						return true;
					}
				} else {
					playerIn.sendStatusMessage(new TextComponentTranslation("brew.success.power", cost), true);
					return false;
				}
				
			} else return false;
			
		} else if (!state.getValue(FULL)) {
			if (isBucket(playerIn.getHeldItem(hand))) {
				if (!worldIn.isRemote) {
					if (!playerIn.isCreative()) playerIn.setHeldItem(hand, emptyBucket(playerIn.getHeldItem(hand)));
					worldIn.setBlockState(pos, state.withProperty(FULL, true),3);
					worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.8F, 1f, false);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return true;
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
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	private boolean isBucket(ItemStack stack) {
		if (stack.isEmpty() || stack.getCount() != 1) return false;
		if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			FluidStack temp = handler.drain(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), false);
			if (temp != null && temp.getFluid() == FluidRegistry.WATER && temp.amount == Fluid.BUCKET_VOLUME) return true;
		}
		if (stack.getItem().equals(Items.WATER_BUCKET)) return true;
		return false;
	}
	
	private ItemStack emptyBucket(ItemStack stack) {
		if (stack.getItem().equals(Items.WATER_BUCKET)) return new ItemStack(Items.BUCKET);
		IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		handler.drain(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
		return handler.getContainer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (state.getValue(FULL) && ((TileEntityCauldron)world.getTileEntity(pos)).getHasItems()) {
			world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, pos.getX()+0.2+rand.nextDouble()*0.6, pos.getY()+0.88D, pos.getZ()+0.2+rand.nextDouble()*0.6, 0, 0.1, 0);
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		if (face.equals(EnumFacing.UP)) return BlockFaceShape.BOWL;
		return face==EnumFacing.DOWN?BlockFaceShape.UNDEFINED:BlockFaceShape.SOLID;
	}
}
