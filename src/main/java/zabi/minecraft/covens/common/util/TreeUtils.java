package zabi.minecraft.covens.common.util;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;

@SuppressWarnings("unused")
public class TreeUtils {
	
	public static void generateElderTree(World world, BlockPos pos, Random r) {
		IBlockState leaves = ModBlocks.leaves_elder.getDefaultState();
		int h = generateTrunk(3, 5,  ModBlocks.log_elder.getDefaultState(), world, pos, r);
		for (int dx=-2;dx<3;dx++) for (int dz=-2;dz<3;dz++) for (int dy = -2; dy<1; dy++) {
			BlockPos current = pos.up(h).add(dx, dy, dz);
			if (world.isAirBlock(current) && ((Math.abs(dz)!=2 || Math.abs(dx)!=2) || r.nextDouble()<0.2)) {
				if ((dy<0 || (dx<2 && dz<2 && dx>-2 && dz>-2)) ) world.setBlockState(current, leaves, 3);
			}
		}
	}

	public static void generateJuniperTree(World world, BlockPos pos, Random r) {
		int h = generateTrunk(2, 4,  ModBlocks.log_juniper.getDefaultState(), world, pos, r);
	}

	public static void generateYewTree(World world, BlockPos pos, Random r) {
		int h1 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos, r);
		int h2 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.east(), r);
		int h3 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.east().north(), r);
		int h4 = generateTrunk(4, 6,  ModBlocks.log_yew.getDefaultState(), world, pos.north(), r);
		int hmin = Math.min(Math.min(h1, h2), Math.min(h3, h4));
		int hmax = Math.max(Math.max(h1, h2), Math.max(h3, h4));
	}
	
	public static boolean canSaplingGrow(EnumSaplingType type, World world, BlockPos pos) {
		return true;//TODO
	}
	
	private static int generateTrunk(int minHeight, int maxHeight, IBlockState log, World world, BlockPos pos, Random r) {
		int h = minHeight + r.nextInt(maxHeight-minHeight+1);
		for (int i=0;i<h;i++) if (world.isAirBlock(pos.up(i)) || i==0) world.setBlockState(pos.up(i), log, 3);
		return h;
	}

}
