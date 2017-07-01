package zabi.minecraft.covens.common.registries.brewing;

import java.util.Collections;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class CovenPotionEffect {
	
	public static final HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	private String potion = "";
	private int length = 0;
	private float multiplier = 1;
	private int strength = 0;
	private int persistency = 0;
	private boolean showParticle = true;
	private boolean isAmbient = true;
	private boolean isCurable = true;
	
	private CovenPotionEffect() {}
	
	public CovenPotionEffect(Potion potion, int duration, int level) {
		this.potion = potion.getRegistryName().toString();
		this.length = duration;
		this.strength = level;
	}
	
	public PotionEffect getPotionEffect() {
		PotionEffect pe = new PotionEffect(Potion.getPotionFromResourceLocation(potion), (int) (length*multiplier), strength, isAmbient, showParticle);
		if (isCurable) pe.setCurativeItems(Collections.singletonList(new ItemStack(Items.MILK_BUCKET)));
		else pe.setCurativeItems(Collections.<ItemStack>emptyList());
		return pe;
	}

	public int getPersistency() {
		return persistency;
	}

	public CovenPotionEffect setPersistency(int persistency) {
		this.persistency = persistency;
		return this;
	}

	public boolean isCurable() {
		return isCurable;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public CovenPotionEffect setCurable(boolean isCurable) {
		this.isCurable = isCurable;
		return this;
	}

	public boolean isAmbient() {
		return isAmbient;
	}

	public CovenPotionEffect setAmbient(boolean isAmbient) {
		this.isAmbient = isAmbient;
		return this;
	}

	public boolean doesShowParticle() {
		return showParticle;
	}

	public CovenPotionEffect setShowParticle(boolean showParticle) {
		this.showParticle = showParticle;
		return this;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setString("potion", potion);
		tag.setIntArray("data_int", new int[] {length, strength, persistency});
		tag.setBoolean("showParticle", showParticle);
		tag.setBoolean("isAmbient", isAmbient);
		tag.setBoolean("isCurable", isCurable);
		tag.setFloat("multiplier", multiplier);
		return tag;
	}
	
	public static CovenPotionEffect loadFromNBT(NBTTagCompound tag) {
		CovenPotionEffect cpe = new CovenPotionEffect();
		cpe.potion = tag.getString("potion");
		int[] data = tag.getIntArray("data_int");
		cpe.length = data[0];
		cpe.strength = data[1];
		cpe.persistency = data[2];
		cpe.showParticle = tag.getBoolean("showParticle");
		cpe.isAmbient = tag.getBoolean("isAmbient");
		cpe.isCurable = tag.getBoolean("isCurable");
		cpe.setMultiplier(tag.getFloat("multiplier"));
		return cpe;
	}

	public float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(float newMultiplier) {
		this.multiplier = newMultiplier;
	}

	public int getBaseCost() {
		return potion!=null && map.containsKey(potion) ? map.get(potion) : 200;
	}
	
	public void setDiminished() {
		strength/=2;
		persistency/=2;
		multiplier/=2;
	}
	
	static {
		map.put("extraalchemy:cheat_death", 1000); //Example
		map.put("minecraft:instant_health", 800);
		map.put("minecraft:regeneration", 800);
	}
	
}
