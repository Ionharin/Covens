package zabi.minecraft.covens.common.item;

import java.util.List;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import zabi.minecraft.covens.api.utils.IInfusionPowerUser;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;
import zabi.minecraft.covens.common.network.messages.InfusionPowerChanged;
import zabi.minecraft.covens.common.registries.spell.Spell;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;

public class ItemGrimoire extends Item implements IInfusionPowerUser {

	public ItemGrimoire() {
		this.setRegistryName(Reference.MID, "grimoire");
		this.setUnlocalizedName("grimoire");
		this.setCreativeTab(ModCreativeTabs.products);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			items.add(new ItemStack(this));
			ItemStack s = new ItemStack(this);
			s.setTagCompound(new NBTTagCompound());
			NBTTagCompound list = new NBTTagCompound();
			int i = 0;
			for (Spell spell:Spell.REGISTRY) {
				list.setString("spell"+i, spell.getRegistryName().toString());
				i++;
			}
			s.getTagCompound().setInteger("storedSpells", i);
			s.getTagCompound().setTag("spells", list);
			s.getTagCompound().setBoolean("creative", true);
			s.getTagCompound().setInteger("selected", 0);
			items.add(s);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag.hasKey("spells")) {
				if (tag.getInteger("selected")>=0) {
					String spellRegName = tag.getCompoundTag("spells").getString("spell"+tag.getInteger("selected"));
					String spellName = TextFormatting.AQUA+I18n.format("item.spell_page."+(spellRegName.replace(':', '.'))+".name");
					tooltip.add(I18n.format("item.grimoire.selected", spellName));
				} else {
					tooltip.add(I18n.format("item.grimoire.selected.none"));
				}
			}
			tooltip.add(I18n.format("item.grimoire.spells_number", tag.getInteger("storedSpells")));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("creative")) return super.getUnlocalizedName(stack)+".creative";
		return super.getUnlocalizedName(stack);
	}

	@Override
	public String getHighlightTip(ItemStack item, String displayName) {
		if (item.hasTagCompound() && item.getTagCompound().hasKey("selected") && item.getTagCompound().getInteger("selected")>=0) {
			NBTTagCompound spells = item.getTagCompound().getCompoundTag("spells");
			String spellRegName = spells.getString("spell"+item.getTagCompound().getInteger("selected"));
			spellRegName = ("item.spell_page."+(spellRegName.replace(':', '.'))+".name");
			String spellName = I18n.format(spellRegName);
			return I18n.format("item.grimoire.format.name", displayName, spellName);
		}
		return super.getHighlightTip(item, displayName);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Spell s = getSpellFromItemStack(playerIn.getHeldItem(handIn));
		if (s!=null && s.canBeUsed(worldIn, playerIn.getPosition(), playerIn)) {
			if (playerIn.getCapability(PlayerData.CAPABILITY, null).usePower(s.getCost(), true)) {
				playerIn.setActiveHand(handIn);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	private Spell getSpellFromItemStack(ItemStack item) {
		if (item.hasTagCompound() && item.getTagCompound().hasKey("selected") && item.getTagCompound().getInteger("selected")>=0) {
			NBTTagCompound spells = item.getTagCompound().getCompoundTag("spells");
			return Spell.REGISTRY.getValue(new ResourceLocation(spells.getString("spell"+item.getTagCompound().getInteger("selected"))));
		}
		return null;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack item, World worldIn, EntityLivingBase entityLiving) {
		Spell spell = getSpellFromItemStack(item);
		if (spell!=null && !worldIn.isRemote) {
			if ((!(entityLiving instanceof EntityPlayer)) || entityLiving.getCapability(PlayerData.CAPABILITY, null).usePower(spell.getCost(), false)) {
				if (spell.getType()==EnumSpellType.INSTANT) spell.performEffect(new RayTraceResult(Type.MISS, entityLiving.getLookVec(), EnumFacing.UP, entityLiving.getPosition()), entityLiving, worldIn);
				else {
					EntitySpellCarrier car = new EntitySpellCarrier(worldIn, entityLiving.posX+entityLiving.getLookVec().x, entityLiving.posY+entityLiving.getEyeHeight()+entityLiving.getLookVec().y, entityLiving.posZ+entityLiving.getLookVec().z);
					car.setSpell(spell);
					car.setCaster(entityLiving);
					car.setHeadingFromThrower(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, 2f, 0);
					worldIn.spawnEntity(car);
				}
				Covens.network.sendToDimension(new InfusionPowerChanged((EntityPlayer) entityLiving, entityLiving.getCapability(PlayerData.CAPABILITY, null).getInfusionPower()), entityLiving.world.provider.getDimension());
			}
		}
		return super.onItemUseFinish(item, worldIn, entityLiving);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}

	public static void scrollSpell(ItemStack stack, int direction) {
		NBTTagCompound tag = stack.getTagCompound();
		if (stack.getItem()==ModItems.grimoire && tag!=null && tag.hasKey("selected")) {
			int maxSpells = tag.getInteger("storedSpells");
			int currentlySelected = tag.getInteger("selected");
			if (currentlySelected>=maxSpells) {
				Log.w("Selected spell is outside of bounds: "+currentlySelected+"/"+maxSpells);
				return;
			}
			if (currentlySelected<0 && maxSpells<=0) {
				return;
			}
			if (currentlySelected<0 && maxSpells>0) {
				if (direction>0) currentlySelected = direction%maxSpells;
				else if (direction<0) currentlySelected = (-direction)%maxSpells;
				selectSpell(stack, currentlySelected);
				return;
			}
			if (direction>0) selectSpell(stack, (currentlySelected+direction)%maxSpells);
			else {
				if (direction+currentlySelected<0) selectSpell(stack, currentlySelected+maxSpells+direction);
				else selectSpell(stack, direction+currentlySelected);
			}
		}
	}

	private static void selectSpell(ItemStack stack, int currentlySelected) {
		stack.getTagCompound().setInteger("selected", currentlySelected);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
}
