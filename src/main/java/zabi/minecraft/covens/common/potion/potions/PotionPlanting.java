package zabi.minecraft.covens.common.potion.potions;

import net.minecraft.entity.EntityLivingBase;
import zabi.minecraft.covens.common.potion.ModPotionInstant;

public class PotionPlanting extends ModPotionInstant {

	public PotionPlanting(int liquidColorIn, String name) {
		super(true, liquidColorIn, name);
	}

	@Override
	protected void applyInstantEffect(EntityLivingBase e, int amp) {
		if (e.world.isAirBlock(e.getPosition())) e.setPositionAndUpdate(e.posX, e.posY>3?e.posY-1:e.posY, e.posZ);
	}

}
