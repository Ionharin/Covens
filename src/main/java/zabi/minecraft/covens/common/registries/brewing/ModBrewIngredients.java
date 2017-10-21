package zabi.minecraft.covens.common.registries.brewing;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.potion.ModPotions;

@Mod.EventBusSubscriber
public class ModBrewIngredients {

	public static BrewIngredient healing, speedPot, jump, fireRes, nightVis, instaHealth, strength, invis, disrobing, tinting, 
	skin_rotting, extinguish_fire, planting, arrow_block, harvest;

	public static void registerAll() {
		Log.i("Creating brews");
		MinecraftForge.EVENT_BUS.register(new ModBrewIngredients());
		
		healing = new BrewIngredient(Ingredient.fromItem(Items.GHAST_TEAR), MobEffects.REGENERATION, MobEffects.POISON, PotionTypes.REGENERATION.getEffects().get(0).getDuration(), PotionTypes.POISON.getEffects().get(0).getDuration());
		speedPot = new BrewIngredient(Ingredient.fromItem(Items.SUGAR), MobEffects.SPEED, MobEffects.SLOWNESS, PotionTypes.SWIFTNESS.getEffects().get(0).getDuration(), PotionTypes.SLOWNESS.getEffects().get(0).getDuration());
		jump = new BrewIngredient(Ingredient.fromItem(Items.RABBIT_FOOT), MobEffects.JUMP_BOOST, null, PotionTypes.LEAPING.getEffects().get(0).getDuration(),0);
		fireRes = new BrewIngredient(Ingredient.fromItem(Items.MAGMA_CREAM), MobEffects.FIRE_RESISTANCE, null, PotionTypes.FIRE_RESISTANCE.getEffects().get(0).getDuration(),0);
		nightVis = new BrewIngredient(Ingredient.fromItem(Items.GOLDEN_CARROT), MobEffects.NIGHT_VISION, MobEffects.BLINDNESS, PotionTypes.NIGHT_VISION.getEffects().get(0).getDuration(), 200);
		instaHealth = new BrewIngredient(Ingredient.fromItem(Items.SPECKLED_MELON), MobEffects.INSTANT_HEALTH, MobEffects.INSTANT_DAMAGE, 1, 1);
		strength = new BrewIngredient(Ingredient.fromItem(Items.BLAZE_POWDER), MobEffects.STRENGTH, MobEffects.WEAKNESS, PotionTypes.STRENGTH.getEffects().get(0).getDuration(), PotionTypes.WEAKNESS.getEffects().get(0).getDuration());
		invis = new BrewIngredient(Ingredient.fromStacks(new ItemStack(ModItems.flowers,1,3)), MobEffects.INVISIBILITY, MobEffects.GLOWING, PotionTypes.INVISIBILITY.getEffects().get(0).getDuration(), PotionTypes.INVISIBILITY.getEffects().get(0).getDuration());
		disrobing = new BrewIngredient(Ingredient.fromItem(Items.SLIME_BALL), ModPotions.disrobing, null, 1,1);
		tinting = new BrewIngredient(Ingredient.fromStacks(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())), ModPotions.tinting, null, 3600, 1);
		skin_rotting = new BrewIngredient(Ingredient.fromItem(Items.ROTTEN_FLESH), ModPotions.skin_rotting, null, 6000, 1);
		extinguish_fire = new BrewIngredient(Ingredient.fromItem(Item.getItemFromBlock(Blocks.WATERLILY)), ModPotions.extinguish_fire, null, 6000, 1);
		planting = new BrewIngredient(Ingredient.fromItem(Items.WHEAT_SEEDS), ModPotions.planting, null, 1, 1);
		arrow_block = new BrewIngredient(Ingredient.fromItem(Item.getItemFromBlock(Blocks.IRON_ORE)), ModPotions.arrow_block, null, 20*180, 1);
		harvest = new BrewIngredient(Ingredient.fromItem(Items.WHEAT), ModPotions.harvest, 1);
	}


	@SubscribeEvent
	public void registerBrewIngredients(RegistryEvent.Register<BrewIngredient> evt) {
		Log.i("Registering brews");
		evt.getRegistry().registerAll(healing, speedPot, jump, fireRes, nightVis, instaHealth, strength, invis, disrobing, 
				tinting, skin_rotting, extinguish_fire, planting, arrow_block, harvest);
	}
}
