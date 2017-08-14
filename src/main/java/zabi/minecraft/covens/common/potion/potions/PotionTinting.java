package zabi.minecraft.covens.common.potion.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.network.messages.NotifyTint;
import zabi.minecraft.covens.common.potion.ModPotion;

public class PotionTinting extends ModPotion {
	public PotionTinting(int liquidColorIn, String name) {
		super(false, liquidColorIn, name);
		this.setIconIndex(0, 0);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		if (!entityLivingBaseIn.world.isRemote) Covens.network.sendToDimension(new NotifyTint(entityLivingBaseIn, -1), entityLivingBaseIn.world.provider.getDimension());
	}
}
