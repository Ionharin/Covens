package zabi.minecraft.covens.common.item;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ItemWitchRobes extends ItemArmor {

	private static final ArmorMaterial material = EnumHelper.addArmorMaterial("witch_robes", "unknown", 10, new int[]{2, 3, 4, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	
	public ItemWitchRobes(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {
		super(material, renderIndexIn, equipmentSlotIn);
		this.setRegistryName(Reference.MID, name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModCreativeTabs.products);
	}
	
}
