package zabi.minecraft.covens.common.crafting.ritual.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.crafting.ritual.Ritual;
import zabi.minecraft.covens.common.lib.Reference;

public class RitualHighMoon extends Ritual {

	public RitualHighMoon(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarPower, int costPerTick) {
		super(input, output, timeInTicks, circles, altarPower, costPerTick);
		this.setRegistryName(new ResourceLocation(Reference.MID, "HighMoon"));
	}

	@Override
	public void onFinish(EntityPlayer player, World world, BlockPos pos, NBTTagCompound tag) {
		if (!world.isRemote) world.setWorldTime(17600);
	}
	
	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos) {
		return world.isDaytime();
	}

}
