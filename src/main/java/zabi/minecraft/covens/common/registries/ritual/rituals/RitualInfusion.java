package zabi.minecraft.covens.common.registries.ritual.rituals;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualInfusion extends Ritual {

	public RitualInfusion(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onFinish(EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data) {
		PlayerData cdata = player.getCapability(PlayerData.CAPABILITY, null);
		cdata.setInfusion(EnumInfusion.NETHER);
		cdata.setMaxPower(100);
		cdata.setPower(100);
	}

}
