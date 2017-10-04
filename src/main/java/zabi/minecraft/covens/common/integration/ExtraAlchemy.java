package zabi.minecraft.covens.common.integration;

import net.minecraft.init.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;
import zabi.minecraft.covens.common.registries.brewing.ModBrewIngredients;

@ObjectHolder("extraalchemy")
public class ExtraAlchemy {
	
	@ObjectHolder("effect.gravity")
	private static final Potion gravity_potion = null;
	@ObjectHolder("effect.combustion")
	private static final Potion combustion_potion = null;
	@ObjectHolder("effect.freezing")
	private static final Potion freezing_potion = null;
	
	public void load() {
		
		if (gravity_potion!=null) {
			ModBrewIngredients.jump.setOpposite(gravity_potion, ModBrewIngredients.jump.getDuration());
		}
		if (combustion_potion!=null && freezing_potion!=null) {
			BrewIngredient freeze_combustion = new BrewIngredient(Ingredient.fromItem(Items.BLAZE_ROD), combustion_potion, freezing_potion, 2400, 1);
			BrewIngredient.REGISTRY.register(freeze_combustion);
		}
	}
}
