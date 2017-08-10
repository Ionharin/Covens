package zabi.minecraft.covens.common.registries.fermenting.recipes;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;
import zabi.minecraft.covens.common.registries.fermenting.BarrelRecipe;

public class PotionExplosion extends BarrelRecipe {

	public PotionExplosion(ItemStack output, ItemStack retrivial, int ticks, int power) {
		super(output, retrivial, ticks, power);
	}

	@Override
	public boolean isValidRecipe(World world, NonNullList<ItemStack> stacks, BlockPos pos, FluidStack fluid) {
		boolean[] ing = new boolean[2];
		for (ItemStack stack:stacks) {
			if (stack.getItem().equals(Items.GUNPOWDER)) ing[0]=true;
			else if (stack.getItem().equals(ModItems.brew_splash)) {
				BrewData data = new BrewData(1);
				data.readFromNBT(stack.getOrCreateSubCompound("brewdata"));
				if (data.isSpoiled()) {
					Log.d("spoiled");
					return false;
				}
				ing[1]=true;
			} else {
				Log.d("unrecognized item: "+stack.getItem().getRegistryName());
				return false;
			}
		}
		boolean hasIngredients = ing[0] && ing[1];
		if (!hasIngredients) {
			Log.i("Missing ingredients");
			return false;
		}
		if (fluid==null || !fluid.getFluid().equals(FluidRegistry.LAVA)) {
			Log.i("Not lava");
			return false;
		}
		return true;
	}
	
	@Override
	public void onFinish(World world, NonNullList<ItemStack> stacks, BlockPos pos, FluidStack fluid) {
		if (!world.isRemote) for (ItemStack stack:stacks) {
			if (stack.getItem().equals(ModItems.brew_splash)) {
				BrewData data = new BrewData(1);
				data.readFromNBT(stack.getOrCreateSubCompound("brewdata"));
				detonate(world, pos, data);
			}
		}
	}

	private void detonate(World world, BlockPos pos, BrewData data) {
		Log.i("Detonating");
		EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(world, pos.getX(), pos.getY(), pos.getZ());
        entityareaeffectcloud.setRadiusOnUse(0);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(0);
        
        int persistency = 0;
        
        NonNullList<CovenPotionEffect> effects = data.getEffects();
        
        for (CovenPotionEffect pe:effects) {
        	entityareaeffectcloud.addEffect(pe.getPotionEffect());
        	persistency += pe.getPersistency();
        }
        
        persistency /= effects.size();
        persistency*=5;
        entityareaeffectcloud.setRadius(1+persistency);
        entityareaeffectcloud.setColor(data.getColor());
        entityareaeffectcloud.setDuration(80+persistency*60);
        world.spawnEntity(entityareaeffectcloud);
		world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3, true);
	}

}
