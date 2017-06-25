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
		
		BrewIngredient healing = new BrewIngredient(Items.GOLDEN_APPLE, MobEffects.REGENERATION, PotionTypes.REGENERATION.getEffects().get(0).getDuration());
		healing.setRegistryName(Reference.MID, "healing");
		
		BrewIngredient speedPot = new BrewIngredient(Items.SUGAR, MobEffects.SPEED, PotionTypes.SWIFTNESS.getEffects().get(0).getDuration());
		speedPot.setRegistryName(Reference.MID, "speed");
		
		BrewIngredient jump = new BrewIngredient(Items.RABBIT_FOOT, MobEffects.JUMP_BOOST, PotionTypes.LEAPING.getEffects().get(0).getDuration());
		jump.setRegistryName(Reference.MID, "jump");
		
		BrewIngredient fireRes = new BrewIngredient(Items.MAGMA_CREAM, MobEffects.FIRE_RESISTANCE, PotionTypes.FIRE_RESISTANCE.getEffects().get(0).getDuration());
		fireRes.setRegistryName(Reference.MID, "fire_resistance");
		
		BrewIngredient nightVis = new BrewIngredient(Items.GOLDEN_CARROT, MobEffects.NIGHT_VISION, PotionTypes.NIGHT_VISION.getEffects().get(0).getDuration());
		nightVis.setRegistryName(Reference.MID, "night_vision");
		
		reg.registerAll(healing, speedPot, jump, fireRes, nightVis);
	}
	
}
