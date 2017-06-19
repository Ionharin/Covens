package zabi.minecraft.covens.common.ritual.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.ritual.Ritual;

public class RitualSandsTime extends Ritual {

	public RitualSandsTime(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
		this.setRegistryName(Reference.MID, "timeSand");
	}
	
	@Override
	public void onUpdate(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		/*if (!world.isRemote)*/ world.setWorldTime(world.getWorldTime()+5);
	}

}
