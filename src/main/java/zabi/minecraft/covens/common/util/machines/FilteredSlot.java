package zabi.minecraft.covens.common.util.machines;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FilteredSlot extends Slot {
	Ingredient ing;
	public FilteredSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Ingredient ing) {
		super(inventoryIn, index, xPosition, yPosition);
		this.ing = ing;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return ing.apply(stack);
	}
}