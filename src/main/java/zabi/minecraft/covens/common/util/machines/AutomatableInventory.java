package zabi.minecraft.covens.common.util.machines;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;

public abstract class AutomatableInventory implements IInventory, IItemHandler {

	NonNullList<ItemStack> stacks;
	
	public AutomatableInventory(int slots) {
		stacks = NonNullList.<ItemStack>withSize(slots, ItemStack.EMPTY);
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
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSlots() {
		return stacks.size();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (canMachineInsert(slot, stack)) {
			ItemStack stackPresent = getStackInSlot(slot); 
			if (stackPresent.isEmpty()) {
				if (!simulate) {
					setInventorySlotContents(slot, stack.copy());
					markDirty();
				}
				return ItemStack.EMPTY;
			} else {
				if (ItemStack.areItemsEqual(stack, stackPresent) && ItemStack.areItemStackTagsEqual(stack, stackPresent)) {
					int inserted = Math.min(stack.getCount(), stackPresent.getMaxStackSize()-stackPresent.getCount());
					if (inserted>0) {
						if (simulate) {
							return stack.copy().splitStack(stack.getCount() - inserted);
						}
						stackPresent.grow(inserted);
						markDirty();
						return stack.copy().splitStack(stack.getCount() - inserted);
					}
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (canMachineExtract(slot, getStackInSlot(slot).copy().splitStack(amount))) {
			ItemStack tgt;
			if (simulate) tgt = getStackInSlot(slot).copy().splitStack(amount);
			else {
				tgt = getStackInSlot(slot).splitStack(amount);
				markDirty();
			}
			return tgt;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public int getSizeInventory() {
		return getSlots();
	}

	@Override
	public boolean isEmpty() {
		return !stacks.stream().anyMatch(is -> !is.isEmpty());
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return stacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack res = getStackInSlot(index).splitStack(count);
		markDirty();
		return res;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack res = getStackInSlot(index);
		stacks.set(index, ItemStack.EMPTY);
		markDirty();
		return res;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		stacks.set(index, stack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		onMarkDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return canMachineInsert(index, stack);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		stacks.clear();
	}
	
	public void loadFromNBT(NBTTagCompound tag) {
		ItemStackHelper.loadAllItems(tag, stacks);
	}
	
	public NBTTagCompound saveToNbt() {
		NBTTagCompound tag = new NBTTagCompound();
		ItemStackHelper.saveAllItems(tag, stacks, true);
		return tag;
	}
	
	public ArrayList<ItemStack> getList() {
		return new ArrayList<ItemStack>(stacks);
	}
	
	public abstract boolean canMachineInsert(int slot, ItemStack stack);
	public abstract boolean canMachineExtract(int slot, ItemStack stack);
	public abstract void onMarkDirty();

}
