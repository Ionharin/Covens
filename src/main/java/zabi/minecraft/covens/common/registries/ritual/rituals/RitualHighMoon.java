package zabi.minecraft.covens.common.registries.ritual.rituals;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualHighMoon extends Ritual {

	public RitualHighMoon(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarPower, int costPerTick) {
		super(input, output, timeInTicks, circles, altarPower, costPerTick);
	}

	@Override
	public void onFinish(EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound tag) {
		if (!world.isRemote) world.setWorldTime(17600);
	}
	
	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe) {
		return world.isDaytime();
	}

}
