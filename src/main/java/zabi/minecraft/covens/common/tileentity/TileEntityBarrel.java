package zabi.minecraft.covens.common.tileentity;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zabi.minecraft.covens.common.block.BlockBarrel;

public class TileEntityBarrel extends TileEntityBase {
	
	private static final int REQUIRED_TICKS = 2400; //2 min
	
	ArrayList<ItemStack> input = new ArrayList<ItemStack>();
	int brewingTime = 0;
	int barrelType = 0;
	ItemStack result = ItemStack.EMPTY;
	boolean hasValidRecipe = false;

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
	}

	@Override
	protected void tick() {
		if (hasValidRecipe) {
			brewingTime++;
			if (brewingTime>=REQUIRED_TICKS) {
				//TODO
			}
		}
	}
	
	public void setType(BlockBarrel.WoodType type) {
		barrelType = type.ordinal();
	}
	
	public void setType(int type) {
		barrelType = type;
	}
	
	public BlockBarrel.WoodType getType() {
		return BlockBarrel.WoodType.values()[barrelType];
	}

}
