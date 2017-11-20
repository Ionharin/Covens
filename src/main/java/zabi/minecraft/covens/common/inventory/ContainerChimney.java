package zabi.minecraft.covens.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.tileentity.TileEntityChimney;
import zabi.minecraft.covens.common.util.machines.OutputSlot;

public class ContainerChimney extends ContainerBase {
	public ContainerChimney(InventoryPlayer pi, TileEntityChimney chim) {
		addSlotToContainer(new JarSlot(chim, 0, 51, 34));
		addSlotToContainer(new OutputSlot(chim, 1, 109, 34));
		addPlayerSlots(pi, 8, 84);
	}
	
	public static class JarSlot extends Slot {
		public JarSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return stack.getItem().equals(ModItems.misc) && stack.getMetadata() == 0;
		}
	}
	
}
