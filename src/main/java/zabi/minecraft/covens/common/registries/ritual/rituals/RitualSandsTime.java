package zabi.minecraft.covens.common.registries.ritual.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.capability.IRitualHandler;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class RitualSandsTime extends Ritual {

	public RitualSandsTime(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onUpdate(EntityPlayer player, IRitualHandler tile, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		/*if (!world.isRemote)*/ world.setWorldTime(world.getWorldTime()+5);
	}

}
