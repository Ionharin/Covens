package zabi.minecraft.covens.common.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ContainerBase extends Container {

	protected void addPlayerSlots(InventoryPlayer playerInventory, int x, int y) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
		}
	}

	protected void addPlayerSlots(InventoryPlayer playerInventory) {
		int x = 8;
		int y = 84;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	protected int[] getFieldsToSync() {
		return new int[0];
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.listeners.forEach(l -> sendChangesToListener(l));
	}
	
	private void sendChangesToListener(IContainerListener l) {
		for (int i=0; i<getFieldsToSync().length; i++) {
			l.sendWindowProperty(this, i, getUpdatedFieldData(i));
		}
	}
	
	public int getUpdatedFieldData(int id) {
		return 0;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		sendChangesToListener(listener);
	}
	
	@Override
	public void updateProgressBar(int id, int data) {
		super.updateProgressBar(id, data);
		updateField(id, data);
	}
	
	protected void updateField(int id, int data) {
		
	}
}