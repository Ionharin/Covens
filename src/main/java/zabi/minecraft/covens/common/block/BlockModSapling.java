package zabi.minecraft.covens.common.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.util.TreeUtils;

public class BlockModSapling extends BlockBush implements IGrowable {
	
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 3);
	public static final PropertySapling TYPE = new PropertySapling("type", EnumSaplingType.class, Arrays.asList(EnumSaplingType.values()));
	
	public BlockModSapling() {
		this.setRegistryName(Reference.MID, "sapling");
		this.setUnlocalizedName("sapling");
		this.setCreativeTab(ModCreativeTabs.herbs);
		this.setTickRandomly(true);
		this.setDefaultState(blockState.getBaseState().withProperty(STAGE, 0).withProperty(TYPE, EnumSaplingType.ELDER));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.sapling,1,0));
		items.add(new ItemStack(ModItems.sapling,1,1));
		items.add(new ItemStack(ModItems.sapling,1,2));
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}
	
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		if (TreeUtils.canSaplingGrow(state.getValue(TYPE), world, pos)) {
			if (state.getValue(STAGE)<3) world.setBlockState(pos, state.cycleProperty(STAGE), 3);
			else {
				generateTree(world, rand, pos, state);
			}
		} else {
			if (state.getValue(TYPE)==EnumSaplingType.YEW) {
				if (world.getBlockState(pos.south()).getBlock()==ModBlocks.sapling && world.getBlockState(pos.south()).getValue(TYPE)==EnumSaplingType.YEW) {
					((BlockModSapling)world.getBlockState(pos.south()).getBlock()).grow(world, rand, pos.south(), world.getBlockState(pos.south()));
					return;
				}
				if (world.getBlockState(pos.west()).getBlock()==ModBlocks.sapling && world.getBlockState(pos.west()).getValue(TYPE)==EnumSaplingType.YEW) {
					((BlockModSapling)world.getBlockState(pos.west()).getBlock()).grow(world, rand, pos.west(), world.getBlockState(pos.west()));
					return;
				}
			}
		}
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).ordinal() | (state.getValue(STAGE)<<2);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, EnumSaplingType.values()[meta&3]).withProperty(STAGE, meta>>2);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, STAGE);
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		if (random.nextDouble()<0.1) grow(worldIn, random, pos, state);
	}
	
	private void generateTree(World world, Random rand, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			switch(state.getValue(TYPE)) {
			case ELDER:
				TreeUtils.generateElderTree(world, pos, rand);
				break;
			case JUNIPER:
				TreeUtils.generateJuniperTree(world, pos, rand);
				break;
			case YEW:
				TreeUtils.generateYewTree(world, pos, rand);
				break;
			default:
				break;
			
			}
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(TYPE, EnumSaplingType.values()[stack.getMetadata()]), 3);
	}
	
	public static enum EnumSaplingType implements IStringSerializable {
		
		ELDER, JUNIPER, YEW;

		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
	}
	
	public static class PropertySapling extends PropertyEnum<EnumSaplingType> {
		protected PropertySapling(String name, Class<EnumSaplingType> valueClass, Collection<EnumSaplingType> allowedValues) {
			super(name, valueClass, allowedValues);
		}
	}

}
