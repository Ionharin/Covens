package zabi.minecraft.covens.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemBrewDrinkable extends ItemBrewBase {
	
	public ItemBrewDrinkable() {
		this.setRegistryName(Reference.MID, "brew_drinkable");
		this.setUnlocalizedName("brew_drinkable");
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		if (player.getHeldItem(hand).getMetadata()==1) return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (stack.getMetadata()==1) {
			if (entityLiving instanceof EntityPlayer) {
				entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1200, 1, false, true));
			} else {
				entityLiving.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 1200, 1, false, true));
			}
			return ItemStack.EMPTY;
		}; 
		getEffectsFromStack(stack).stream()
			.filter(ce -> entityLiving.isPotionApplicable(ce.getPotionEffect()))
			.forEach(ce -> entityLiving.addPotionEffect(ce.getPotionEffect()));
		return ItemStack.EMPTY;
	}
	
	
	
}
