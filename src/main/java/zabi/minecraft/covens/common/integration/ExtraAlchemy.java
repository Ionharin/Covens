package zabi.minecraft.covens.common.integration;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import zabi.minecraft.covens.common.item.ModItems;
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
	@ObjectHolder("effect.beheading")
	private static final Potion beheading_potion = null;
	@ObjectHolder("effect.learining")
	private static final Potion learning_potion = null;
	@ObjectHolder("effect.reincarnation")
	private static final Potion reincarnation_potion = null;
	@ObjectHolder("effect.magnetism")
	private static final Potion magnetism_potion = null;
	
	public void load() {
		
		if (gravity_potion!=null) {
			ModBrewIngredients.jump.setOpposite(gravity_potion, ModBrewIngredients.jump.getDuration());
		}
		if (combustion_potion!=null && freezing_potion!=null) {
			BrewIngredient pot = new BrewIngredient(Ingredient.fromItem(Items.BLAZE_ROD), combustion_potion, freezing_potion, 2400, 1);
			BrewIngredient.REGISTRY.register(pot);
		}
		if (beheading_potion!=null) {
			BrewIngredient pot = new BrewIngredient(Ingredient.fromStacks(new ItemStack(Items.DIAMOND_AXE,1,0)), beheading_potion, 100);
			BrewIngredient.REGISTRY.register(pot);
		}
		if (learning_potion!=null) {
			BrewIngredient pot = new BrewIngredient(Ingredient.fromItem(Item.getItemFromBlock(Blocks.LAPIS_BLOCK)), learning_potion, 4800);
			BrewIngredient.REGISTRY.register(pot);
		}
		if (reincarnation_potion!=null) {
			BrewIngredient pot = new BrewIngredient(Ingredient.fromStacks(new ItemStack(ModItems.misc,1,14)), reincarnation_potion, 4800);
			BrewIngredient.REGISTRY.register(pot);
		}
		if (magnetism_potion!=null) {
			BrewIngredient pot = new BrewIngredient(Ingredient.fromItems(Items.IRON_INGOT), magnetism_potion, 4800);
			BrewIngredient.REGISTRY.register(pot);
		}
		
	}
}
