package zabi.minecraft.covens.common.tileentity;

import java.util.stream.Collectors;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.registries.fermenting.BarrelRecipe;

public class TileEntityBarrel extends TileEntityBaseTickable implements IAltarUser, IItemHandler, IInventory {
	
	FluidTank internalTank = new FluidTank(Fluid.BUCKET_VOLUME) {
		protected void onContentsChanged() {
			if (this.getFluidAmount()==0 || this.getFluidAmount()==Fluid.BUCKET_VOLUME) markDirty();
			checkRecipe();
		};
	};
	NonNullList<ItemStack> inventory = NonNullList.withSize(7, ItemStack.EMPTY);
	NonNullList<ItemStack> inventoryLast = NonNullList.withSize(7, ItemStack.EMPTY);
	int brewingTime = 0, barrelType = 0, powerAbsorbed = 0;
	String recipe = null;
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
		ItemStackHelper.loadAllItems(tag.getCompoundTag("inventory"), inventory);
		ItemStackHelper.loadAllItems(tag.getCompoundTag("inventoryLast"), inventoryLast);
		internalTank = internalTank.readFromNBT(tag.getCompoundTag("fluid"));
		internalTank.setCanDrain(true);
		internalTank.setCanFill(true);
		if (tag.hasKey("recipe")) recipe = tag.getString("recipe");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("time", brewingTime);
		tag.setInteger("type", barrelType);
		tag.setInteger("powerAbsorbed", powerAbsorbed);
		tag.setTag("inventory", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventory, true));
		tag.setTag("inventoryLast", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventoryLast, true));
		NBTTagCompound fluid = new NBTTagCompound();
		internalTank.writeToNBT(fluid);
		tag.setTag("fluid", fluid);
		if (recipe!=null) tag.setString("recipe", recipe);
	}

	@Override
	protected void tick() {
		if (!world.isRemote) {
			if (hasRecipe()) {
				BarrelRecipe currentRecipe = getRecipe();
				if (powerAbsorbed<currentRecipe.getPower()) {
					if (consumePower(1)) {
						powerAbsorbed++;//TODO sync gui
						markDirty();
						return;
					}
				} else {
					if (brewingTime<currentRecipe.getRequiredTime()) {
						brewingTime++;//TODO sync gui
						markDirty();
						return;
					} else {
						ItemStack output = inventory.get(0);
						if (output.isEmpty()) inventory.set(0, currentRecipe.getResult());
						else output.grow(currentRecipe.getResult().getCount());
						currentRecipe.onFinish(world, inventoryLast.stream().skip(1).collect(Collectors.toList()), pos, internalTank.drain(1000, true));
						brewingTime = 0;
						powerAbsorbed = 0;
						recipe = null;
						cachedRecipe = null;
						inventoryLast.clear();
						//TODO sync gui
						markDirty();
						checkRecipe();
						return;
					}
				}
			}
		}
	}
	
	public void checkRecipe() {
		if (this.recipe!=null && this.recipe.length()>0) {
			return;
		}
		refreshRecipeStatus(BarrelRecipe.getRecipe(world, inventory.stream().skip(1).collect(Collectors.toList()), pos, internalTank.drainInternal(1000, false)));
	}
	
	private void refreshRecipeStatus(BarrelRecipe recipe) {
		if (recipe!=null) {
			ItemStack recipeOutput = inventory.get(0);
			if (recipeOutput.isEmpty() || recipeOutput.getMaxStackSize()>=recipeOutput.getCount()+recipe.getResult().getCount()) {
				for (int i=1; i<inventory.size(); i++) {
					inventoryLast.set(i, inventory.get(i).splitStack(1));
				}
				internalTank.drain(Fluid.BUCKET_VOLUME, true);
				this.cachedRecipe = recipe;
				this.recipe = recipe.getRegistryName().toString();
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
		if (capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this;
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
		return recipe!=null && recipe.length()>0;
	}
	
	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}

	@Override
	public int getSlots() {
		return 7;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot==0) return stack;
		ItemStack original = stack.copy();
		if (inventory.get(slot).isEmpty()) {
			if (!simulate) {
				inventory.set(slot, original);
				checkRecipe();
				markDirty();
			}
			return ItemStack.EMPTY;
		} else {
			ItemStack present = inventory.get(slot);
			if (ItemStack.areItemsEqual(present, original) && ItemStack.areItemStackTagsEqual(present, original)) {
				if (present.getMaxStackSize()>=present.getCount()+original.getCount()) {
					if (!simulate) {
						present.grow(original.getCount());
						checkRecipe();
						markDirty();
					}
					return ItemStack.EMPTY;
				} else {
					int instertedAmount = present.getMaxStackSize() - present.getCount();
					ItemStack inserted = original.splitStack(instertedAmount);
					if (!simulate) {
						inventory.set(slot, inserted);
						checkRecipe();
						markDirty();
					}
					return original;
				}
			} else {
				return stack;
			}
		}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot!=0) return ItemStack.EMPTY;
		ItemStack resultSlot = inventory.get(slot).copy();
		ItemStack extracted = resultSlot.splitStack(amount);
		if (!simulate) {
			inventory.set(0, resultSlot);
			markDirty();
			checkRecipe();
		}
		return extracted;
	}

	@Override
	public int getSlotLimit(int slot) {
		return getInventoryStackLimit();
	}
	
	public int getBrewingTime() {
		return brewingTime;
	}

	public int getPowerAbsorbed() {
		return powerAbsorbed;
	}

	public String getRecipeName() {
		return recipe;
	}
	
	@Nullable
	public BarrelRecipe getRecipe() {
		if (cachedRecipe==null) {
			if (recipe==null || recipe.length()==0) return null;
			cachedRecipe = BarrelRecipe.REGISTRY.getValue(new ResourceLocation(recipe));
			Log.i("New Recipe is: "+cachedRecipe);
			markDirty();
		}
		return cachedRecipe;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack s:inventory) if (!s.isEmpty()) return false;
		return true;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack res = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.EMPTY);
		return res;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventory.set(index, stack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index!=0;
	}

	@Override
	public int getField(int id) {
		return id==0?brewingTime:powerAbsorbed;
	}

	@Override
	public void setField(int id, int value) {
		if (id==0) brewingTime=value;
		else powerAbsorbed=value;
		markDirty();
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {
		for (ItemStack s:inventory) s.setCount(0);
		markDirty();
	}

}
