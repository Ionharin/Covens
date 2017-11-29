package zabi.minecraft.covens.common.tileentity;

import java.lang.reflect.Method;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.chimney.ChimneyRecipe;
import zabi.minecraft.covens.common.util.machines.AutomatableInventory;

public class TileEntityChimney extends TileEntityBaseTickable {
	
	AutomatableInventory handler = new AutomatableInventory(2) {
		
		@Override
		public void onMarkDirty() {
			markTileDirty();
		}
		
		@Override
		public boolean canMachineInsert(int slot, ItemStack stack) {
			return slot==0 && stack.getItem().equals(ModItems.misc) && stack.getMetadata()==0;
		}
		
		@Override
		public boolean canMachineExtract(int slot, ItemStack stack) {
			return slot!=0;
		}
	};
	
	private static Method canSmelt = ReflectionHelper.findMethod(TileEntityFurnace.class, "canSmelt", "func_145948_k", new Class<?>[0]);

	private int lastProgression = 0; //Prevent different tick times from yielding greater outputs
	
	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		if (tag.hasKey("jars")) handler.setInventorySlotContents(0, new ItemStack(tag.getCompoundTag("jars")));
		if (tag.hasKey("product")) handler.setInventorySlotContents(1, new ItemStack(tag.getCompoundTag("product")));
		lastProgression = tag.getInteger("p");
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
		tag.setInteger("p", lastProgression);
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
					if (time+1==timeMax && Math.random()<getChance() && lastProgression!=time) {
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
							handler.setInventorySlotContents(1, product);
							handler.extractItem(0, 1, false);
						}
					}
					lastProgression = time;
					markDirty();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public float getChance() {
		return 0.3F;
	}

	@Override
	protected void NBTSaveUpdate(NBTTagCompound tag) {
	}

	@Override
	protected void NBTLoadUpdate(NBTTagCompound tag) {
	}
	
	protected void markTileDirty() {
		markDirty();
	}
	
	public IInventory getInventory() {
		return handler;
	}

}
