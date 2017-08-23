package zabi.minecraft.covens.common.registries.brewing.environmental;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class ExtinguishFireEffect extends EnvironmentalPotionEffect {

	public ExtinguishFireEffect(Potion potion) {
		super(potion);
	}

	@Override
	public void splashedOn(World world, BlockPos pos, EntityLivingBase thrower, CovenPotionEffect data) {
		MutableBlockPos search = new MutableBlockPos();
		int p = 3*(data.getPersistency()+1);
		for (int dx = -p; dx<=p; dx++) for (int dz = -p; dz<=p; dz++) for (int dy = -p/2; dy<=p/2; dy++) {
			search.setPos(pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz);
			if (world.getBlockState(search).getBlock().equals(Blocks.FIRE)) {
				world.setBlockState(search, Blocks.AIR.getDefaultState(), 3);
			}
		}
	}

}
