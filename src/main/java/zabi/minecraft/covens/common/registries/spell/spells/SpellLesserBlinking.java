package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellLesserBlinking extends Spell {

	public SpellLesserBlinking(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		BlockPos dest = new BlockPos(caster.getPositionVector().add(caster.getLookVec().scale(2).addVector(0, 1, 0)));
		if (!world.getBlockState(dest).causesSuffocation()) {
			caster.setPositionAndUpdate(dest.getX(), dest.getY(), dest.getZ());
		}
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return true;
	}

}
