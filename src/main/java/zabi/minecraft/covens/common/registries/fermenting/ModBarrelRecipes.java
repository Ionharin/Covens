package zabi.minecraft.covens.common.registries.fermenting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.fermenting.recipes.PotionExplosion;
import zabi.minecraft.covens.common.registries.fermenting.recipes.SlimeRecipe;

@Mod.EventBusSubscriber
public class ModBarrelRecipes {
	
	public static BarrelRecipe slime;
	public static BarrelRecipe potionDetonation;

	public static void registerAll() {
		Log.i("Creating barrel recipes");
		slime = new SlimeRecipe(new ItemStack(Items.SLIME_BALL), 1200, 0);
		slime.setRegistryName(Reference.MID, "slime");
		potionDetonation = new PotionExplosion(ItemStack.EMPTY, ItemStack.EMPTY, 100, 2);
		potionDetonation.setRegistryName(Reference.MID, "detonation");
	}
	
	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<BarrelRecipe> evt) {
		Log.i("Registering barrel recipes");
		evt.getRegistry().registerAll(
				slime, potionDetonation
		);
	}
}
