package zabi.minecraft.covens.common.registries.ritual.rituals;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.capability.IRitualHandler;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class RitualInfusion extends Ritual {
	
	private EnumInfusion infusion = null;

	public RitualInfusion(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick, @Nonnull EnumInfusion infusion) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
		this.infusion = infusion;
	}
	
	@Override
	public void onFinish(EntityPlayer player, IRitualHandler tile, World world, BlockPos pos, NBTTagCompound data) {
		if (player!=null) {
			PlayerData cdata = player.getCapability(PlayerData.CAPABILITY, null);
			cdata.setInfusion(infusion);
			cdata.setMaxPower(infusion.getPower());
			cdata.setPower(infusion.getPower());
			player.attackEntityFrom(DamageSource.MAGIC, (float) Integer.MAX_VALUE);
		}
	}
	
	@Override
	public boolean canBeUsedFromCandle() {
		return false;
	}
	

}
