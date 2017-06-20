package zabi.minecraft.covens.common.tileentity;

import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.covens.common.block.BlockWitchAltar;
import zabi.minecraft.covens.common.block.BlockWitchAltar.AltarMultiblockType;
import zabi.minecraft.covens.common.block.ModBlocks;

public class TileEntityAltar extends TileEntityBase {
	
	private static final int REFRESH_TIME = 200, RADIUS = 16, MAX_SCORE_PER_CATEGORY = 20;
	
	int power = 0, maxPower=0, gain = 0;
	int refreshTimer = REFRESH_TIME;

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		power = tag.getInteger("power");
		maxPower = tag.getInteger("maxPower");
		gain = tag.getInteger("gain");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("power", power);
		tag.setInteger("gain", gain);
		tag.setInteger("maxPower", maxPower);
	}

	@Override
	protected void tick() {
		refreshTimer++;
		if (refreshTimer>=REFRESH_TIME) {
			refreshTimer = 0;
			refreshNature();
		}
		power+=gain;
		if (power>maxPower) power=maxPower;
	}
	
	private void refreshNature() {
		gain = 0;
		maxPower = 0;
		HashMap<IBlockState, Integer> map = new HashMap<IBlockState, Integer>();
		for (int i = -RADIUS; i <= RADIUS; i++) {
			for (int j = -RADIUS; j <= RADIUS; j++) {
				for (int k = -RADIUS; k <= RADIUS; k++) {
					BlockPos checking = getPos().add(i, j, k);
					int score = getPowerValue(checking);
					if (score>0) {
						IBlockState state = getWorld().getBlockState(checking);
						int currentScore = 0;
						if (map.containsKey(state)) currentScore = map.get(state);
						if (currentScore<MAX_SCORE_PER_CATEGORY) map.put(state, currentScore+score);
					}
				}
			}
		}
		map.values().forEach(i -> maxPower+=i);
		double multiplier = 1;
		for (int dx = -1; dx<=1;dx++) for (int dz = -1; dz<=1;dz++) {
			BlockPos ps = getPos().add(dx, 0, dz);
			if (getWorld().getBlockState(ps).getBlock().equals(ModBlocks.altar) && !getWorld().getBlockState(ps).getValue(BlockWitchAltar.ALTAR_TYPE).equals(AltarMultiblockType.UNFORMED)) {
				multiplier += getMultiplier(getWorld().getBlockState(ps.up()));
				gain += getGain(getWorld().getBlockState(ps.up()));
			}
		}
		maxPower*=multiplier;
	}

	private int getGain(IBlockState blockState) {
		return 50; //TODO need to build a map of (block - power regen value)
	}

	private double getMultiplier(IBlockState blockState) {
		return 0; //TODO same for maxpower multiplier
	}

	private int getPowerValue(BlockPos add) {
		return 50; //TODO same for natural block to power
	}

	public int getAltarPower() {
		return power;
	}
	
	public boolean consumePower(int amount) {
		if (amount>power) return false;
		power-=amount;
		return true;
	}

}
