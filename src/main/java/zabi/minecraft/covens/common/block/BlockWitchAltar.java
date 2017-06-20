package zabi.minecraft.covens.common.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityAltar;

public class BlockWitchAltar extends Block implements ITileEntityProvider {

	public static final PropertyAltar ALTAR_TYPE = new PropertyAltar("altar_multiblock", AltarMultiblockType.class, Arrays.asList(AltarMultiblockType.values()));
	
	public BlockWitchAltar() {
		super(Material.ROCK);
		this.setHardness(2);
		this.setCreativeTab(ModCreativeTabs.rituals);
		this.setUnlocalizedName("witch_altar");
		GameRegistry.register(this, new ResourceLocation(Reference.MID, "witch_altar"));
		this.setDefaultState(this.blockState.getBaseState().withProperty(ALTAR_TYPE, AltarMultiblockType.UNFORMED));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(ALTAR_TYPE).equals(AltarMultiblockType.TILE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return meta==2?new TileEntityAltar():null;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return !hasFormedBlocksAround(worldIn, pos.north(), pos.south(), pos.east(), pos.west(), pos.west().north(), pos.west().south(), pos.east().north(), pos.east().south());
	}
	
	private boolean hasFormedBlocksAround(World world, BlockPos... pos) {
		for (BlockPos p:pos) {
			IBlockState bs = world.getBlockState(p);
			if (bs.getBlock().equals(ModBlocks.altar) && !bs.getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) return true;
		}
		return false;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ALTAR_TYPE);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ALTAR_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ALTAR_TYPE, AltarMultiblockType.values()[meta]);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if (isMBBase(worldIn, pos.north().north().east())) if (tryFormMultiblock(worldIn, pos, pos.north().north().east())) return;
		if (isMBBase(worldIn, pos.north().north().west())) if (tryFormMultiblock(worldIn, pos, pos.north().north().west())) return;
		if (isMBBase(worldIn, pos.north().east().east())) if (tryFormMultiblock(worldIn, pos, pos.north().east().east())) return;
		if (isMBBase(worldIn, pos.north().west().west())) if (tryFormMultiblock(worldIn, pos, pos.north().west().west())) return;
		if (isMBBase(worldIn, pos.south().south().east())) if (tryFormMultiblock(worldIn, pos, pos.south().south().east())) return;
		if (isMBBase(worldIn, pos.south().south().west())) if (tryFormMultiblock(worldIn, pos, pos.south().south().west())) return;
		if (isMBBase(worldIn, pos.south().east().east())) if (tryFormMultiblock(worldIn, pos, pos.south().east().east())) return;
		if (isMBBase(worldIn, pos.south().west().west())) if (tryFormMultiblock(worldIn, pos, pos.south().west().west())) return;
	
