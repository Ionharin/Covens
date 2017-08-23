package zabi.minecraft.covens.common.registries.brewing;

import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import zabi.minecraft.covens.common.item.ModItems;

public class BrewData {
	private NonNullList<CovenPotionEffect> brewEffects = NonNullList.<CovenPotionEffect>create();
	private int type = 0;
	private int color = 0x000000;
	private boolean spoiled = false;
	
	public BrewData() {
		this(0);
	}
	
	public BrewData(int type) {
		this.type=type;
	}

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
		tag.setInteger("type", type);
		tag.setBoolean("spoiled", spoiled);
		return tag;
	}
	
	public BrewData readFromNBT(NBTTagCompound tag) {
		color = tag.getInteger("color");
		type = tag.getInteger("type");
		spoiled = tag.getBoolean("spoiled");
		for (String tagname:tag.getKeySet()) {
			if (tagname.startsWith("pot")) {
				CovenPotionEffect cpe = CovenPotionEffect.loadFromNBT(tag.getCompoundTag(tagname));
				brewEffects.add(cpe);
			}
		}
		return this;
	}
	
	@Nullable
	public static BrewData getDataFromStack(ItemStack stack) {
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("brewdata")) return null;
		return new BrewData().readFromNBT(stack.getOrCreateSubCompound("brewdata"));
	}
	
	public NonNullList<CovenPotionEffect> getEffects() {
		if (brewEffects.size()>0) brewEffects.get(0).setColor(color);
		return brewEffects;
	}
	
	public int getColor() {
		return spoiled?0x4f670a:color;
	}

	public int getCost() {
		if (spoiled) return 0;
		int cost = Math.min(40*(1<<brewEffects.size()),200);
		for (CovenPotionEffect pe:brewEffects) {
			int powerCost = (pe.getStrength()*pe.getStrength()/2)+1;
			int addedCost = (int) (powerCost*pe.getMultiplier()*pe.getBaseCost());
			cost += addedCost;
		}
		return cost;
	}
	
	public Item getType() {
		if (type==1) return ModItems.brew_splash;
		if (type==2) return ModItems.brew_lingering;
		if (type==3) return ModItems.brew_gas;
		return ModItems.brew_drinkable;
	}
	
	public void spoil() {
		spoiled = true;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

	public boolean isSpoiled() {
		return spoiled;
	}
	
}
