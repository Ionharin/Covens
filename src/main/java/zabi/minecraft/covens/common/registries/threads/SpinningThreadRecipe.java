package zabi.minecraft.covens.common.registries.threads;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Log;
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
		ArrayList<ItemStack> comp = new ArrayList<ItemStack>(list);
		for (int i=0;i<inputs.length;i++) {
			Ingredient current = inputs[i];
			for (int j=0;j<comp.size();j++) {
				ItemStack is = comp.get(j);
				if (!is.isEmpty() && current.apply(is)) {
					found[i]=true;
					comp.set(j, ItemStack.EMPTY);
					break;
				}
			}
		}
		int c = 0;
		for (boolean b:found) {
			if (!b) {
				Log.d("Not "+this.getRegistryName().toString()+" because "+inputs[c]+" is missing");
				return false;
			}
			c++;
		}
		return true;
	}

	@Nullable
	public static SpinningThreadRecipe getRecipe(NonNullList<ItemStack> list) {
		Log.d("Scanning recipes");
		for (SpinningThreadRecipe r:REGISTRY) {
			if (r.matches(list)) {
				Log.d("found recipe");
				return r;
			}
		}
		return null;
	}
	
}
