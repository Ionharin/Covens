package zabi.minecraft.covens.common.registries.ritual.rituals;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualConjurationWitch extends Ritual {

	public RitualConjurationWitch(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
	}
	
	@Override
	public void onFinish(EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data) {
		if (!world.isRemote) {
			EntityWitch witch = new EntityWitch(world);
			witch.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), (float) (Math.random()*360), 0);
			witch.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(witch)), (IEntityLivingData)null);
			world.spawnEntity(witch);
			if (Math.random()<0.1) witch.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 6000, 2, false, false));
		}
	}

}
