package zabi.minecraft.covens.common.potion.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import zabi.minecraft.covens.common.potion.ModPotion;

public class PotionExtinguishFire extends ModPotion {

	public PotionExtinguishFire(int liquidColorIn, String name) {
		super(false, liquidColorIn, name);
		this.setIconIndex(2, 0);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration%10==0;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if (entity.isBurning()) entity.extinguish();
		if (entity.isImmuneToFire()) entity.attackEntityFrom(DamageSource.MAGIC, 1f);
	}

}
