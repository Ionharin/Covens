package zabi.minecraft.covens.common.crafting.chimney;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModChimneyRecipes {
	
	private static final ChimneyRecipe default_recipe = new ChimneyRecipe(ItemStack.EMPTY, new ItemStack(ModItems.misc, 1, 2), true, false);
	private static final ChimneyRecipe oak_spirit = new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,0), new ItemStack(ModItems.misc, 1, 2), true, false);
	
	public static void registerAll() {
		ChimneyRecipe.setDefault(default_recipe);
		default_recipe.setRegistryName(new ResourceLocation(Reference.MID, "cloudy_oil"));
		oak_spirit.setRegistryName(new ResourceLocation(Reference.MID, "oak_fume"));
	}
	
	@SubscribeEvent
	public static void registerChimneyRecipes(RegistryEvent.Register<ChimneyRecipe> evt) {
		Log.i("Registering chimney recipes for "+Reference.NAME);
		IForgeRegistry<ChimneyRecipe> reg = evt.getRegistry();
		reg.registerAll(default_recipe, oak_spirit);
	}
}
