package zabi.minecraft.covens.common.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;

public interface PlayerData {

	@CapabilityInject(PlayerData.class)
	public static final Capability<PlayerData> CAPABILITY = null;

	@Nullable public EnumInfusion getInfusion();
	public void setInfusion(@Nullable EnumInfusion infusion);
	public int getInfusionPower();
	public boolean usePower(int amount, boolean simulate);
	public void restorePower();
	public void setMaxPower(int maxPower);
	public void setPower(int power);
	public int getMaxPower();
	public BlockPos getSpectatingPoint();
	public void setSpectatingPoint(BlockPos pos);

	public static class Impl implements PlayerData {

		private EnumInfusion infusion = null;
		private int infusionPower = 0, maxInfusionPower = 0;
		private BlockPos pos = null; 

		@Override
		public EnumInfusion getInfusion() {
			return infusion;
		}

		@Override
		public void setInfusion(EnumInfusion infusion) {
			this.infusion = infusion;
			if (infusion==null) {
				setMaxPower(0); 
				setPower(0);
			}
		}

		@Override
		public int getInfusionPower() {
			return infusionPower;
		}

		@Override
		public boolean usePower(int amount, boolean simulate) {
			if (infusion==null) return false;
			if (infusionPower<amount) return false;
			if (!simulate) {
				infusionPower-=amount;
				if (infusionPower<0) setInfusion(null);
			}
			return true;
		}

		@Override
		public void restorePower() {
			if (infusion!=null) {
				infusionPower += maxInfusionPower/20;
				if (infusionPower>maxInfusionPower) infusionPower = maxInfusionPower;
			}
		}

		@Override
		public void setMaxPower(int maxPower) {
			if (infusion!=null) maxInfusionPower=maxPower;
		}

		@Override
		public int getMaxPower() {
			if (infusion==null) return 0;
			return maxInfusionPower;
		}

		@Override
		public void setPower(int power) {
			if (infusion!=null) {
				infusionPower = power;
				if (infusionPower>maxInfusionPower) infusionPower = maxInfusionPower;
				if (power<=0) setInfusion(null);
			}
		}
		
		@Override
		public BlockPos getSpectatingPoint() {
			return pos;
		}
		
		@Override
		public void setSpectatingPoint(BlockPos pos) {
			this.pos = pos;
		}
	}

	public static class Storage implements IStorage<PlayerData> {
		@Override
		public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side) {
			NBTTagCompound tag = new NBTTagCompound();
			if (instance.getInfusion()!=null) {
				tag.setInteger("infusion", instance.getInfusion().ordinal());
				tag.setInteger("power", instance.getInfusionPower());
				tag.setInteger("maxPower", instance.getMaxPower());
			}
			if (instance.getSpectatingPoint()!=null) tag.setTag("spect_position", NBTUtil.createPosTag(instance.getSpectatingPoint()));
			return tag;
		}

		@Override
		public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("infusion")) {
				instance.setInfusion(EnumInfusion.values()[tag.getInteger("infusion")]);
				instance.setMaxPower(tag.getInteger("maxPower"));
				instance.setPower(tag.getInteger("power"));
			}
			if (tag.hasKey("spect_position")) instance.setSpectatingPoint(NBTUtil.getPosFromTag(tag.getCompoundTag("spect_position")));
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
