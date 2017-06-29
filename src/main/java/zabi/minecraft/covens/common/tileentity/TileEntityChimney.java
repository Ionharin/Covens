package zabi.minecraft.covens.common.tileentity;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.chimney.ChimneyRecipe;

public class TileEntityChimney extends TileEntityBase implements IInventory /*implements ISidedInventory*/ {
	
	ItemStackHandler handler = new ItemStackHandler(2) {
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (slot==1) return stack;
			if (slot==0 && stack.getItem().equals(ModItems.misc) && stack.getMetadata()==0) return super.insertItem(slot, stack, simulate);
			return stack;
		}
	};
	private static Method canSmelt = ReflectionHelper.findMethod(TileEntityFurnace.class, "canSmelt", "func_145948_k", new Class<?>[0]);

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey("jars")) handler.setStackInSlot(0, new ItemStack(tag.getCompoundTag("jars")));
		if (tag.hasKey("product")) handler.setStackInSlot(1, new ItemStack(tag.getCompoundTag("product")));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return (T) handler;
		return super.getCapability(capability, facing);
	}
	
	@Override
	protected void NBTSave(NBTTagCompound tag) {
		NBTTagCompound jarsTag = new NBTTagCompound();
		handler.getStackInSlot(0).writeToNBT(jarsTag);
		tag.setTag("jars", jarsTag);
		NBTTagCompound productTag = new NBTTagCompound();
		handler.getStackInSlot(1).writeToNBT(productTag);
		tag.setTag("product", productTag);
	}

	@Override
	protected void tick() {
		if (!world.isRemote) {
			TileEntityFurnace furnace = ((TileEntityFurnace) getWorld().getTileEntity(this.getPos().down()));
			if (furnace==null) return;
			try {
				if (furnace.isBurning() && !handler.getStackInSlot(0).isEmpty()) {
					int time = furnace.getField(2);
					int timeMax = furnace.getField(3);
					if (time+1==timeMax && Math.random()<getChance()) {
						if ((boolean) canSmelt.invoke(furnace, new Object[0])) {
							ItemStack smeltingItem = furnace.getStackInSlot(0);
							ChimneyRecipe recipe = ChimneyRecipe.getRecipeFor(smeltingItem);
							if (recipe==null) return;
							ItemStack result = recipe.getOutput();
							int count = result.getCount();
							ItemStack product = handler.getStackInSlot(1);
							if (!product.isEmpty()) {
								if (!result.isItemEqual(product)) return;
								count += product.getCount();
								if (count > product.getMaxStackSize()) return;
								if (count > 64) return;
							}
							product = result;
							product.setCount(count);
							handler.setStackInSlot(1, product);
							handler.extractItem(0, 1, false);
							markDirty();
							world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return handler.getStackInSlot(0).isEmpty() && handler.getStackInSlot(1).isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return getStackInSlot(index).splitStack(count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack res = handler.getStackInSlot(index).copy();
		setInventorySlotContents(index, ItemStack.EMPTY.copy());
		return res;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		handler.setStackInSlot(index, stack);
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
		if (index==0 && stack.getItem().equals(ModItems.misc) && stack.getMetadata()==0) return true;
		return false;
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
		removeStackFromSlot(0);
		removeStackFromSlot(1);
	}

	@Override
	public String getName() {
		return I18n.translateToLocal("tile.chimney.name");
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	public float getChance() {
		return 0.3F;
	}

}
