package zabi.minecraft.covens.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.registries.fermenting.BarrelRecipe;

public class TileEntityBarrel extends TileEntityBaseTickable implements IAltarUser {
	
	FluidTank internalTank = new FluidTank(Fluid.BUCKET_VOLUME) {
		protected void onContentsChanged() {
			if (this.getFluidAmount()==0 || this.getFluidAmount()==Fluid.BUCKET_VOLUME) checkRecipe();
		};
	};
	NonNullList<ItemStack> input = NonNullList.create();
	int brewingTime = 0;
	int barrelType = 0;
	ItemStack result = ItemStack.EMPTY, retrivial = ItemStack.EMPTY;
	boolean hasValidRecipe = false;
	BarrelRecipe currentRecipe = null; //cached, not stored in nbt. Fetch if null
	TileEntityAltar te = null;	//cached

	public TileEntityBarrel() {
		internalTank.setTileEntity(this);
	}
	
	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		brewingTime = tag.getInteger("time");
		result = new ItemStack(tag.getCompoundTag("out"));
		NBTTagCompound in = tag.getCompoundTag("input");
		for (String s:in.getKeySet()) {
			input.add(new ItemStack(in.getCompoundTag(s)));
		}
		hasValidRecipe = tag.getBoolean("hasRecipe");
		barrelType = tag.getInteger("type");
		internalTank = internalTank.readFromNBT(tag.getCompoundTag("fluid"));
		retrivial = new ItemStack(tag.getCompoundTag("retrivial"));
		internalTank.setCanDrain(!hasValidRecipe);
		internalTank.setCanFill(!hasValidRecipe);
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("time", brewingTime);
		NBTTagCompound out = new NBTTagCompound();
		result.writeToNBT(out);
		tag.setTag("out", out);
		int in=0;
		NBTTagCompound inList = new NBTTagCompound();
		for (ItemStack stack:input) {
			NBTTagCompound singleIngredient = new NBTTagCompound();
			stack.writeToNBT(singleIngredient);
			inList.setTag(""+in, singleIngredient);
			in++;
		}
		tag.setTag("input", inList);
		tag.setBoolean("hasRecipe", hasValidRecipe);
		tag.setInteger("type", barrelType);
		NBTTagCompound fluid = new NBTTagCompound();
		internalTank.writeToNBT(fluid);
		tag.setTag("fluid", fluid);
		NBTTagCompound rtr = new NBTTagCompound();
		retrivial.writeToNBT(rtr);
		tag.setTag("retrivial", rtr);
	}

	@Override
	protected void tick() {
		if (!world.isRemote && hasValidRecipe) {
			if (currentRecipe==null) {
				fetchRecipe();
				if (currentRecipe==null) {
					hasValidRecipe = false;
					return;
				}
			}
			markDirty();
			if (consumePower(currentRecipe.getPower())) {
				brewingTime++;
				if (brewingTime>=currentRecipe.getRequiredTime()) {
					result = currentRecipe.getResult();
					retrivial = currentRecipe.getRetrivialItem();
					currentRecipe.onFinish(world, input, pos, internalTank.drain(Fluid.BUCKET_VOLUME, false));
					input.clear();
					brewingTime=0;
					internalTank.setFluid(null);
					currentRecipe = null;
					hasValidRecipe = false;
					internalTank.setCanDrain(true);
					internalTank.setCanFill(true);
				}
				markDirty();
			}
		}
	}
	
	private void fetchRecipe() {
		currentRecipe = BarrelRecipe.getRecipe(getWorld(), input, getPos(), internalTank.getFluid());
		markDirty();
	}
	
	public void addItem(ItemStack item) {
		ItemStack add = item.copy().splitStack(1);
		input.add(add);
		markDirty();
		checkRecipe();
	}
	
	private void checkRecipe() {
		if (!result.isEmpty()) {
			hasValidRecipe = false;
			currentRecipe = null;
			markDirty();
			return;
		}
		fetchRecipe();
		this.hasValidRecipe = currentRecipe!=null;
		if (hasValidRecipe) Log.d("Found recipe: "+currentRecipe.getRegistryName());
		internalTank.setCanDrain(!hasValidRecipe);
		internalTank.setCanFill(!hasValidRecipe);
		markDirty();
	}
	
	public ItemStack getRequiredStackToRetrieve() {
		return retrivial;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) internalTank;
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

	public ItemStack popLastInsertedItem() {
		if (input.size()==0) return ItemStack.EMPTY;
		ItemStack res = input.get(input.size()-1);
		input.remove(res);
		markDirty();
		checkRecipe();
		return res;
	}
	
	public boolean hasRecipe() {
		return hasValidRecipe;
	}
	
	public boolean hasResult() {
		return !result.isEmpty();
	}
	
	public ItemStack popResult() {
		ItemStack res = result;
		result = ItemStack.EMPTY;
		hasValidRecipe = false;
		markDirty();
		return res;
	}

	public NonNullList<ItemStack> getIngredients() {
		return input;
	}

	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}
	
}
