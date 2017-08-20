package zabi.minecraft.covens.common.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.block.BlockModLog;
import zabi.minecraft.covens.common.block.BlockModSapling;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;

public class TreeUtils {
	
	public static void generateElderTree(World world, BlockPos pos, Random r) {
		IBlockState leaves = ModBlocks.leaves_elder.getDefaultState();
		int h = generateTrunk(3, 5,  ModBlocks.log_elder.getDefaultState(), world, pos, r);
		for (int dx=-2;dx<3;dx++) for (int dz=-2;dz<3;dz++) for (int dy = -2; dy<1; dy++) {
			BlockPos current = pos.up(h).add(dx, dy, dz);
			if (isAirBlock(world, current) && ((Math.abs(dz)!=2 || Math.abs(dx)!=2) || r.nextDouble()<0.2)) {
				if ((dy<0 || (dx<2 && dz<2 && dx>-2 && dz>-2)) ) world.setBlockState(current, leaves, 3);
			}
		}
	}

	public static void generateJuniperTree(World world, BlockPos pos, Random r) {
		int h = generateTrunk(2, 4,  ModBlocks.log_juniper.getDefaultState(), world, pos, r);
		EnumFacing branchOffset = EnumFacing.HORIZONTALS[r.nextInt(4)];
		BlockPos branching = pos.up(h).offset(branchOffset);
		IBlockState log = ModBlocks.log_juniper.getDefaultState().withProperty(BlockModLog.LOG_AXIS, EnumAxis.NONE);
		ArrayList<BlockPos> logs = new ArrayList<BlockPos>();
		if (isAirBlock(world, branching)) {
			world.setBlockState(branching, log, 3);
			logs.add(branching);
		}
		BlockPos other = branching.offset(branchOffset.getOpposite(), 2);
		if (isAirBlock(world, other)) {
			world.setBlockState(other, log, 3);
			logs.add(other);
		}
		for (int i = 0; i<h/2;i++) {
			BlockPos current = branching.up().offset(branchOffset, i+1);
			if (isAirBlock(world, current)) {
				logs.add(current);
				world.setBlockState(current, log, 3);
			}
		}
		
		IBlockState leaves = ModBlocks.leaves_juniper.getDefaultState();
		for (BlockPos p:logs) {
			for (EnumFacing f:EnumFacing.VALUES) {
				BlockPos lpos1 = p.offset(f);
				if (isAirBlock(world, lpos1)) world.setBlockState(lpos1, leaves, 3);
				for (EnumFacing f2:EnumFacing.VALUES) if (f2!=EnumFacing.DOWN){
					BlockPos lpos = p.offset(f).offset(f2);
					if (isAirBlock(world, lpos) && r.nextDouble()<0.8D) world.setBlockState(lpos, leaves, 3);
				}
			}
		}
		
	}

	public static void generateYewTree(World world, BlockPos pos, Random r) {
		int h1 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos, r);
		int h2 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.east(), r);
		int h3 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.east().north(), r);
		int h4 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.north(), r);
		int hmin = Math.min(Math.min(h1, h2), Math.min(h3, h4));
		int hmax = Math.max(Math.max(h1, h2), Math.max(h3, h4));
		
		IBlockState leaves = ModBlocks.leaves_yew.getDefaultState();
		
		for (int dx=-2;dx<4;dx++) for (int dz=-3;dz<3;dz++) for (int dy = -2; dy<hmax-hmin+2; dy++) {
			BlockPos current = pos.up(hmin).add(dx, dy, dz);
			if (isAirBlock(world, current) /*&& ((Math.abs(dz)!=2 || Math.abs(dx)!=2) || r.nextDouble()<0.2)*/) {
				if (dx==-2 || dx==3 || dz==-3 || dz==2) if (r.nextDouble()<0.1 || dy>=hmax-hmin) continue;
				if (dx==-1 || dx==2 || dz==-2 || dz==1) if (dy==hmax-hmin+1) continue;
				if (dx==-2 && dz==-3) continue;
				if (dx==-2 && dz==2) continue;
				if (dx==3 && dz==-3) continue;
				if (dx==3 && dz==2) continue;
				world.setBlockState(current, leaves, 3);
			}
		}
		
		
	}
	
	public static boolean canSaplingGrow(EnumSaplingType type, World world, BlockPos pos) {
		if (world.isRemote) return false;
		if (!isAirBlock(world, pos.up())) return false;
		if (type==EnumSaplingType.ELDER) {
			for (int dx=-1;dx<2;dx++) for (int dz=-1;dz<2;dz++) for (int dy = -2; dy<1; dy++) {
				BlockPos current = pos.up(2).add(dx, dy, dz);
				if (!isAirBlock(world, current)) return false;
			}
		} else if (type==EnumSaplingType.JUNIPER) {
			for (int dx=-2;dx<3;dx++) for (int dz=-2;dz<3;dz++) for (int dy = -2; dy<2; dy++) {
				BlockPos current = pos.up(2).add(dx, dy, dz);
				if (!isAirBlock(world, current)) return false;
			}
		} else if (type==EnumSaplingType.YEW) {
			
			if (world.getBlockState(pos.north()).getBlock()!=ModBlocks.sapling || world.getBlockState(pos.north()).getValue(BlockModSapling.TYPE)!=EnumSaplingType.YEW) return false;
			if (world.getBlockState(pos.east()).getBlock()!=ModBlocks.sapling || world.getBlockState(pos.east()).getValue(BlockModSapling.TYPE)!=EnumSaplingType.YEW) return false;
			if (world.getBlockState(pos.north().east()).getBlock()!=ModBlocks.sapling || world.getBlockState(pos.north().east()).getValue(BlockModSapling.TYPE)!=EnumSaplingType.YEW) return false;
			for (int dx=-2;dx<3;dx++) for (int dz=-2;dz<3;dz++) for (int dy = 0; dy<3; dy++) {
				BlockPos current = pos.up(2).add(dx, dy, dz);
				if (!isAirBlock(world, current)) {
					return false;
				}
			}
		}
		return true;//TODO
	}
	
	private static int generateTrunk(int minHeight, int maxHeight, IBlockState log, World world, BlockPos pos, Random r) {
		int h = minHeight + r.nextInt(maxHeight-minHeight+1);
		for (int i=0;i<h;i++) if (isAirBlock(world, pos.up(i)) || i==0) world.setBlockState(pos.up(i), log, 3);
		return h;
	}
	
	private static boolean isAirBlock(World world, BlockPos current) {
		return world.getBlockState(current).getBlock().canBeReplacedByLeaves(world.getBlockState(current), world, current);
	}

}
