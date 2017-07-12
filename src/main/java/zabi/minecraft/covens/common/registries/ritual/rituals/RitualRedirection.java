package zabi.minecraft.covens.common.registries.ritual.rituals;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class RitualRedirection extends Ritual {

	public static final int TP_SENSITIVITY = 4;
	
	public RitualRedirection(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
		this.setRegistryName(Reference.MID, "redirection");
	}
	
	@Override
	public void onUpdate(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		NBTTagCompound dest = null;
		for (String iname:data.getCompoundTag("itemsUsed").getKeySet()) {
			ItemStack stack = new ItemStack(data.getCompoundTag("itemsUsed").getCompoundTag(iname));
			if (stack.getItem().equals(ModItems.cardinal_stone) && stack.getMetadata()==2) {
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null || !dest.hasKey("x")) {
			Log.w("Error in nbt ritual data: missing destination for redirection");
			return;
		}
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(20)).stream()
			.filter(e -> e.getDistanceSq(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ)>TP_SENSITIVITY)
			.forEach(e -> e.attemptTeleport(x, y, z));
	}
	
	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe) {
		NBTTagCompound dest = null;
		for (ItemStack stack:recipe) {
			if (stack.getItem().equals(ModItems.cardinal_stone) && stack.getMetadata()==2) {
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null || !dest.hasKey("x")) {
			return false;
		}
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		if (Math.abs(x-pos.getX())<30 && Math.abs(y - pos.getY())<30 && Math.abs(z - pos.getZ())<30) {
			return false; //Destination is inside
		}
		return true;
	}

}
