package zabi.minecraft.covens.common.item;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.entity.EntityBrew;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.registries.brewing.BrewIngredient;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;

public class ItemBrewBase extends Item {
	
	protected static final String[] names = new String[] {"full", "spoiled"};
	
	public ItemBrewBase(String name) {
		this.setCreativeTab(ModCreativeTabs.brews);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setRegistryName(Reference.MID, name);
		this.setUnlocalizedName(name);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab.equals(getCreativeTab())) {

			for (BrewIngredient bi:BrewIngredient.REGISTRY) {
				BrewData data = new BrewData();
				data.addEffectToBrew(new CovenPotionEffect(bi.getResult(), bi.getDuration(), 0));
				items.add(getBrewStackWithData(this, data));
				if (bi.getOpposite()!=null) {
					BrewData opp = new BrewData();
					opp.addEffectToBrew(new CovenPotionEffect(bi.getOpposite(), bi.getDurationOpposite(), 0));
					items.add(getBrewStackWithData(this, opp));
				}
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (BrewData.getDataFromStack(stack).isSpoiled()) {
			tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.brew.spoiled"));
		} else {
			addPotionTooltip(stack, tooltip);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void addPotionTooltip(ItemStack itemIn, List<String> stackTooltip) {
		BrewData data = BrewData.getDataFromStack(itemIn);
		List<CovenPotionEffect> list = data.getEffects();
		List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>>newArrayList();

		if (list.isEmpty() || data.isSpoiled()) {
			String s = I18n.format("effect.none").trim();
			stackTooltip.add(TextFormatting.GRAY + s);
		} else {
			for (CovenPotionEffect pef : list) {
				PotionEffect potioneffect = pef.getPotionEffect();
				if (potioneffect.getPotion()==null) return;
				String effectName = I18n.format(potioneffect.getEffectName()).trim();
				Potion potion = potioneffect.getPotion();
				Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier originalAttributeModifier = entry.getValue();
						AttributeModifier copySafeAM = new AttributeModifier(originalAttributeModifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), originalAttributeModifier), originalAttributeModifier.getOperation());
						list1.add(new Tuple<String, AttributeModifier>(((IAttribute)entry.getKey()).getName(), copySafeAM));
					}
				}

				if (potioneffect.getAmplifier() > 0) {
					effectName = effectName + " " + I18n.format("potion.potency." + potioneffect.getAmplifier()).trim();
				}

				if (potioneffect.getDuration() > 20) {
					effectName = effectName + " (" + Potion.getPotionDurationString(potioneffect, pef.getMultiplier()) + ")";
				}

				if (potion.isBadEffect()) {
					stackTooltip.add(TextFormatting.RED + effectName);
				} else {
					stackTooltip.add(TextFormatting.BLUE + effectName);
				}
				
				if (!pef.hasEntityEffect()) stackTooltip.add(" "+TextFormatting.GRAY+I18n.format("potion.removed.entity"));
				if (!pef.hasEnvironmentalEffect()) stackTooltip.add(" "+TextFormatting.GRAY+I18n.format("potion.removed.environmental"));
			}
		}
		if (!list1.isEmpty()) {
			stackTooltip.add("");
			stackTooltip.add(TextFormatting.DARK_PURPLE + I18n.format("potion.whenDrank"));

			for (Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
					d1 = attributemodifier2.getAmount();
				}
				else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					stackTooltip.add(TextFormatting.BLUE + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.format("attribute.name." + (String)tuple.getFirst())));
				}
				else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					stackTooltip.add(TextFormatting.RED + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.format("attribute.name." + (String)tuple.getFirst())));
				}
			}
		}
	}

	public static ItemStack getBrewStackWithData(Item item, BrewData data) {
		ItemStack stack = new ItemStack(item);
		NBTTagCompound tag = stack.getOrCreateSubCompound("brewdata");
		data.writeToNBT(tag);
		stack.setTagInfo("brewdata", tag);
		NBTTagInt color = new NBTTagInt(data.getColor());
		stack.setTagInfo("color", color);
		return stack;
	}

	public static int getPotionColor(ItemStack stack) {
		BrewData data = BrewData.getDataFromStack(stack);
		if (data.isSpoiled()) return 0x4f670a;
		return data.getColor();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		BrewData data = BrewData.getDataFromStack(stack);
		return super.getUnlocalizedName()+"_"+names[data.isSpoiled()?1:0];
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		BrewData data = BrewData.getDataFromStack(stack);
		if (!data.isSpoiled()) {
			ItemStack pot = stack.copy();
			pot.setCount(1);
			EntityBrew ent = new EntityBrew(worldIn, playerIn, pot);
			Vec3d lv = playerIn.getLookVec().scale(0.4d);
			ent.setVelocity(lv.x, lv.y, lv.z);
			if (!worldIn.isRemote) worldIn.spawnEntity(ent);
			if (!playerIn.isCreative()) stack.setCount(stack.getCount()-1);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
}
