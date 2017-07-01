package zabi.minecraft.covens.common.registries.brewing;

import net.minecraft.item.ItemStack;

public class CovensBrewIngredientRegistry {
	
	public static boolean isNewPotionInitializer(ItemStack is) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return true;
		}
		return false;
	}
	
	public static BrewIngredient getPotion(ItemStack is) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return bi;
		}
		return null;
	}
	
	public static int getDuration(ItemStack is, boolean getOpposite) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return getOpposite?bi.getDurationOpposite():bi.getDuration();
		}
		return 0;
	}
}
