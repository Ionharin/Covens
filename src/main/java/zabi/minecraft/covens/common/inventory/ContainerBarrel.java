package zabi.minecraft.covens.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;

public class ContainerBarrel extends ContainerBase {
	
	public final TileEntityBarrel te;

	public ContainerBarrel(InventoryPlayer pi, TileEntityBarrel barrel) {
		addPlayerSlots(pi);
		te = barrel;
		addSlotToContainer(new SlotBarrelOutput(barrel, 0, 134, 43));
		for (int row=0; row<2; row++) for (int col=0; col<3; col++) {
			addSlotToContainer(new SlotBarrel(barrel, (row*3 + col) + 1, 62 + (18*col), 35 + (18*row)));
		}
	}
	
	
	public class SlotBarrel extends Slot {
		public SlotBarrel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		@Override
		public void onSlotChanged() {
			super.onSlotChanged();
			te.checkRecipe();
		}
	}
	
	public class SlotBarrelOutput extends SlotBarrel {

		public SlotBarrelOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
		
	}

}
