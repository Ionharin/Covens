package zabi.minecraft.covens.common.registries.brewing.environmental;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class HarvestEffect extends EnvironmentalPotionEffect {
	
	public HarvestEffect(Potion potion) {
		super(potion);
	}

	@Override
	public void splashedOn(World world, BlockPos pos, EntityLivingBase thrower, CovenPotionEffect data) {
		if (!world.isRemote) {
			
			EntityPlayer p = null;
			if (thrower instanceof EntityPlayer) p = (EntityPlayer) thrower;
			else p = FakePlayerFactory.getMinecraft((WorldServer) world);
			
			int radius = 1 + data.getPersistency();
			int fortune = data.getStrength();
			ItemStack stick = new ItemStack(Items.STICK);
			stick.addEnchantment(Enchantments.FORTUNE, fortune);
			MutableBlockPos scanpos = new MutableBlockPos();
			
			for (int i=-radius;i<=radius;i++) {
				for (int j=-radius;j<=radius;j++) {
					for (int k=-radius;k<=radius;k++) {
						scanpos.setPos(pos.getX()+i, pos.getY()+j, pos.getZ()+k);
						IBlockState state = world.getBlockState(scanpos);
						if (shouldHarvest(world, state.getBlock(), state, scanpos)) {
							if (state.getBlock().removedByPlayer(state, world, scanpos, p, true)) {
								state.getBlock().harvestBlock(world, p, scanpos, state, null, stick);
							}
						}
					}
				}
			}
		}
	}

	private static boolean shouldHarvest(World world, Block block, IBlockState state, MutableBlockPos scanpos) {
		if ((block instanceof BlockCrops || block instanceof BlockCocoa) && !((IGrowable)block).canGrow(world, scanpos, state, false)) {
			return true;
		}
		return block instanceof BlockMushroom || block instanceof BlockPumpkin || block instanceof BlockMelon;
	}

}
