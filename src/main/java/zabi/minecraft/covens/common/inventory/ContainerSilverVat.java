package zabi.minecraft.covens.common.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import zabi.minecraft.covens.common.tileentity.TileEntitySilverVat;
import zabi.minecraft.covens.common.util.machines.FilteredSlot;
import zabi.minecraft.covens.common.util.machines.OutputSlot;

public class ContainerSilverVat extends ContainerBase {
	
	public static final OreIngredient goldOre = new OreIngredient("oreGold");
	
	TileEntitySilverVat te;
	public int[] data_a = {0, 0};
	
	public ContainerSilverVat(InventoryPlayer pi, TileEntitySilverVat vat) {
		te = vat;
		data_a[0] = te.getAcidLevel();
		data_a[1] = te.getInventory().getStackInSlot(4).getCount();
		IInventory vatInv = vat.getInventory();
		this.addSlotToContainer(new FilteredSlot(vatInv, 2, 79, 8, goldOre));
		this.addSlotToContainer(new FilteredSlot(vatInv, 4, 16, 20, Ingredient.fromItem(Items.GUNPOWDER)) {
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				if (vat.getAcidLevel()==0) vat.randomTick();
			}
		});
		this.addSlotToContainer(new OutputSlot(vatInv, 0, 79, 56));
		this.addSlotToContainer(new OutputSlot(vatInv, 1, 102, 49));
		this.addSlotToContainer(new OutputSlot(vatInv, 3, 56, 49));
		addPlayerSlots(pi, 8, 84);
	}
	
	@Override
	protected int[] getFieldsToSync() {
		return data_a;
	}
	
	@Override
	public int getUpdatedFieldData(int id) {
		return id==0?te.getAcidLevel():te.getInventory().getStackInSlot(4).getCount();
	}
	
	@Override
	protected void updateField(int id, int data) {
		data_a[id]=data;
		if (id==1 && !getSlot(1).getStack().isEmpty()) getSlot(1).getStack().setCount(data);
	}
	
}
