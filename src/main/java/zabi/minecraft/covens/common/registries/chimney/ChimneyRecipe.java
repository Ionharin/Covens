package zabi.minecraft.covens.common.registries.chimney;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;

public class ChimneyRecipe extends IForgeRegistryEntry.Impl<ChimneyRecipe> {
	
	public static final IForgeRegistry<ChimneyRecipe> REGISTRY = new RegistryBuilder<ChimneyRecipe>().setName(new ResourceLocation(Reference.MID, "chimney")).setType(ChimneyRecipe.class).setIDRange(0, 200).create();
	
	private ItemStack in, out;
	private boolean meta, nbt;
	public static ChimneyRecipe defaultReicpe = null;
	
	public ChimneyRecipe(@Nonnull ItemStack in, @Nonnull ItemStack out, boolean matchMetadata, boolean matchNBT) {
		this.in=in.copy();
		this.out=out.copy();
		meta=matchMetadata;
		nbt=matchNBT;
	}
	
	public boolean isValidIngredient(@Nonnull ItemStack ingredient) {
		boolean itemFlag = in.getItem().equals(ingredient.getItem());
		boolean metaFlag = !meta || in.getMetadata() == ingredient.getMetadata();
		boolean nbtFlag = !nbt || ItemStack.areItemStackTagsEqual(ingredient, in);
		return itemFlag && metaFlag && nbtFlag;
	}
	
	public ItemStack getOutput() {
		return out.copy();
	}
	
	public ItemStack getInput() {
		ItemStack res = new ItemStack(in.getItem(),1);
		if (meta) res.setItemDamage(in.getMetadata());
		else res.setItemDamage(-1);
		if (nbt) res.setTagCompound(in.getTagCompound().copy());
		return res;
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
