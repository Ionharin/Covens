package zabi.minecraft.covens.common.item;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Reference;

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
				items.add(getStackFor(s));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("spell")) return super.getUnlocalizedName(stack)+"."+stack.getTagCompound().getString("spell").replace(':', '.');
		return super.getUnlocalizedName(stack);
	}
	
	public ItemStack getStackFor(Spell s) {
		ItemStack stack = new ItemStack(this);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("spell", s.getRegistryName().toString());
		return stack;
	}
	
	@Nullable
	public static Spell getSpellFromItemStack(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("spell")) {
			return Spell.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("spell")));
		}
		return null;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Spell s = getSpellFromItemStack(playerIn.getHeldItem(handIn));
		if (s!=null && s.canBeUsed(worldIn, playerIn.getPosition(), playerIn)) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		} else {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		Spell spell = getSpellFromItemStack(stack);
		if (spell!=null && !worldIn.isRemote) {
			if (spell.getType()==EnumSpellType.INSTANT) spell.performEffect(new RayTraceResult(Type.MISS, entityLiving.getLookVec(), EnumFacing.UP, entityLiving.getPosition()), entityLiving, worldIn);
			else {
				EntitySpellCarrier car = new EntitySpellCarrier(worldIn, entityLiving.posX+entityLiving.getLookVec().x, entityLiving.posY+entityLiving.getEyeHeight()+entityLiving.getLookVec().y, entityLiving.posZ+entityLiving.getLookVec().z);
				car.setSpell(spell);
				car.setCaster(entityLiving);
				car.setHeadingFromThrower(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, 2f, 0);
				worldIn.spawnEntity(car);
			}
		}
		if (entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).isCreative()) return stack;
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
