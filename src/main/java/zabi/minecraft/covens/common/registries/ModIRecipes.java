package zabi.minecraft.covens.common.registries;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.crafting.RecipeAddSpellToGrimoire;

@Mod.EventBusSubscriber
public class ModIRecipes {
	public static IRecipe spellToGrimoire;
	
	public static void registerAll() {
		MinecraftForge.EVENT_BUS.register(new ModIRecipes());
		Log.i("Creating special recipe handlers");
		spellToGrimoire = new RecipeAddSpellToGrimoire();
		spellToGrimoire.setRegistryName(new ResourceLocation(Reference.MID, "spell_to_grimoire"));
	}
	
	@SubscribeEvent
	public void onRegistration(RegistryEvent.Register<IRecipe> evt) {
		Log.i("Registering special recipe handlers");
		evt.getRegistry().registerAll(
				spellToGrimoire
		);
	}
	
}
