package zabi.minecraft.covens.common.registries.ritual.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualIdentification extends Ritual {

	public RitualIdentification(NonNullList<ItemStack> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onFinish(EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data) {
		NBTTagCompound bound = null;
		for (String iname:data.getCompoundTag("itemsUsed").getKeySet()) {
			ItemStack stack = new ItemStack(data.getCompoundTag("itemsUsed").getCompoundTag(iname));
			if (stack.getItem().equals(ModItems.soulstring) && stack.getMetadata()==1) {
				bound = stack.getOrCreateSubCompound("boundData");
				break;
			}
		}
		if (bound==null || !bound.hasKey("uid")) {
			Log.w("Error in nbt ritual data: missing entity for identification");
			return;
		}
		tile.addEntityUUIDToList(bound.getString("uid"), bound.getString("name"));
	}

}
