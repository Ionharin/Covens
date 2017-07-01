package zabi.minecraft.covens.common.registries.brewing;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;

public class PotionDigester {
	
	
	public static BrewData digestPotion(NonNullList<ItemStack> stacks) {
		
		Log.d("------------ Potion digester ---------------");
		ItemStack[] items = new ItemStack[stacks.size()];//Performance wise I prefer to have quick read time, since this list can get really long
		stacks.toArray(items);
		
		int type = getType(items);
		Log.d("-- Potion type id: "+type);
		if (type<0) {
			Log.d("No type: spoiled");
			return new BrewData();
		}
		BrewData result = new BrewData(type);
		int effectSize = getEffectSize(items);
		Log.d("-- Effect slots found: "+effectSize);
		int read = 1;
		
		int currentBaseDuration = 0;
		int currentOppositeDuration = 0;
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
							return new BrewData();
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
						return new BrewData(); //No opposite exists
					}
					Log.d("but reversed");
					getOpposite = true;
				} else {
					Log.d("unrecognized item, spoiled: "+items[read].getItem().getRegistryName());
					return new BrewData(); //Unrecognized Item, ruined potion
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
		}
		
		if (!result.getEffects().isEmpty()) Log.d("Potion successful");
		Log.d("-------- result --------");
		for (CovenPotionEffect e:result.getEffects()) {
			Log.d("-->  "+e.getPotionEffect().getEffectName()+"\t"+e.getStrength()+"\t"+e.getMultiplier()+"\t"+e.getPersistency()+"\t"+e.isCurable()+"\t"+e.doesShowParticle());
		}
		Log.d("------------------------");
		
		return result;
	}

	private static int getType(ItemStack[] items) {
		if (items.length==0) return -1;
		ItemStack ity = items[items.length-1];
		Log.d("Potion type item: "+ity.getItem().getRegistryName());
		items[items.length-1] = null;
		if (ity.getItem().equals(ModItems.flowers) && ity.getMetadata()==1) return 0; //Hellebore = drinkable
		if (ity.getItem().equals(ModItems.flowers) && ity.getMetadata()==0) return 3; //Aconitum = gas
		if (ity.getItem().equals(Items.GUNPOWDER)) return 1; //Gunpowder = splash
		if (ity.getItem().equals(Items.DRAGON_BREATH)) return 2; //Breath = lingering
		Log.d("No match for "+ity.getItem().getRegistryName());
		return -1;
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
