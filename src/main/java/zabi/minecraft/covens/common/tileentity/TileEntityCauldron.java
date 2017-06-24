package zabi.minecraft.covens.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;

public class TileEntityCauldron extends TileEntityBase {
	
	private static final int BREW_TIME = 200;
	
	private int work = 0;
	private String recipe = "";
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>create();

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		work = tag.getInteger("work");
		NBTTagCompound inv = tag.getCompoundTag("inv");
		for (String s:inv.getKeySet()) {
			stacks.set(Integer.parseInt(s), new ItemStack(inv.getCompoundTag(s)));
		}
		recipe = tag.getString("recipe");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("work", work);
		NBTTagCompound inv = new NBTTagCompound();
		int i = 0;
		for (ItemStack s:stacks) {
			NBTTagCompound stackTag = new NBTTagCompound();
			if (!s.isEmpty()) s.writeToNBT(stackTag);
			inv.setTag(""+i, stackTag);
			i++;
		}
		tag.setTag("inv", inv);
		tag.setString("recipe", recipe);
	}

	@Override
	protected void tick() {
		if (!recipe.equals("") && work<BREW_TIME) work++;
	}	
	
	public boolean isPotionReady() {
		return work>=BREW_TIME;
	}
	
	public Potion getResult() {
		Potion pot = getPotionFromRecipe();
		work=0;
		recipe="";
		return pot;
	}

	private Potion getPotionFromRecipe() {
		return null;
	}

	//Returns the remaining stack
	public ItemStack dropItem(ItemStack stack) {
		if (!recipe.equals("")) return stack;
		if (!stack.isEmpty()) stacks.add(stack);
		recipe = getRecipeFor(stacks);
		return ItemStack.EMPTY.copy();
	}

	private String getRecipeFor(NonNullList<ItemStack> stacklist) {
		return "";
	}

}
