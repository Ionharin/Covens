package zabi.minecraft.covens.common.crafting.brewing;

import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

public class BrewData {
	private NonNullList<CovenPotionEffect> brewEffects = NonNullList.<CovenPotionEffect>create();
	private int color = 0x000000;
	
	public void addEffectToBrew(CovenPotionEffect potionEffect) {
		brewEffects.add(potionEffect);
		recalculateColor();
	}
	
	private void recalculateColor() {
		color = PotionUtils.getPotionColorFromEffectList(brewEffects.stream().map(cpe -> cpe.getPotionEffect()).collect(Collectors.toList()));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		int i = 0;
		for (CovenPotionEffect pot:brewEffects) {
			NBTTagCompound cpe = pot.writeToNBT(new NBTTagCompound());
			tag.setTag("pot"+i, cpe);
			i++;
		}
		tag.setInteger("color", color);
		return tag;
	}
	
	public BrewData readFromNBT(NBTTagCompound tag) {
		color = tag.getInteger("color");
		for (String tagname:tag.getKeySet()) {
			if (tagname.startsWith("pot")) {
				CovenPotionEffect cpe = CovenPotionEffect.loadFromNBT(tag.getCompoundTag(tagname));
				brewEffects.add(cpe);
			}
		}
		return this;
	}
	
	public NonNullList<CovenPotionEffect> getEffects() {
		return brewEffects;
	}
	
	public int getColor() {
		return color;
	}
	
}
