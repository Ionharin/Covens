package zabi.minecraft.covens.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import zabi.minecraft.covens.common.crafting.brewing.BrewData;
import zabi.minecraft.covens.common.item.ItemBrewDrinkable;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityCauldron;

public class BlockCauldron extends Block implements ITileEntityProvider {
	
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
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 0;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCauldron();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;//state.getValue(FULL);
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
			BrewData data = cauldron.getResult();
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
						ItemStack result = ItemBrewDrinkable.getBrewStackWithData(data.getType(), data);
						if (data.getEffects().isEmpty()) {
							result = new ItemStack(data.getType(),1,1);
						} 
						worldIn.spawnEntity(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, result));
						return true;
					}
				} else {
					playerIn.sendStatusMessage(new TextComponentTranslation("brew.success.power", cost), true);
					return false;
				}
				
			} else return false;
			
		} else if (worldIn.isRemote && !state.getValue(FULL)) {
			if (isBucket(playerIn.getHeldItem(hand))) {
				if (!playerIn.isCreative()) playerIn.setHeldItem(hand, emptyBucket(playerIn.getHeldItem(hand)));
				worldIn.setBlockState(pos, state.withProperty(FULL, true),3);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
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
}
