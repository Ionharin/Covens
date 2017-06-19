package zabi.minecraft.covens.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAltar extends TileEntityBase {

	@Override
	protected void NBTLoad(NBTTagCompound tag) {

	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {

	}

	@Override
	protected void tick() {

	}
	
	public int getAltarPower() {
		return 10000;
	}
	
	public boolean consumePower(int power) {
		return true;
	}

}
