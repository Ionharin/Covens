package zabi.minecraft.covens.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemEerieSeeds extends Item {
	public ItemEerieSeeds() {
		this.setCreativeTab(ModCreativeTabs.products);
		this.setUnlocalizedName("eerie_seeds");
		this.setRegistryName(Reference.MID, "eerie_seeds");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.getBlockState(pos).getBlock().equals(Blocks.GRASS)) {
			worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1, 1, false);
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
