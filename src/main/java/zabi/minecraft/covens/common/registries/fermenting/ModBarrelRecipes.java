package zabi.minecraft.covens.common.registries.fermenting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.fermenting.recipes.SlimeRecipe;

@Mod.EventBusSubscriber
public class ModBarrelRecipes {
	
	public static BarrelRecipe slime;

	public static void registerAll() {
		slime = new SlimeRecipe(new ItemStack(Items.SLIME_BALL), 10, 0);
		slime.setRegistryName(Reference.MID, "slime");
	}
	
	@SubscribeEvent
	public static void onRegister(RegistryEvent.Register<BarrelRecipe> evt) {
		evt.getRegistry().registerAll(
				slime
		);
	}
}
