package zabi.minecraft.covens.common.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemCardinalStone extends Item {
	
	public static final int MAX_USES = 3;
	
	public ItemCardinalStone() {
		this.setUnlocalizedName("cardinal_stone");
		this.setRegistryName(Reference.MID, "cardinal_stone");
		this.setMaxStackSize(1);
		this.setCreativeTab(ModCreativeTabs.products);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		NBTTagCompound pos = stack.getOrCreateSubCompound("pos");
		if (pos.hasKey("x")) {
			tooltip.add(TextFormatting.GRAY + TextFormatting.ITALIC.toString() + I18n.format("item.cardinal_stone.bound_to", (int) pos.getDouble("x"), (int) pos.getDouble("y"), (int) pos.getDouble("z"), pos.getInteger("dim")));
		}
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("uses") && stack.getMetadata()==2) {
			return (1D/MAX_USES)*(stack.getTagCompound().getInteger("uses"));
		}
		return 0;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata()==1;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getMetadata()==2;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getMetadata()!=1) return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		NBTTagCompound pos = stack.getOrCreateSubCompound("pos"); 
		pos.setDouble("x", player.posX);
		pos.setDouble("y", player.posY);
		pos.setDouble("z", player.posZ);
		pos.setInteger("dim", player.getEntityWorld().provider.getDimension());
		pos.setDouble("pitch", player.rotationPitch);
		pos.setDouble("yaw", player.rotationYaw);
		stack.setItemDamage(2);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
}
