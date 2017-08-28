package zabi.minecraft.covens.common.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;
import zabi.minecraft.covens.common.registries.spell.Spell;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;

public class DispenseSpells implements IBehaviorDispenseItem {

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		Spell s = ItemSpellPage.getSpellFromItemStack(stack);
		if (s!=null) {
			EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
			Vec3d lookVect = new Vec3d(enumfacing.getDirectionVec());
			if (s.canBeUsed(source.getWorld(), source.getBlockPos().offset(enumfacing), null)) {
				if (s.getType()==EnumSpellType.INSTANT) s.performEffect(new RayTraceResult(Type.MISS, lookVect, EnumFacing.UP, source.getBlockPos()), null, source.getWorld());
				else {
					EntitySpellCarrier car = new EntitySpellCarrier(source.getWorld(), source.getBlockPos().getX()+1.5*lookVect.x+0.5, source.getBlockPos().getY()+0.5d+lookVect.y, source.getBlockPos().getZ()+1.5*lookVect.z+0.5);
					car.setSpell(s);
					car.setCaster(null);
					car.setHeadingFromThrower(car, 0, enumfacing.getHorizontalAngle(), 0, 1f, 0);
					source.getWorld().spawnEntity(car);
				}
				return ItemStack.EMPTY;
			}
		}
		return stack;
		
	}

}
