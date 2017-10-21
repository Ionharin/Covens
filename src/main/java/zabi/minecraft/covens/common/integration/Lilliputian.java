package zabi.minecraft.covens.common.integration;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;

@ObjectHolder("lilliputian")
public class Lilliputian {
	
	@ObjectHolder("shrinking")
	public static final Potion shrink = null;
	@ObjectHolder("growing")
	public static final Potion grow = null;
	
	public void load() {
		if (shrink!=null && grow!=null) {
			BrewIngredient growth = new BrewIngredient(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())), grow, shrink, 1200, 1200);
			BrewIngredient.REGISTRY.register(growth);
		}
	}
}
