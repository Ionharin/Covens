package zabi.minecraft.covens.common.registries.brewing.environmental;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;


//A potion to environmental effect converter basically
@Mod.EventBusSubscriber
public abstract class EnvironmentalPotionEffect extends IForgeRegistryEntry.Impl<EnvironmentalPotionEffect> {
	
	public static final IForgeRegistry<EnvironmentalPotionEffect> REGISTRY = new RegistryBuilder<EnvironmentalPotionEffect>().setName(new ResourceLocation(Reference.MID, "pot_to_environmental")).setType(EnvironmentalPotionEffect.class).setIDRange(0, 200).create();
	
	private Potion potion;
	
	public EnvironmentalPotionEffect(Potion potion) {
		this.potion = potion;
	}
	
	public Potion getPotion() {
		return potion;
	}
	
	public abstract void splashedOn(World world, BlockPos pos, EntityLivingBase thrower, CovenPotionEffect data);
	
	@SubscribeEvent
	public static void registerBrewIngredients(RegistryEvent.Register<EnvironmentalPotionEffect> evt) {
		
	}
	
	@Nullable
	public static EnvironmentalPotionEffect getEffectForPotion(Potion potion) {
		for (EnvironmentalPotionEffect pe:REGISTRY) {
			if (pe.getPotion().equals(potion)) {
				return pe;
			}
		}
		return null;
	}
}
