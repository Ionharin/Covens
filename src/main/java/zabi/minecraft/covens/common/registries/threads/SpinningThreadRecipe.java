package zabi.minecraft.covens.common.registries.threads;

import java.util.ArrayList;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

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
	
	public Ingredient[] getInputs() {
		return inputs;
	}
	
	public boolean matches(NonNullList<ItemStack> list) {
		int nonEmpty = 0;
		for (ItemStack is:list) {
			if (is.getCount()>0) nonEmpty++;
//			else if (is.getCount()>1) {
//				Log.i("One stack was bigger than 1");
//				return false;
//			}
		}
		if (nonEmpty!=inputs.length) {
//			Log.i("recipe size mismatch: list "+list.size()+", input "+inputs.length);
			return false;
		}
		boolean[] found = new boolean[inputs.length];
		ArrayList<ItemStack> comp = new ArrayList<ItemStack>(list);
		for (int i=0;i<inputs.length;i++) {
			Ingredient current = inputs[i];
			for (int j=0;j<comp.size();j++) {
				ItemStack is = comp.get(j);
				if (current.apply(is)) {
					found[i]=true;
					comp.set(j, ItemStack.EMPTY);
					break;
				}
			}
		}
		for (boolean b:found) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	@Nullable
	public static SpinningThreadRecipe getRecipe(NonNullList<ItemStack> list) {
		for (SpinningThreadRecipe r:REGISTRY) {
			if (r.matches(list)) {
				return r;
			}
		}
		return null;
	}
	
}
