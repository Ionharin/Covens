package zabi.minecraft.covens.common.tileentity;

import java.util.stream.Collectors;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.registries.fermenting.BarrelRecipe;
import zabi.minecraft.covens.common.util.machines.AutomatableInventory;

public class TileEntityBarrel extends TileEntityBaseTickable implements IAltarUser {
	
	AxisAlignedBB around;
	
	FluidTank internalTank = new FluidTank(Fluid.BUCKET_VOLUME) {
		protected void onContentsChanged() {
			if (this.getFluidAmount()==0 || this.getFluidAmount()==Fluid.BUCKET_VOLUME) markDirty();
			checkRecipe();
		};
	};
	AutomatableInventory inv = new AutomatableInventory(7) {
		
		@Override
		public void onMarkDirty() {
			markTileDirty();
		}
		
		@Override
		public boolean canMachineInsert(int slot, ItemStack stack) {
			return slot!=0;
		}
		
		@Override
		public boolean canMachineExtract(int slot, ItemStack stack) {
			return slot==0;
		}
	};
	int brewingTime = 0, barrelType = 0, powerAbsorbed = 0, powerRequired = 0, timeRequired = 0;
	String recipeName = null;
	TileEntityAltar te = null;	//cached
	private BarrelRecipe cachedRecipe = null;

	public TileEntityBarrel() {
		internalTank.setTileEntity(this);
	}
	
	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		brewingTime = tag.getInteger("time");
		barrelType = tag.getInteger("type");
		powerAbsorbed = tag.getInteger("powerAbsorbed");
		inv.loadFromNBT(tag.getCompoundTag("inv"));
		internalTank = internalTank.readFromNBT(tag.getCompoundTag("fluid"));
		internalTank.setCanDrain(true);
		internalTank.setCanFill(true);
		if (tag.hasKey("recipe")) {
			recipeName = tag.getString("recipe");
			getRecipe();//Refresh cache
		}
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("time", brewingTime);
		tag.setInteger("type", barrelType);
		tag.setInteger("powerAbsorbed", powerAbsorbed);
		tag.setTag("inv", inv.saveToNbt());
		NBTTagCompound fluid = new NBTTagCompound();
		internalTank.writeToNBT(fluid);
		tag.setTag("fluid", fluid);
		if (recipeName!=null) tag.setString("recipe", recipeName);
	}

	@Override
	protected void tick() {
		if (!world.isRemote) {
			if (hasRecipe()) {
				BarrelRecipe currentRecipe = getRecipe();
				if (powerAbsorbed<currentRecipe.getPower()) {
					if (consumePower(1)) {
						powerAbsorbed++;
						markDirty();
						return;
					}
				} else {
					if (brewingTime<currentRecipe.getRequiredTime()) {
						brewingTime++;
						markDirty();
						return;
					} else {
						currentRecipe.onFinish(world, inv.getList().stream().skip(1).collect(Collectors.toList()), pos, internalTank.drain(1000, true));
						ItemStack output = inv.getStackInSlot(0);
						if (output.isEmpty()) inv.setInventorySlotContents(0, currentRecipe.getResult());
						else output.grow(currentRecipe.getResult().getCount());
						brewingTime = 0;
						powerAbsorbed = 0;
						recipeName = null;
						cachedRecipe = null;
						markDirty();
						checkRecipe();
						return;
					}
				}
			}
		}
	}
	
	public void checkRecipe() {
		if (this.recipeName!=null && this.recipeName.length()>0) {
			return;
		}
		refreshRecipeStatus(BarrelRecipe.getRecipe(world, inv.getList().stream().skip(1).collect(Collectors.toList()), pos, internalTank.drainInternal(1000, false)));
	}
	
	public void refreshRecipeStatus(BarrelRecipe incomingRecipe) {
		if (incomingRecipe!=null) {
			ItemStack recipeOutput = inv.getStackInSlot(0);
			if (recipeOutput.isEmpty() || recipeOutput.getMaxStackSize()>=recipeOutput.getCount()+incomingRecipe.getResult().getCount()) {
				internalTank.drain(Fluid.BUCKET_VOLUME, true);
				this.cachedRecipe = incomingRecipe;
				this.recipeName = incomingRecipe.getRegistryName().toString();
				powerRequired = cachedRecipe.getPower();
				timeRequired = cachedRecipe.getRequiredTime();
				markDirty();
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		if (capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) internalTank;
		if (capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inv;
		return super.getCapability(capability, facing);
	}
	
	public void setType(BlockBarrel.WoodType type) {
		barrelType = type.ordinal();
		markDirty();
	}
	
	public void setType(int type) {
		barrelType = type;
		markDirty();
	}
	
	public BlockBarrel.WoodType getType() {
		return BlockBarrel.WoodType.values()[barrelType];
	}
	
	public boolean consumePower(int power) {
		if (power==0) return true;
		if (te==null || te.isInvalid()) te = TileEntityAltar.getClosest(pos, world);
		if (te==null) return false;
		return te.consumePower(power, false);
	}

	public boolean hasRecipe() {
		return recipeName!=null && recipeName.length()>0;
	}
	
	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}

	public int getBrewingTime() {
		return brewingTime;
	}

	public int getPowerAbsorbed() {
		return powerAbsorbed;
	}

	public String getRecipeName() {
		return recipeName;
	}
	
	@Nullable
	public BarrelRecipe getRecipe() {
		if (cachedRecipe==null) {
			if (recipeName==null || recipeName.length()==0) return null;
			cachedRecipe = BarrelRecipe.REGISTRY.getValue(new ResourceLocation(recipeName));
			if (cachedRecipe!=null) {
				powerRequired = cachedRecipe.getPower();
				timeRequired = cachedRecipe.getRequiredTime();
			}
			markDirty();
		}
		return cachedRecipe;
	}

	@Override
	protected void NBTSaveUpdate(NBTTagCompound tag) {
		tag.setInteger("bt", brewingTime);
		tag.setInteger("pw", powerAbsorbed);
		tag.setInteger("ty", barrelType);
		tag.setInteger("pr", powerRequired);
		tag.setInteger("tr", timeRequired);
		if (recipeName!=null) {
			if (getRecipe()!=null) {
				tag.setString("rc", recipeName);
			}
		}
		tag.setTag("tank", internalTank.writeToNBT(new NBTTagCompound()));
	}

	@Override
	protected void NBTLoadUpdate(NBTTagCompound tag) {
		Log.i(tag);
		brewingTime = tag.getInteger("bt");
		powerAbsorbed = tag.getInteger("pw");
		barrelType = tag.getInteger("ty");
		powerRequired = tag.getInteger("pr");
		timeRequired = tag.getInteger("tr");
		if (tag.hasKey("rc")) {
			recipeName = tag.getString("rc");
		}
		else recipeName = null;
		internalTank.readFromNBT(tag.getCompoundTag("tank"));
	}

	public int getPowerRequired() {
		return powerRequired;
	}

	public int getTimeRequired() {
		return timeRequired;
	}
	
	protected void markTileDirty() {
		markDirty();
		checkRecipe();
	}
	
	public IInventory getInventory() {
		return inv;
	}

}
