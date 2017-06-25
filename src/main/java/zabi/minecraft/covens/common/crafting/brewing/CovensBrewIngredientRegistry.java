package zabi.minecraft.covens.common.crafting.brewing;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class CovensBrewIngredientRegistry {
	
	public static boolean isNewPotionInitializer(ItemStack is) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return true;
		}
		return false;
	}
	
	public static Potion getPotion(ItemStack is) {
		for (BrewIngredient bi:BrewIngredient.REGISTRY) {
			if (bi.isValid(is)) return bi.getResult();
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
