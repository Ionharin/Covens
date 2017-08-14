package zabi.minecraft.covens.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface CovensData {
	
	@CapabilityInject(CovensData.class)
	public static final Capability<CovensData> CAPABILITY = null;
	
	public int getTint();
	public void setTint(int tint);
	
	public static class Impl implements CovensData {
		
		private int tint = -1;

		@Override
		public int getTint() {
			return tint;
		}

		@Override
		public void setTint(int tint) {
			this.tint = tint;
		}
	}
	
	public static class Storage implements IStorage<CovensData> {
		@Override
		public NBTBase writeNBT(Capability<CovensData> capability, CovensData instance, EnumFacing side) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("tint", instance.getTint());
			return tag;
		}

		@Override
		public void readNBT(Capability<CovensData> capability, CovensData instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			instance.setTint(tag.getInteger("tint"));
		}
	}
	
	public static class Provider implements ICapabilitySerializable<NBTBase> {
		
		private CovensData default_capability = CAPABILITY.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability==CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability==CAPABILITY) return CAPABILITY.<T>cast(default_capability);
			return null;
		}

		@Override
		public NBTBase serializeNBT() {
			return CAPABILITY.getStorage().writeNBT(CAPABILITY, default_capability, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			CAPABILITY.getStorage().readNBT(CAPABILITY, default_capability, null, nbt);
		}
		
	}
}