		if (tryFormMultiblock(worldIn, pos.east(), pos.south().west())) return;
		if (tryFormMultiblock(worldIn, pos.east(), pos.north().west())) return;
		if (tryFormMultiblock(worldIn, pos.north(), pos.south().west())) return;
		if (tryFormMultiblock(worldIn, pos.north(), pos.south().east())) return;
	}
	
	private boolean tryFormMultiblock(World worldIn, BlockPos firstCorner, BlockPos secondCorner) {
		ArrayList<BlockPos> blocks = new ArrayList<BlockPos>(6);

		int y = firstCorner.getY();
		int sx = Math.min(firstCorner.getX(), secondCorner.getX());
		int ex = Math.max(firstCorner.getX(), secondCorner.getX());
		int sy = Math.min(firstCorner.getZ(), secondCorner.getZ());
		int ey = Math.max(firstCorner.getZ(), secondCorner.getZ());

		for (int i = sx - 1; i <= ex + 1; i++) {
			for (int j = sy - 1; j <= ey + 1; j++) { //-1 +1 to check the border too, it needs to be a non-altar block, doesn't matter meta
				BlockPos checked = new BlockPos(i, y, j);
				if (!isMBBase(worldIn, checked) && i>=sx && i<=ex && j>=sy && j<=ey) return false; //If something in the altar area is not an unformed altar, abort
				blocks.add(checked);
				if (worldIn.getBlockState(checked).getBlock().equals(ModBlocks.altar) && (i < sx || i > ex || j < sy || j > ey)) return false; //At least one other altar block is touching the multiblock you're trying to form
			}
		}

			if (ex-sx<ey-sy) {
				worldIn.setBlockState(new BlockPos(sx, y, sy+1), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.TILE));
				worldIn.setBlockState(new BlockPos(ex, y, sy+1), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.SIDE));
			} else {
				worldIn.setBlockState(new BlockPos(sx+1, y, sy), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.TILE));
				worldIn.setBlockState(new BlockPos(sx+1, y, ey), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.SIDE));
			}
			worldIn.setBlockState(new BlockPos(sx, y, sy), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.CORNER));
			worldIn.setBlockState(new BlockPos(sx, y, ey), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.CORNER));
			worldIn.setBlockState(new BlockPos(ex, y, sy), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.CORNER));
			worldIn.setBlockState(new BlockPos(ex, y, ey), ModBlocks.altar.getDefaultState().withProperty(BlockWitchAltar.ALTAR_TYPE, BlockWitchAltar.AltarMultiblockType.CORNER));
		return true;
	}

	private static boolean isMBBase(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().equals(ModBlocks.altar) && world.getBlockState(pos).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED);
	}
	
	@Override
	public boolean requiresUpdates() {
		return true;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		Log.i(blockIn);
		if (world.isAirBlock(fromPos) && !checkRecursive(world,pos,0, new ArrayList<BlockPos>(6)) && blockIn.equals(ModBlocks.altar)) {
			dismantleRecursive(world, pos);
		};
	}
	
	private void dismantleRecursive(World world, BlockPos pos) {
		world.setBlockState(pos, getDefaultState());
		if (world.getBlockState(pos.north()).getBlock().equals(ModBlocks.altar) && !world.getBlockState(pos.north()).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) dismantleRecursive(world, pos.north());
		if (world.getBlockState(pos.south()).getBlock().equals(ModBlocks.altar) && !world.getBlockState(pos.south()).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) dismantleRecursive(world, pos.south());
		if (world.getBlockState(pos.west()).getBlock().equals(ModBlocks.altar) && !world.getBlockState(pos.west()).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) dismantleRecursive(world, pos.west());
		if (world.getBlockState(pos.east()).getBlock().equals(ModBlocks.altar) && !world.getBlockState(pos.east()).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) dismantleRecursive(world, pos.east());
	}

	private boolean checkRecursive(IBlockAccess world, BlockPos pos, int i, ArrayList<BlockPos> arrayList) {
		if (i==5 && world.getBlockState(pos).getBlock().equals(ModBlocks.altar) && !world.getBlockState(pos).getValue(ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) return true;
		arrayList.add(pos);
		if (!arrayList.contains(pos.north()) && world.getBlockState(pos).getBlock().equals(ModBlocks.altar)) return checkRecursive(world, pos.north(), i+1, arrayList);
		if (!arrayList.contains(pos.south()) && world.getBlockState(pos).getBlock().equals(ModBlocks.altar)) return checkRecursive(world, pos.south(), i+1, arrayList);
		if (!arrayList.contains(pos.west()) && world.getBlockState(pos).getBlock().equals(ModBlocks.altar)) return checkRecursive(world, pos.west(), i+1, arrayList);
		if (!arrayList.contains(pos.east()) && world.getBlockState(pos).getBlock().equals(ModBlocks.altar)) return checkRecursive(world, pos.east(), i+1, arrayList);
		return false;
	}
	

	//###########################################################################################################
	
	public static class PropertyAltar extends PropertyEnum<AltarMultiblockType> {
		protected PropertyAltar(String name, Class<AltarMultiblockType> valueClass, Collection<AltarMultiblockType> allowedValues) {
			super(name, valueClass, allowedValues);
		}
	}
	
	public static enum AltarMultiblockType implements IStringSerializable {
		UNFORMED, CORNER, TILE, SIDE;
		
		@Override
		public String getName() {
			return this.name().toLowerCase();
		}
		
	}
}
