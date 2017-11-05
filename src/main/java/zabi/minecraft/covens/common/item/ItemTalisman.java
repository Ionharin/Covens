package zabi.minecraft.covens.common.item;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemTalisman extends Item implements IBauble {

	private int baubleType = 3, enchantability = 0;
	EntityEquipmentSlot slot = null;
	
	public ItemTalisman(int type, int enchantability, String name, EntityEquipmentSlot equipmentSlotAlternative) {
		
		this.setCreativeTab(ModCreativeTabs.products);
		this.setUnlocalizedName(name);
		this.setRegistryName(Reference.MID, "talisman_"+name);
		slot = equipmentSlotAlternative;
		
		if ((type<0 || type>6) && Loader.isModLoaded("baubles")) {
			Log.w(this+" was given type ID "+type+", which does not correspond to any bauble type (0-6). Falling back to trinket (3)");
		} else {
			baubleType = type;
		}
		this.enchantability = enchantability;
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack) {
		return enchantability;
	}
	
	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return !EnchantmentHelper.hasBindingCurse(itemstack);
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}
	
	@Optional.Method(modid="baubles")
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.values()[baubleType];
	}
	
	@Override
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
		return Loader.isModLoaded("baubles")?null:slot;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
}
