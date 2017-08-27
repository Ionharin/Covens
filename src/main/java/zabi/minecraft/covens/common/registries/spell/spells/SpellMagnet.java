package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellMagnet extends Spell {

	public SpellMagnet(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster) {
		if (rtrace.typeOfHit==Type.BLOCK && caster!=null) {
			caster.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(rtrace.hitVec, rtrace.hitVec.addVector(1, 1, 1)).grow(2)).forEach(ei -> {
				ei.setNoPickupDelay();
				if (caster instanceof EntityPlayer) {
					ei.onCollideWithPlayer((EntityPlayer) caster);
				} else {
					ei.setPositionAndUpdate(caster.posX, caster.posY, caster.posZ);
				}
			});
		}
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return true;
	}

}
