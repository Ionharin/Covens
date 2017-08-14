package zabi.minecraft.covens.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemBrewSplash extends ItemBrewBase {
	
	public ItemBrewSplash() {
		this.setRegistryName(Reference.MID, "brew_splash");
		this.setUnlocalizedName("brew_splash");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
