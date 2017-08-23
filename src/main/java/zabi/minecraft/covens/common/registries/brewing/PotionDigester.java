package zabi.minecraft.covens.common.registries.brewing;

import java.awt.Color;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zabi.minecraft.covens.common.item.ItemBrewBase;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;

public class PotionDigester {
	
	
	public static ItemStack digestPotion(NonNullList<ItemStack> stacks) {
		
		Log.d("------------ Potion digester ---------------");
		ItemStack[] items = new ItemStack[stacks.size()];//Performance wise I prefer to have quick read time, since this list can get really long
		stacks.toArray(items);
		
		Item type = getType(items);
		Log.d("-- Potion type id: "+type);
		BrewData result = new BrewData();
		if (type==null) {
			result = new BrewData();
			result.spoil();
		}
		int effectSize = getEffectSize(items);
		Log.d("-- Effect slots found: "+effectSize);
		int read = 1;
		int currentBaseDuration = 0;
		int currentOppositeDuration = 0;
		int color = -1;
		BrewIngredient currentPotion = null;
		float currentLengthModifier = 1;
		int currentPower = 0;
		int persistency = 0;
		boolean isCurable = true;
		boolean showParticles = true;
		boolean getOpposite = false;
		Log.d("-- Starting recipe analysis");
		while (read<items.length - 1) {
			if (items[read]!=null) Log.d(read+") Item analyzed: "+items[read].getItem().getRegistryName()+":"+items[read].getMetadata());
			else Log.d(read+") Null item, skipping");
			
			if (items[read]!=null) {
				if (CovensBrewIngredientRegistry.isNewPotionInitializer(items[read])) {
					if (currentPotion!=null) {
						Log.d("Finishing potion "+currentPotion.getResult().getRegistryName());
						CovenPotionEffect pe = new CovenPotionEffect(getOpposite?currentPotion.getOpposite():currentPotion.getResult(), getOpposite?currentOppositeDuration:currentBaseDuration, currentPower);
						pe.setCurable(isCurable);
						pe.setMultiplier(currentLengthModifier);
						pe.setShowParticle(showParticles);
						pe.setPersistency(persistency);
						result.addEffectToBrew(pe);
						if (result.getEffects().size()>=effectSize) { //RuinedPotion
							Log.d("Too many effects for slots: spoiled");
							result.spoil();
						}
					}
					currentPotion = CovensBrewIngredientRegistry.getPotion(items[read]);
					Log.d("Starting Potion "+currentPotion.getResult().getRegistryName());
					currentBaseDuration = CovensBrewIngredientRegistry.getDuration(items[read], false);
					currentOppositeDuration = CovensBrewIngredientRegistry.getDuration(items[read], true);
					currentLengthModifier = 1f;
					currentPower = 0;
					persistency = 0;
					getOpposite = false;
					isCurable = true;
					showParticles = true;
				} else if (items[read].getItem().equals(Items.REDSTONE) && currentPotion!=null) {
					currentLengthModifier+=0.4;
					Log.d("modifier: length");
					if (currentLengthModifier>3) currentLengthModifier=3;
				} else if (items[read].getItem().equals(Items.GLOWSTONE_DUST) && currentPotion!=null) {
					currentPower++;
					Log.d("modifier: power");
					if (currentPower>5) currentPower=5;
				} else if (items[read].getItem().equals(ModItems.flowers) && items[read].getMetadata()==2 && currentPotion!=null) {
					persistency++;
					Log.d("modifier: persistency");
					if (persistency>5) persistency=5;
				} else if (items[read].getItem().equals(Items.DIAMOND) && currentPotion!=null) {
					Log.d("no particles");
					showParticles = false;
				} else if (items[read].getItem().equals(ModItems.flowers) && items[read].getMetadata()==3 && currentPotion!=null) { //Chrysanthemum
					isCurable = false;
					Log.d("not curable");
				} else if (items[read].getItem().equals(Items.FERMENTED_SPIDER_EYE) && currentPotion!=null && !getOpposite) {
					if (currentPotion.getOpposite()==null) {
						Log.d("No opposite: spoiled");
						result.spoil(); //No opposite exists
					}
					Log.d("but reversed");
					getOpposite = true;
				} else if (items[read].getItem().equals(Item.getItemFromBlock(Blocks.WOOL))) {
					color = colorAverage(color, getHexFromMeta(items[read].getMetadata()));
					Log.d("color is now "+color);
				} else {
					Log.d("unrecognized item, spoiled: "+items[read].getItem().getRegistryName());
					result.spoil(); //Unrecognized Item, ruined potion
				}
			}
			read++;
		}
		if (currentPotion!=null) {
			Log.d("Finishing potion "+currentPotion.getResult().getRegistryName());
			CovenPotionEffect pe = new CovenPotionEffect(getOpposite?currentPotion.getOpposite():currentPotion.getResult(), getOpposite?currentOppositeDuration:currentBaseDuration, currentPower);
			pe.setCurable(isCurable);
			pe.setMultiplier(currentLengthModifier);
			pe.setShowParticle(showParticles);
			pe.setPersistency(persistency);
			result.addEffectToBrew(pe);
			Log.d("-- Analysis finished");
		} else {
			Log.d("Brew has no effects, spoiled");
			result.spoil();
		}
		
		if (!result.isSpoiled()) Log.d("Potion successful");
		Log.d("-------- result --------");
		for (CovenPotionEffect e:result.getEffects()) {
			Log.d("-->  "+e.getPotionEffect().getEffectName()+"\t"+e.getStrength()+"\t"+e.getMultiplier()+"\t"+e.getPersistency()+"\t"+e.isCurable()+"\t"+e.doesShowParticle());
		}
		Log.d("------------------------");
		if (color!=-1) {
			result.setColor(color);
		}
		if (type==null) type = ModItems.brew_drinkable;
		return ItemBrewBase.getBrewStackWithData(type, result);
	}

