package zabi.minecraft.covens.common.potion.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import zabi.minecraft.covens.common.potion.ModPotion;

public class PotionSkinRotting extends ModPotion {

	public PotionSkinRotting(int liquidColorIn, String name) {
		super(true, liquidColorIn, name);
		this.setIconIndex(1, 0);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration%600==0;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if (entity.getCreatureAttribute()!=EnumCreatureAttribute.UNDEAD) {
			entity.attackEntityFrom(DamageSource.MAGIC, 0.5f + (float) amplifier / 4f);
		}
	}
	
}
