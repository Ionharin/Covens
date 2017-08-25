package zabi.minecraft.covens.common.registries.threads;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;

public class SpinningThreadRecipe extends IForgeRegistryEntry.Impl<SpinningThreadRecipe> {
	
	public static final IForgeRegistry<SpinningThreadRecipe> REGISTRY = new RegistryBuilder<SpinningThreadRecipe>().setName(new ResourceLocation(Reference.MID, "thread_spinning")).setType(SpinningThreadRecipe.class).setIDRange(0, 200).create();
	
	final ItemStack output;
	final Ingredient[] inputs;
	
	public SpinningThreadRecipe(ItemStack output, Ingredient... inputs) {
		this.output=output;
		this.inputs=inputs;
	}
	
	public SpinningThreadRecipe(String modId, String regName, ItemStack output, Ingredient... inputs) {
		this(output, inputs);
		this.setRegistryName(modId, regName);
	}
	
	public ItemStack getResult() {
		return output.copy();
	}
	
	public boolean matches(NonNullList<ItemStack> list) {
		boolean[] found = new boolean[inputs.length];
		for (int i=0;i<inputs.length;i++) {
			Ingredient current = inputs[i];
			for (ItemStack is:list) {
				if (current.apply(is)) {
					found[i]=true;
					break;
				}
			}
		}
		for (boolean b:found) if (!b) return false;
		return true;
	}
	
	@Nullable
	public static SpinningThreadRecipe getRecipe(NonNullList<ItemStack> list) {
		for (SpinningThreadRecipe r:REGISTRY) if (r.matches(list)) return r;
		return null;
	}
	
}
