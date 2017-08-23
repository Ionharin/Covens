package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.potion.ModPotions;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class ModCreativeTabs extends CreativeTabs {
	
	private ItemStack icon;
	
	public ModCreativeTabs(String label) {
		super(Reference.MID+"."+label);
	}

	public static ModCreativeTabs herbs, machines, products, brews;
	
	public static void registerTabs() {
		Log.i("Creating tabs");
		herbs = new ModCreativeTabs("herbs");
		machines = new ModCreativeTabs("machines");
		products = new ModCreativeTabs("products");
		brews = new ModCreativeTabs("brews");
	}
	
	public static void registerIcons() {
		herbs.setTabIconItem(new ItemStack(ModItems.flowers));
		machines.setTabIconItem(new ItemStack(ModBlocks.chimney));
		products.setTabIconItem(new ItemStack(ModItems.misc));
		BrewData icon = new BrewData();
		icon.addEffectToBrew(new CovenPotionEffect(ModPotions.extinguish_fire, 1, 0));
		brews.setTabIconItem(ItemBrewBase.getBrewStackWithData(ModItems.brew_drinkable, icon));
	}

	@Override
	public ItemStack getTabIconItem() {
		return icon;
	}
	
	public void setTabIconItem(ItemStack stack) {
		icon = stack;
	}
	
}
