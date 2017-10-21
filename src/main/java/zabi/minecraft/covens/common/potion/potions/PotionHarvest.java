package zabi.minecraft.covens.common.potion.potions;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.potion.ModPotionInstant;

public class PotionHarvest extends ModPotionInstant {
	
	private static final HashMap<Class<?>,ItemStack> map = new HashMap<Class<?>,ItemStack>();
	static {
		map.put(EntityCreeper.class, new ItemStack(Items.GUNPOWDER));
	}

	public PotionHarvest(int liquidColorIn, String name) {
		super(false, liquidColorIn, name);
	}

	@Override
	protected void applyInstantEffect(EntityLivingBase e, int amp) {
		if (!e.getEntityWorld().isRemote) {
			if (map.containsKey(e.getClass())) {
				if (e.getRNG().nextDouble()<amp*0.05d) {
					EntityItem ei = new EntityItem(e.getEntityWorld(), e.posX, e.posY, e.posZ, map.get(e.getClass()).copy());
					ei.setDefaultPickupDelay();
					e.getEntityWorld().spawnEntity(ei);
				}
			}
		}
	}

}
