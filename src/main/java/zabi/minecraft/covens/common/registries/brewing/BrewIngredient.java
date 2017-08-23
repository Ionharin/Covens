package zabi.minecraft.covens.common.registries.brewing;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
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
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.potion.ModPotions;

@Mod.EventBusSubscriber
public class BrewIngredient extends IForgeRegistryEntry.Impl<BrewIngredient> {
	
	public static final IForgeRegistry<BrewIngredient> REGISTRY = new RegistryBuilder<BrewIngredient>().setName(new ResourceLocation(Reference.MID, "brew_ingredients")).setType(BrewIngredient.class).setIDRange(0, 200).create();

	private ItemStack ingredient = ItemStack.EMPTY;
	private Potion result = null, opposite = null;
	private boolean checkMeta = false, checkNbt = false;
	private int baseDuration = 0, oppositeDuration=0;
	
	public BrewIngredient(ItemStack in, Potion out, Potion opposite, boolean metadataSensible, boolean NBTSensible, int duration, int durationOpposite) {
		ingredient = in;
		result = out;
		checkMeta=metadataSensible;
		checkNbt=NBTSensible;
		baseDuration = duration;
		this.opposite=opposite;
		oppositeDuration = durationOpposite;
		this.setRegistryName(out.getRegistryName());
	}
	
	public BrewIngredient(Item in, Potion out, Potion opposite, int duration, int opp_duration) {
		this(new ItemStack(in),out,opposite, false,false, duration, opp_duration);
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
	
	public int getDurationOpposite() {
		return oppositeDuration;
	}
	
	@Nullable
	public Potion getOpposite() {
		return opposite;
	}
	
	public ItemStack getInput() {
		return ingredient.copy();
	}
	
	public static BrewIngredient healing, speedPot, jump, fireRes, nightVis, instaHealth, strength, invis, disrobing, tinting, 
		skin_rotting, extinguish_fire, planting;
	
	public static void registerAll() {
		Log.i("Creating brews");
		healing = new BrewIngredient(Items.GHAST_TEAR, MobEffects.REGENERATION, MobEffects.POISON, PotionTypes.REGENERATION.getEffects().get(0).getDuration(), PotionTypes.POISON.getEffects().get(0).getDuration());
		speedPot = new BrewIngredient(Items.SUGAR, MobEffects.SPEED, MobEffects.SLOWNESS, PotionTypes.SWIFTNESS.getEffects().get(0).getDuration(), PotionTypes.SLOWNESS.getEffects().get(0).getDuration());
		jump = new BrewIngredient(Items.RABBIT_FOOT, MobEffects.JUMP_BOOST, null, PotionTypes.LEAPING.getEffects().get(0).getDuration(),0);
		fireRes = new BrewIngredient(Items.MAGMA_CREAM, MobEffects.FIRE_RESISTANCE, null, PotionTypes.FIRE_RESISTANCE.getEffects().get(0).getDuration(),0);
		nightVis = new BrewIngredient(Items.GOLDEN_CARROT, MobEffects.NIGHT_VISION, MobEffects.BLINDNESS, PotionTypes.NIGHT_VISION.getEffects().get(0).getDuration(), 200);
		instaHealth = new BrewIngredient(Items.SPECKLED_MELON, MobEffects.INSTANT_HEALTH, MobEffects.INSTANT_DAMAGE, 1, 1);
		strength = new BrewIngredient(Items.BLAZE_POWDER, MobEffects.STRENGTH, MobEffects.WEAKNESS, PotionTypes.STRENGTH.getEffects().get(0).getDuration(), PotionTypes.WEAKNESS.getEffects().get(0).getDuration());
		invis = new BrewIngredient(new ItemStack(ModItems.flowers,1,3), MobEffects.INVISIBILITY, MobEffects.GLOWING, true, false, PotionTypes.INVISIBILITY.getEffects().get(0).getDuration(), PotionTypes.INVISIBILITY.getEffects().get(0).getDuration());
		disrobing = new BrewIngredient(Items.SLIME_BALL, ModPotions.disrobing, null, 1,1);
		tinting = new BrewIngredient(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()), ModPotions.tinting, null, true, false, 3600, 1);
		skin_rotting = new BrewIngredient(Items.ROTTEN_FLESH, ModPotions.skin_rotting, null, 6000, 1);
		extinguish_fire = new BrewIngredient(Item.getItemFromBlock(Blocks.WATERLILY), ModPotions.extinguish_fire, null, 6000, 1);
		planting = new BrewIngredient(Items.WHEAT_SEEDS, ModPotions.planting, null, 1, 1);
	}
	
	
	@SubscribeEvent
	public static void registerBrewIngredients(RegistryEvent.Register<BrewIngredient> evt) {
		Log.i("Registering brews");
		evt.getRegistry().registerAll(healing, speedPot, jump, fireRes, nightVis, instaHealth, strength, invis, disrobing, 
				tinting, skin_rotting, extinguish_fire, planting);
	}
	
}
