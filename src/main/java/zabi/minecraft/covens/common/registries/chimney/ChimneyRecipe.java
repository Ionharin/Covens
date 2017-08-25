package zabi.minecraft.covens.common.registries.chimney;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;

public class ChimneyRecipe extends IForgeRegistryEntry.Impl<ChimneyRecipe> {
	
	public static final IForgeRegistry<ChimneyRecipe> REGISTRY = new RegistryBuilder<ChimneyRecipe>().setName(new ResourceLocation(Reference.MID, "chimney")).setType(ChimneyRecipe.class).setIDRange(0, 200).create();
	
	private ItemStack out;
	private Ingredient in;
	public static ChimneyRecipe defaultReicpe = null;
	
	public ChimneyRecipe(@Nonnull Ingredient in, @Nonnull ItemStack out) {
		this.in=in;
		this.out=out.copy();
	}
	
	public boolean isValidIngredient(@Nonnull ItemStack ingredient) {
		return in.apply(ingredient);
	}
	
	public ItemStack getOutput() {
		return out.copy();
	}
	
	public ItemStack[] getInput() {
		return in.getMatchingStacks();
	}
	
	public static ChimneyRecipe getRecipeFor(ItemStack ingredient) {
		for (ChimneyRecipe cr:REGISTRY) {
			if (cr.isValidIngredient(ingredient)) return cr;
		}
		return defaultReicpe;
	}
	
	public static void setDefault(ChimneyRecipe r) {
		defaultReicpe = r;
	}
}
