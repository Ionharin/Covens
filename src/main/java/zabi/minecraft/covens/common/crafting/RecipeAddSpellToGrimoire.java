package zabi.minecraft.covens.common.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModItems;

public class RecipeAddSpellToGrimoire implements IRecipe {
	
	private ResourceLocation rl;

	@Override
	public IRecipe setRegistryName(ResourceLocation name) {
		rl=name;
		return this;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return rl;
	}

	@Override
	public Class<IRecipe> getRegistryType() {
		return IRecipe.class;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean grimoireFound = false, spellFound = true;
		int itemsFound = 0;
		for (int i=0;i<inv.getSizeInventory();i++) {
			ItemStack stack = inv.getStackInSlot(i); 
			if (stack.isEmpty()) continue;
			itemsFound++;
			if (stack.getItem()==ModItems.grimoire) grimoireFound=true;
			else if (stack.getItem()==ModItems.spell_page && stack.hasTagCompound() && stack.getTagCompound().hasKey("spell")) spellFound=true;
		}
		
		return itemsFound==2 && grimoireFound && spellFound;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		
		ItemStack grimoireStack = null, spellStack = null;
		
		for (int i=0;i<inv.getSizeInventory();i++) {
			ItemStack stack = inv.getStackInSlot(i); 
			if (stack.isEmpty()) continue;
			if (stack.getItem()==ModItems.grimoire) grimoireStack=stack;
			else if (stack.getItem()==ModItems.spell_page) spellStack=stack;
		}
		ItemStack newGrimoire = grimoireStack.copy();
		if (!newGrimoire.hasTagCompound() || !newGrimoire.getTagCompound().hasKey("selected")) {
			NBTTagCompound tag = newGrimoire.hasTagCompound()?newGrimoire.getTagCompound():new NBTTagCompound();
			tag.setInteger("storedSpells", 0);
			tag.setTag("spells", new NBTTagCompound());
			tag.setBoolean("creative", false);
			tag.setInteger("selected", -1);
			newGrimoire.setTagCompound(tag);
		}
		NBTTagCompound tag = newGrimoire.getTagCompound();
		NBTTagCompound spells = tag.getCompoundTag("spells");
		spells.setString("spell"+tag.getInteger("storedSpells"), spellStack.getTagCompound().getString("spell"));
		tag.setInteger("storedSpells", 1+tag.getInteger("storedSpells"));
		return newGrimoire;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width>1||height>1;
	}
	
	private static final ItemStack result = new ItemStack(ModItems.grimoire);

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
