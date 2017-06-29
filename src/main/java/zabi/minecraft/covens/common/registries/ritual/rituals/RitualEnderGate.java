package zabi.minecraft.covens.common.registries.ritual.rituals;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ItemEgressStone;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class RitualEnderGate extends Ritual {

	public RitualEnderGate(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onFinish(EntityPlayer player, World world, BlockPos pos, NBTTagCompound data) {
		NBTTagCompound dest = null;
		ItemStack stone = null;
		for (String iname:data.getCompoundTag("itemsUsed").getKeySet()) {
			ItemStack stack = new ItemStack(data.getCompoundTag("itemsUsed").getCompoundTag(iname));
			if (stack.getItem().equals(ModItems.waystone) && stack.getMetadata()==2) {
				stone = stack.copy();
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null) {
			Log.w("Error in nbt ritual data: missing destination for endergate");
			return;
		}
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).expand(5, 3, 5).expand(-5, 0, -5)).stream()
			.forEach(elb -> {
				elb.moveToBlockPosAndAngles(new BlockPos(x, y, z), elb.rotationYaw, elb.rotationPitch);
			});
		if (!world.isRemote) {
			if (!stone.getTagCompound().hasKey("uses")) stone.getTagCompound().setInteger("uses", 1);
			else stone.getTagCompound().setInteger("uses", stone.getTagCompound().getInteger("uses")+1);
			if (stone.getTagCompound().getInteger("uses")<ItemEgressStone.MAX_USES) {
				EntityItem ei = new EntityItem(world, x, y, z, stone);
				world.spawnEntity(ei);
			}
		}
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

}