	private static int colorAverage(int color, int newColor) {
		if (color==-1) return newColor;
		Color c1 = new Color(color);
		Color c2 = new Color(newColor);
		Color res = new Color(
				(int) Math.sqrt((c1.getRed()*c1.getRed()+c2.getRed()*c2.getRed())/2), 
				(int) Math.sqrt((c1.getGreen()*c1.getGreen()+c2.getGreen()*c2.getGreen())/2), 
				(int) Math.sqrt((c1.getBlue()*c1.getBlue()+c2.getBlue()*c2.getBlue())/2));
		return res.getRGB()&0xFFFFFF;
	}

	private static int getHexFromMeta(int metadata) {
		switch (metadata) {
		case 0: return 16383998;
		case 1: return 16351261;
		case 2: return 13061821;
		case 3: return 3847130;
		case 4: return 16701501;
		case 5: return 8439583;
		case 6: return 15961002;
		case 7: return 4673362;
		case 8: return 10329495;
		case 9: return 1481884;
		case 10: return 8991416;
		case 11: return 3949738;
		case 12: return 8606770;
		case 13: return 6192150;
		case 14: return 11546150;
		case 15: return 1908001;
		}
		return 0;
	}

	private static Item getType(ItemStack[] items) {
		if (items.length==0) return null;
		ItemStack ity = items[items.length-1];
		Log.d("Potion type item: "+ity.getItem().getRegistryName());
		items[items.length-1] = null;
		if (ity.getItem().equals(ModItems.flowers) && ity.getMetadata()==1) return ModItems.brew_drinkable; //Hellebore
		if (ity.getItem().equals(ModItems.flowers) && ity.getMetadata()==0) return ModItems.brew_gas; //Aconitum
		if (ity.getItem().equals(Items.GUNPOWDER)) return ModItems.brew_splash; //Gunpowder
		if (ity.getItem().equals(Items.DRAGON_BREATH)) return ModItems.brew_lingering; //Breath
		Log.d("No match for "+ity.getItem().getRegistryName());
		return null;
	}

	private static int getEffectSize(ItemStack[] items) { //wart = +1, mysterious seeds  = +2, nether star = +4, some op endgame item = +8
		boolean[] catalysts = new boolean[4];
		int effectSize = 0;
		for (int i = 0; i < Math.min(items.length, 4);i++) {
			Log.d("-- Checking for slots: "+items[i]);
			if (items[i]!=null) {
				if (items[i].getItem().equals(Items.NETHER_WART) && !catalysts[0]) {
					catalysts[0]=true;
					items[i]=null;
					effectSize+=1;
				} else if (items[i].getItem().equals(ModItems.eerie_seeds) && !catalysts[1]) {
					catalysts[1]=true;
					items[i]=null;
					effectSize+=2;
				} else if (items[i].getItem().equals(Items.NETHER_STAR) && !catalysts[2]) {
					catalysts[2]=true;
					items[i]=null;
					effectSize+=4;
				} else if (items[i].getItem().equals(Items.NETHER_STAR) && !catalysts[3]) { //For now, 2 nstars
					catalysts[3]=true;
					items[i]=null;
					effectSize+=8;
				} else {
					return effectSize; //first invalid length modifier: start recipe digestion
				}
			}
		}
		return effectSize;
	}
}
