package zabi.minecraft.covens.common.crafting.brewing;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;

@Mod.EventBusSubscriber
public class BrewIngredient extends IForgeRegistryEntry.Impl<BrewIngredient> {
	
	public static final IForgeRegistry<BrewIngredient> REGISTRY = new RegistryBuilder<BrewIngredient>().setName(new ResourceLocation(Reference.MID, "brew_ingredients")).setType(BrewIngredient.class).setIDRange(0, 200).create();

	private ItemStack ingredient = ItemStack.EMPTY;
	private Potion result = null;
	private boolean checkMeta = false, checkNbt = false;
	private int baseDuration = 0;
	
	public BrewIngredient(ItemStack in, Potion out, boolean metadataSensible, boolean NBTSensible, int duration) {
		ingredient = in;
		result = out;
		checkMeta=metadataSensible;
		checkNbt=NBTSensible;
		baseDuration = duration;
	}
	
	public BrewIngredient(Item in, Potion out, int duration) {
		this(new ItemStack(in),out,false,false, duration);
	}
	
	public boolean isValid(ItemStack test) {
		return ingredient.getItem().equals(test.getItem()) &&
				(!checkMeta || ingredient.getMetadata()==test.getMetadata()) &&
				(!checkNbt || ItemStack.areItemStackTagsEqual(test, ingredient));
	}
	
	public Potion getResult() {
		return result;
	}
	
	public int getDuration() {
		return baseDuration;
	}
	
	@SubscribeEvent
	public static void registerBrewIngredients(RegistryEvent.Register<BrewIngredient> evt) {
		
		IForgeRegistry<BrewIngredient> reg = evt.getRegistry();
		
		BrewIngredient healingPot = new BrewIngredient(Items.GOLDEN_APPLE, MobEffects.REGENERATION, PotionTypes.REGENERATION.getEffects().get(0).getDuration());
		healingPot.setRegistryName(Reference.MID, "healing");

		reg.registerAll(healingPot);
	}
	
}
