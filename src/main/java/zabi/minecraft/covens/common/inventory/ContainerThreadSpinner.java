package zabi.minecraft.covens.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;

public class ContainerThreadSpinner extends ContainerBase {
	
	public ContainerThreadSpinner(InventoryPlayer pi, TileEntityThreadSpinner spinner) {
		addSlotToContainer(new OutputSlot(spinner, 0, 116, 34));
		addSlotToContainer(new InputSlot(spinner, 1, 44, 25));
		addSlotToContainer(new InputSlot(spinner, 2, 62, 25));
		addSlotToContainer(new InputSlot(spinner, 3, 44, 43));
		addSlotToContainer(new InputSlot(spinner, 4, 62, 43));
		addPlayerSlots(pi, 8, 84);
	}
	
	public static class InputSlot extends Slot {
		public InputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public int getSlotStackLimit() {
			return 1;
		}
		
	}
	
	public static class OutputSlot extends Slot {
		public OutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
