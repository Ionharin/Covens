package zabi.minecraft.covens.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class PotionDisrobing extends ModPotionInstant {

	protected PotionDisrobing(int liquidColorIn, String name) {
		super(true, liquidColorIn, name);
	}

	@Override
	protected void applyInstantEffect(EntityLivingBase e, int amp) {
		if (!e.world.isRemote) {
			int i = e.getRNG().nextInt(4+16/(amp+1));
			switch (i) {
			case 0:
				spawnItem(e,e.getItemStackFromSlot(EntityEquipmentSlot.FEET));
				e.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
				break;
			case 1:
				spawnItem(e,e.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
				e.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
				break;
			case 2:
				spawnItem(e,e.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
				e.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
				break;
			case 3:
				spawnItem(e,e.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
				e.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
				break;
			}
		}
	}
	
	private static void spawnItem(EntityLivingBase e, ItemStack is) {
		EntityItem ei = new EntityItem(e.world, e.posX, e.posY, e.posZ, is);
		ei.setPickupDelay(100);
		e.world.spawnEntity(ei);
	}
	

}
