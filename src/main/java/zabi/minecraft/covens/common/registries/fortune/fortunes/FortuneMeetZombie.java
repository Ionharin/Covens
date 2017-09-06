package zabi.minecraft.covens.common.registries.fortune.fortunes;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.covens.common.registries.fortune.Fortune;

public class FortuneMeetZombie extends Fortune {

	public FortuneMeetZombie(int weight, String name, String modid) {
		super(weight, name, modid);
	}

	@Override
	public boolean canBeUsedFor(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean canShouldBeAppliedNow(EntityPlayer player) {
		return player.getRNG().nextDouble()<0.01;
	}

	@Override
	public boolean apply(EntityPlayer player) {
		for (int i = 0; i<10; i++) {
			BlockPos pos = new BlockPos(player.posX+player.getRNG().nextGaussian()*4, player.posY, player.posZ+player.getRNG().nextGaussian()*4);
			if (player.world.isAirBlock(pos) && player.world.isAirBlock(pos.up())) {
				EntityZombie zombie = new EntityZombie(player.world);
				zombie.setPosition(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
				zombie.onInitialSpawn(player.world.getDifficultyForLocation(pos), null);
				player.world.spawnEntity(zombie);
				return true;
			}
		}
		return false;
	}

}
