package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellMagnet extends Spell {

	public SpellMagnet(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster) {
		if (rtrace.typeOfHit==Type.ENTITY && rtrace.entityHit instanceof EntityItem) {
			EntityItem ei = (EntityItem) rtrace.entityHit;
			ei.setNoPickupDelay();
			if (caster instanceof EntityPlayer) {
				ei.onCollideWithPlayer((EntityPlayer) caster);
			} else if (caster!=null) {
				ei.setPositionAndUpdate(caster.posX, caster.posY, caster.posZ);
			}
		}
	}

}
