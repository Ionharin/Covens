package zabi.minecraft.covens.common.registries.chimney;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class ModChimneyRecipes {
	
	private static ChimneyRecipe default_recipe;
	private static ChimneyRecipe oak_spirit;
	private static ChimneyRecipe birch_soul;
	private static ChimneyRecipe acacia_essence;
	private static ChimneyRecipe spruce_heart;
	
	public static void registerAll() {
		Log.i("Creating chimney recipes");
		default_recipe = new ChimneyRecipe(ItemStack.EMPTY, new ItemStack(ModItems.misc, 1, 1), true, false);
		oak_spirit = new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,0), new ItemStack(ModItems.misc, 1, 2), true, false);
		birch_soul = new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,2), new ItemStack(ModItems.misc, 1, 3), true, false);
		acacia_essence = new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,4), new ItemStack(ModItems.misc, 1, 4), true, false);
		spruce_heart = new ChimneyRecipe(new ItemStack(Blocks.SAPLING,1,1), new ItemStack(ModItems.misc, 1, 5), true, false);
		ChimneyRecipe.setDefault(default_recipe);
		default_recipe.setRegistryName(Reference.MID, "cloudy_oil");
		oak_spirit.setRegistryName(Reference.MID, "oak_fume");
		birch_soul.setRegistryName(Reference.MID, "birch_soul");
		acacia_essence.setRegistryName(Reference.MID, "acacia_essence");
		spruce_heart.setRegistryName(Reference.MID, "spruce_heart");
	}
	
	@SubscribeEvent
	public static void registerChimneyRecipes(RegistryEvent.Register<ChimneyRecipe> evt) {
		Log.i("Registering chimney recipes");
		IForgeRegistry<ChimneyRecipe> reg = evt.getRegistry();
		reg.register(default_recipe);
		reg.register(oak_spirit);
		reg.register(spruce_heart);
		reg.register(acacia_essence);
		reg.register(birch_soul);
	}
}
