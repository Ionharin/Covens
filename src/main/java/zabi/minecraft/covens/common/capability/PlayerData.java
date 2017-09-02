package zabi.minecraft.covens.common.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;

public interface PlayerData {

	@CapabilityInject(PlayerData.class)
	public static final Capability<PlayerData> CAPABILITY = null;

	@Nullable public EnumInfusion getInfusion();
	public void setInfusion(@Nullable EnumInfusion infusion);

	public static class Impl implements PlayerData {

		private EnumInfusion infusion = null;

		@Override
		public EnumInfusion getInfusion() {
			return infusion;
		}

		@Override
		public void setInfusion(EnumInfusion infusion) {
			this.infusion = infusion;
		}
	}

	public static class Storage implements IStorage<PlayerData> {
		@Override
		public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side) {
			NBTTagCompound tag = new NBTTagCompound();
			if (instance.getInfusion()!=null) tag.setInteger("infusion", instance.getInfusion().ordinal());
			return tag;
		}

		@Override
		public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("infusion")) instance.setInfusion(EnumInfusion.values()[tag.getInteger("infusion")]);
		}
	}

	public static class Provider implements ICapabilitySerializable<NBTBase> {

		private PlayerData default_capability = CAPABILITY.getDefaultInstance();

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
