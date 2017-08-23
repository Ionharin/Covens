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
import zabi.minecraft.covens.common.capability.CovensData;
import zabi.minecraft.covens.common.potion.ModPotions;
import zabi.minecraft.covens.common.registries.brewing.BrewData;

public class ItemBrewDrinkable extends ItemBrewBase {
	
	public ItemBrewDrinkable() {
		super("brew_drinkable");
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
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		BrewData data = BrewData.getDataFromStack(stack);
		if (data.isSpoiled()) {
			if (entityLiving instanceof EntityPlayer) {
				entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 1200, 1, false, true));
			}
			return ItemStack.EMPTY;
		}; 
		
		data.getEffects().stream()
			.filter(ce -> entityLiving.isPotionApplicable(ce.getPotionEffect()))
			.forEach(ce -> {
				PotionEffect pe = ce.getPotionEffect();
					entityLiving.addPotionEffect(pe);
					if (pe.getPotion().equals(ModPotions.tinting)) entityLiving.getCapability(CovensData.CAPABILITY, null).setTint(data.getColor());
				});
		return ItemStack.EMPTY;
	}
	
	
	
}
