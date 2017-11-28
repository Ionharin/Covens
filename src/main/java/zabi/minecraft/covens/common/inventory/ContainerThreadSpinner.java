package zabi.minecraft.covens.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;
import zabi.minecraft.covens.common.util.machines.OutputSlot;

public class ContainerThreadSpinner extends ContainerBase {
	
	TileEntityThreadSpinner ts;
	public int data_a[] = {0};
	
	public ContainerThreadSpinner(InventoryPlayer pi, TileEntityThreadSpinner spinner) {
		ts = spinner;
		addSlotToContainer(new OutputSlot(spinner.getInventory(), 0, 116, 34));
		addSlotToContainer(new Slot(spinner.getInventory(), 1, 44, 25));
		addSlotToContainer(new Slot(spinner.getInventory(), 2, 62, 25));
		addSlotToContainer(new Slot(spinner.getInventory(), 3, 44, 43));
		addSlotToContainer(new Slot(spinner.getInventory(), 4, 62, 43));
		addPlayerSlots(pi, 8, 84);
	}
	
	@Override
	protected void updateField(int id, int data) {
		data_a[id] = data;
	}
	
	@Override
	protected int[] getFieldsToSync() {
		return data_a;
	}
	
	@Override
	public int getUpdatedFieldData(int id) {
		return ts.getTickProgress();
	}
	
}
