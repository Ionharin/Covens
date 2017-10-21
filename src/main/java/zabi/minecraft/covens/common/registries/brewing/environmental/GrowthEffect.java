package zabi.minecraft.covens.common.registries.brewing.environmental;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class GrowthEffect extends EnvironmentalPotionEffect {

	public GrowthEffect(Potion potion) {
		super(potion);
	}

	@Override
	public void splashedOn(World world, BlockPos pos, EntityLivingBase thrower, CovenPotionEffect data) {
if (!world.isRemote) {
			
			int radius = 1 + data.getPersistency();
			int fortune = data.getStrength();
			MutableBlockPos scanpos = new MutableBlockPos();
			
			for (int i=-radius;i<=radius;i++) {
				for (int j=-radius;j<=radius;j++) {
					for (int k=-radius;k<=radius;k++) {
						scanpos.setPos(pos.getX()+i, pos.getY()+j, pos.getZ()+k);
						if (applyBonemeal(world, pos, fortune)) {
							world.playEvent(2005, pos, 0);
		                }
					}
				}
			}
		}
	}
	
	public static boolean applyBonemeal(World worldIn, BlockPos target, int fortune) {
        IBlockState iblockstate = worldIn.getBlockState(target);
        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable)iblockstate.getBlock();
            for (int i=0;i<fortune;i++) if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
            	if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
            		igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
            	}
            	return true;
            }
        }
        return false;
    }

}
