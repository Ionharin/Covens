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
	public int[] acid = {0};
	
	public ContainerSilverVat(InventoryPlayer pi, TileEntitySilverVat vat) {
		te = vat;
		acid[0] = te.getAcidLevel();
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
		return acid;
	}
	
	@Override
	public int getUpdatedFieldData(int id) {
		return te.getAcidLevel();
	}
	
	@Override
	protected void updateField(int id, int data) {
		acid[0]=data;
	}
	
}
