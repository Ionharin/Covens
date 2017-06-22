package zabi.minecraft.covens.common.tileentity;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zabi.minecraft.covens.common.crafting.chimney.ChimneyRecipe;
import zabi.minecraft.covens.common.item.ModItems;

public class TileEntityChimney extends TileEntityBase implements ISidedInventory {
	
	ItemStack jars = ItemStack.EMPTY.copy(), product = ItemStack.EMPTY.copy();
	private static Method canSmelt = ReflectionHelper.findMethod(TileEntityFurnace.class, "canSmelt", "func_145948_k", new Class<?>[0]);

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey("jars")) jars = new ItemStack(tag.getCompoundTag("jars"));
		if (tag.hasKey("product")) product = new ItemStack(tag.getCompoundTag("product"));
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		NBTTagCompound jarsTag = new NBTTagCompound();
		jars.writeToNBT(jarsTag);
		tag.setTag("jars", jarsTag);
		NBTTagCompound productTag = new NBTTagCompound();
		product.writeToNBT(productTag);
		tag.setTag("product", productTag);
	}

	@Override
	protected void tick() {
		TileEntityFurnace furnace = ((TileEntityFurnace) getWorld().getTileEntity(this.getPos().down()));
		if (furnace==null) return;
		try {
			if (furnace.isBurning()) {
				int time = furnace.getField(2);
				int timeMax = furnace.getField(3);
				if (time+1==timeMax) {
					if ((boolean) canSmelt.invoke(furnace, new Object[0])) {
						ItemStack smeltingItem = furnace.getStackInSlot(0);
						ChimneyRecipe recipe = ChimneyRecipe.getRecipeFor(smeltingItem);
						if (recipe==null) return;
						ItemStack result = recipe.getOutput();
						int count = result.getCount();
						if (!product.isEmpty()) {
							if (!result.isItemEqual(product)) return;
							count += product.getCount();
							if (count > product.getMaxStackSize()) return;
							if (count > getInventoryStackLimit()) return;
						}
						product = result;
						product.setCount(count);
						markDirty();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index==0) return jars;
		if (index==1) return product;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return getStackInSlot(index).splitStack(count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack res = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.EMPTY.copy());
		return res;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index==0) jars=stack;
		else if (index==1) product=stack;
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
		return "";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	private static final int[] slots = {0,1};
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return slots;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index==1 && !product.isEmpty();
	}

}
