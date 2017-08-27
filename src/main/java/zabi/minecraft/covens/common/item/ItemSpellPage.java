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
import net.minecraft.world.World;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;
import zabi.minecraft.covens.common.registries.spell.Spell;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;

public class ItemSpellPage extends Item {

	public ItemSpellPage() {
		this.setRegistryName(Reference.MID, "spell_page");
		this.setUnlocalizedName("spell_page");
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(ModCreativeTabs.products);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (Spell s:Spell.REGISTRY) {
				Log.i("sn: "+s.getRegistryName().toString());
				items.add(getStackFor(s));
			}
		}
	}

	public ItemStack getStackFor(Spell s) {
		ItemStack stack = new ItemStack(this);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("spell", s.getRegistryName().toString());
		return stack;
	}
	
	public Spell getSpellFromItemStack(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("spell")) {
			return Spell.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("spell")));
		}
		return null;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Spell spell = getSpellFromItemStack(stack);
		if (spell!=null) tooltip.add(I18n.format(spell.getName()));
		else tooltip.add(I18n.format("item.spell_page.no_spell"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		Spell spell = getSpellFromItemStack(stack);
		if (spell!=null && !worldIn.isRemote) {
			if (spell.getType()==EnumSpellType.INSTANT) spell.performEffect(new RayTraceResult(Type.MISS, entityLiving.getLookVec(), EnumFacing.UP, entityLiving.getPosition()), entityLiving);
			else {
				EntitySpellCarrier car = new EntitySpellCarrier(worldIn, entityLiving.posX, entityLiving.posY+entityLiving.getEyeHeight(), entityLiving.posZ);
				car.setSpell(spell);
				car.setCaster(entityLiving);
				car.setHeadingFromThrower(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, 0.1f, 0);
				worldIn.spawnEntity(car);
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 30;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
}
