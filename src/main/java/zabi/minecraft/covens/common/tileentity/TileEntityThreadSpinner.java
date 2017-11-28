package zabi.minecraft.covens.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.registries.threads.SpinningThreadRecipe;
import zabi.minecraft.covens.common.util.machines.AutomatableInventory;

public class TileEntityThreadSpinner extends TileEntityBaseTickable implements IAltarUser {
	
	public static final int MAX_TICKS = 200;
	public static final int POWER_PER_TICK = 6;
	
	private int tickProcessed = 0;
	private String loadedRecipe = null;
	private TileEntityAltar te = null;
	private AutomatableInventory inv = new AutomatableInventory(5) {
		
		@Override
		public void onMarkDirty() {
			checkRecipe();
			markTile();
		}
		
		@Override
		public boolean canMachineInsert(int slot, ItemStack stack) {
			return slot != 0 && getStackInSlot(slot).isEmpty();
		}
		
		@Override
		public boolean canMachineExtract(int slot, ItemStack stack) {
			return slot==0;
		}
	};

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		inv.loadFromNBT(tag.getCompoundTag("inv"));
		if (tag.hasKey("recipe")) loadedRecipe = tag.getString("recipe");
		else loadedRecipe = null;
		tickProcessed = tag.getInteger("ticks");
	}

	protected void markTile() {
		markDirty();
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setTag("inv", inv.saveToNbt());
		if (loadedRecipe!=null) tag.setString("recipe", loadedRecipe);
		tag.setInteger("ticks", tickProcessed);
	}

	@Override
	protected void tick() {
		if (loadedRecipe!=null && canStackResults()) {
			if (te==null || te.isInvalid()) te = TileEntityAltar.getClosest(getPos(), getWorld());
			if (te==null || te.isInvalid()) {
				loadedRecipe = null;
				return;
			}
			if (te.consumePower(POWER_PER_TICK, false)) {
				tickProcessed++;
				if (tickProcessed>=MAX_TICKS) {
					ItemStack result = SpinningThreadRecipe.REGISTRY.getValue(new ResourceLocation(loadedRecipe)).getResult();
					if (inv.getStackInSlot(0).isEmpty()) inv.setInventorySlotContents(0, result);
					else {
						inv.getStackInSlot(0).setCount(inv.getStackInSlot(0).getCount()+result.getCount());
					}
					for (int i=1; i<5; i++) inv.decrStackSize(i, 1);
				}
				inv.markDirty();
				markDirty();
			}
		} else {
			tickProcessed=0;
			markDirty();
		}
	}
	
	private boolean canStackResults() {
		if (inv.getStackInSlot(0).isEmpty()) return true;
		ItemStack recipeResult = SpinningThreadRecipe.REGISTRY.getValue(new ResourceLocation(loadedRecipe)).getResult();
		if (ItemStack.areItemStacksEqual(inv.getStackInSlot(0), recipeResult)) {
			int sum = inv.getStackInSlot(0).getCount() + recipeResult.getCount();
			return sum<=recipeResult.getMaxStackSize();
		}
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inv;
		return super.getCapability(capability, facing);
	}

	private void checkRecipe() {
		SpinningThreadRecipe recipe = SpinningThreadRecipe.getRecipe(NonNullList.from(ItemStack.EMPTY, new ItemStack[] {
				inv.getStackInSlot(1), inv.getStackInSlot(2), inv.getStackInSlot(3), inv.getStackInSlot(4)
		}));
		if (recipe!=null) {
			loadedRecipe = recipe.getRegistryName().toString();
		} else {
			loadedRecipe = null;
		}
	}

	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}
	
	public AutomatableInventory getInventory() {
		return inv;
	}
	
	public int getTickProgress() {
		return tickProcessed;
	}
	
}
