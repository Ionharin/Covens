package zabi.minecraft.covens.common.item;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.block.BlockModSapling;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemEerieSeeds extends Item {
	public ItemEerieSeeds() {
		this.setCreativeTab(ModCreativeTabs.products);
		this.setUnlocalizedName("eerie_seeds");
		this.setRegistryName(Reference.MID, "eerie_seeds");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.getBlockState(pos).getBlock().equals(Blocks.GRASS)) {
			worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1, 1, false);
			if (!worldIn.isRemote) {
				ArrayList<BlockPos> validSpots = new ArrayList<BlockPos>();
				for (int dx=-2; dx<=2; dx++) for (int dz=-2; dz<=2; dz++) {
					BlockPos spot = pos.add(dx, 0, dz);
					if (worldIn.getBlockState(spot).getBlock().equals(Blocks.GRASS) && worldIn.isAirBlock(spot.up())) validSpots.add(spot);
				}
				
				Random r = new Random();
				for (int i = 0; i<2+r.nextInt(3); i++) {
					if (validSpots.size()>0) {
						int rngIndex = r.nextInt(validSpots.size());
						BlockPos place = validSpots.get(rngIndex);
						growOn(worldIn, place, r);
						validSpots.remove(rngIndex);
					}
				}
				
			}
			if (!player.isCreative()) player.getHeldItem(hand).shrink(1);
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	private static IBlockState[] validResources = new IBlockState[] {
			ModBlocks.sapling.getDefaultState().withProperty(BlockModSapling.TYPE, EnumSaplingType.ELDER),
			ModBlocks.sapling.getDefaultState().withProperty(BlockModSapling.TYPE, EnumSaplingType.JUNIPER),
			ModBlocks.sapling.getDefaultState().withProperty(BlockModSapling.TYPE, EnumSaplingType.YEW),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.SPRUCE),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.BIRCH),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.DARK_OAK),
			Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.ACACIA),
			Blocks.BROWN_MUSHROOM.getDefaultState(),
			Blocks.RED_MUSHROOM.getDefaultState(),
			Blocks.WATERLILY.getDefaultState(),
			Blocks.VINE.getDefaultState(),
			Blocks.YELLOW_FLOWER.getDefaultState(),
			Blocks.RED_FLOWER.getDefaultState()
	};

	private void growOn(World worldIn, BlockPos place, Random r) {
		worldIn.setBlockState(place.up(), validResources[r.nextInt(validResources.length)], 2);
	}
}
