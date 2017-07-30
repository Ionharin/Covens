package zabi.minecraft.covens.common.item;

import java.util.List;
import java.util.Random;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemSoulString extends Item {
	public ItemSoulString() {
		this.setRegistryName(Reference.MID, "soulstring");
		this.setUnlocalizedName("soulstring");
		this.setCreativeTab(ModCreativeTabs.products);
		this.setMaxStackSize(3);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata()==1;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getMetadata()==1) {
			NBTTagCompound data = stack.getOrCreateSubCompound("boundData");
			if (data.hasKey("name")) {
				tooltip.add(TextFormatting.GRAY+TextFormatting.ITALIC.toString()+I18n.format("item.soulstring.bound", data.getString("name")));
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		if (stack.getMetadata()!=0) return false;
		if (!player.world.isRemote) {
			if (target instanceof EntityPlayer) {
				if (new Random().nextDouble()<0.5d) {
					bindEntity(player, target, hand);
				} else {
					((EntityPlayer)target).sendStatusMessage(new TextComponentString(TextFormatting.BOLD+TextFormatting.DARK_RED.toString()+I18n.format("item.soulstring.warning")), true);
				}
			} else bindEntity(player, target, hand);
		}
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.getHeldItem(hand).getMetadata()!=0) return EnumActionResult.PASS;
		if (!worldIn.isRemote) bindEntity(player, player, hand);
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.getMetadata()==0) return super.getItemStackLimit(stack);
		return 1;
	}

	private void bindEntity(EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		ItemStack res = new ItemStack(this,1,1);
		NBTTagCompound tag = res.getOrCreateSubCompound("boundData");
		tag.setString("name", target.getDisplayName().getUnformattedText());
		tag.setString("uid", target.getUniqueID().toString());
		tag.setBoolean("isPlayer", target instanceof EntityPlayer);
		player.setHeldItem(hand, res);
	}
	
}
