package zabi.minecraft.covens.common.capability;

import net.minecraft.entity.player.EntityPlayer;

public interface IRitualHandler {
	public boolean consumePower(int power);
	public void stopRitual(EntityPlayer player);
}
