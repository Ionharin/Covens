package zabi.minecraft.covens.common.enchantment;

import java.util.List;

import com.google.common.collect.Lists;

import zabi.minecraft.covens.common.lib.Reference;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import zabi.minecraft.covens.common.item.ItemTalisman;

public class EnchantmentTalismans extends Enchantment {

	public EnchantmentTalismans(Rarity rarity, String name) {
		super(rarity, EnumEnchantmentType.ALL, Loader.isModLoaded("baubles")?new EntityEquipmentSlot[0]:new EntityEquipmentSlot[] {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.OFFHAND});
		setName(name);
		setRegistryName(Reference.MID, name);
	}
	
	@Override
	public boolean canApply(ItemStack stack) {
		return stack.getItem() instanceof ItemTalisman;
	}
	
	@Override
	public List<ItemStack> getEntityEquipment(EntityLivingBase entityIn) {
		if (Loader.isModLoaded("baubles")) return getBaublesEquipment(entityIn);
		return super.getEntityEquipment(entityIn);
	}
	
	@Optional.Method(modid="baubles")
	public List<ItemStack> getBaublesEquipment(EntityLivingBase entityIn) {
		List<ItemStack> list = Lists.<ItemStack>newArrayList();

		if (entityIn instanceof EntityPlayer) {
			IBaublesItemHandler baubInv = BaublesApi.getBaublesHandler((EntityPlayer) entityIn);
			for (int i = 0; i < baubInv.getSlots(); i++) {
				ItemStack itemstack = baubInv.extractItem(i, 1, true);
				if (!itemstack.isEmpty()) {
					list.add(itemstack);
				}
			}
		}

		return list;
	}
	
}
