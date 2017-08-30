package zabi.minecraft.covens.common.registries.spell.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class SpellSelfHeal extends Spell {

	public SpellSelfHeal(int color, EnumSpellType type, String name, String mod_id) {
		super(color, type, name, mod_id);
	}

	@Override
	public void performEffect(RayTraceResult rtrace, EntityLivingBase caster, World world) {
		if (caster!=null) {
			caster.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 160, 0, false, true));
			caster.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 160, 0, false, false));
		}
	}

	@Override
	public boolean canBeUsed(World world, BlockPos pos, EntityLivingBase caster) {
		return caster!=null;
	}

}
