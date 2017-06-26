package zabi.minecraft.covens.common.crafting.brewing;

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
	
	public static int getDuration(ItemStack is) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return bi.getDuration();
		}
		return 0;
	}
}
