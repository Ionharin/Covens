package zabi.minecraft.covens.common.crafting.ritual.rituals;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.crafting.ritual.Ritual;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualEnderStream extends Ritual {

	public RitualEnderStream(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe) {
		for (ItemStack stack:recipe) {
			if (stack.getItem().equals(ModItems.waystone) && stack.getMetadata()==2) {
				NBTTagCompound dest = stack.getOrCreateSubCompound("pos");
				if (!dest.hasKey("dim") || dest.getInteger("dim")!=world.provider.getDimension()) return false;
			}
		}
		return super.isValid(player, world, pos, recipe);
	}
	
	@Override
	public void onUpdate(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		if (ticks%20!=0) return;
		NBTTagCompound dest = null;
		for (String iname:data.getCompoundTag("itemsUsed").getKeySet()) {
			ItemStack stack = new ItemStack(data.getCompoundTag("itemsUsed").getCompoundTag(iname));
			if (stack.getItem().equals(ModItems.waystone) && stack.getMetadata()==2) {
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null) {
			Log.w("Error in nbt ritual data: missing destination for enderstream");
			return;
		}
		final NBTTagCompound dst = dest;
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).expand(5, 3, 5).expand(-5, 0, -5)).stream()
			.forEach(elb -> {
				if (!elb.isSneaking()) elb.moveToBlockPosAndAngles(new BlockPos(x, y, z), (float) dst.getDouble("yaw"), (float) dst.getDouble("pitch"));
			});
	}
	
	@Override
	public void onLowPower(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		((TileEntityGlyph) world.getTileEntity(pos)).stopRitual(player);
	}

}
