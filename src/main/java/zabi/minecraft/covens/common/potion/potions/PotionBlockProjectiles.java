package zabi.minecraft.covens.common.potion.potions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zabi.minecraft.covens.common.potion.ModPotion;

public class PotionBlockProjectiles extends ModPotion {
	
	Field inground = ReflectionHelper.findField(EntityArrow.class, "inGround", "field_70254_i");
	Method arrowstack = ReflectionHelper.findMethod(EntityArrow.class, "getArrowStack", "func_184550_j");
	
	public PotionBlockProjectiles(int liquidColorIn, String name) {
		super(false, liquidColorIn, name);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		amplifier++;
		entity.world.getEntitiesWithinAABB(EntityArrow.class, entity.getEntityBoundingBox().expand(amplifier/3, 1, amplifier/3).expand(-amplifier/3, -1, -amplifier/3))
			.parallelStream()
			.filter(a -> a.shootingEntity!=entity)
			.filter(a -> !isInGround(a))
			.forEach(a -> {
				if (!entity.world.isRemote && a.pickupStatus==PickupStatus.ALLOWED) {
					ItemStack arrow;
					try {
						arrow = (ItemStack) arrowstack.invoke(a);
						EntityItem ei = new EntityItem(entity.world, a.posX, a.posY, a.posZ, arrow);
						entity.world.spawnEntity(ei);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					
				}
				a.setDead();
			});
	}

	private boolean isInGround(EntityArrow a) {
		try {
			return (boolean) inground.get(a);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
