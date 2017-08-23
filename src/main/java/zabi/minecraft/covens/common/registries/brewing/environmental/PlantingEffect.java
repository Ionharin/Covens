package zabi.minecraft.covens.common.registries.brewing.environmental;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemSeeds;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class PlantingEffect extends EnvironmentalPotionEffect {

	public PlantingEffect(Potion potion) {
		super(potion);
	}

	@Override
	public void splashedOn(World world, BlockPos pos, EntityLivingBase thrower, CovenPotionEffect data) {
		if (!world.isRemote) {
			AxisAlignedBB area = new AxisAlignedBB(pos).expand((data.getPersistency()+1)*2, 2, (data.getPersistency()+1)*2).expand(-(data.getPersistency()+1)*2, -2, -(data.getPersistency()+1)*2);
			world.getEntitiesWithinAABB(EntityItem.class, area, i -> (i.getItem().getItem() instanceof ItemSeeds))
			.forEach(seed -> {
				MutableBlockPos search = new MutableBlockPos();
				for (int dx = 0; dx<5; dx++) for (int dz = 0; dz<5; dz++) if (seed.getItem().getCount()>0) {
					search.setPos(pos.getX()+dx-2, pos.getY(), pos.getZ()+dz-2);
					if (world.isAirBlock(search.up()) && world.getBlockState(search).getBlock().canSustainPlant(world.getBlockState(search), world, search, EnumFacing.UP, (IPlantable) seed.getItem().getItem())) {
						world.setBlockState(search.up(), ((IPlantable)seed.getItem().getItem()).getPlant(world, search.up()), 3);
						seed.getItem().setCount(seed.getItem().getCount()-1);
						if (seed.getItem().getCount()<=0) {
							seed.setDead();
							break;
						}
					}
				}
			});
		}
	}

}
