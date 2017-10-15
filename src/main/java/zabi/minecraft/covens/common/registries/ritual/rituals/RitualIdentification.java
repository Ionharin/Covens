package zabi.minecraft.covens.common.registries.ritual.rituals;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.capability.IRitualHandler;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualIdentification extends Ritual {

	public RitualIdentification(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onFinish(EntityPlayer player, IRitualHandler tile, World world, BlockPos pos, NBTTagCompound data) {
		if (tile instanceof TileEntityGlyph) {
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
			((TileEntityGlyph)tile).addEntityUUIDToList(bound.getString("uid"), bound.getString("name"));
		}
	}
	
	@Override
	public boolean canBeUsedFromCandle() {
		return false;
	}

}
