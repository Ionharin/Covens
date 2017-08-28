package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellDestabilization extends Spell {

	public SpellDestabilization(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		world.newExplosion(caster, rtrace.hitVec.x, rtrace.hitVec.y, rtrace.hitVec.z, 0.3f, false, true);
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return true;
	}

}
