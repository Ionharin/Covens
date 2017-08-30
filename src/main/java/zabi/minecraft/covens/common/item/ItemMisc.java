package zabi.minecraft.covens.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemMisc extends Item {
	public static final String[] names = new String[] {
			"empty_jar", //0
			"ash", //1
			"oak_spirit", //2 
			"birch_soul", //3
			"acacia_essence", //4
			"spruce_heart", //5
			"cloudy_oil", //6
			"wax_ball", //7
			"talisman", //8
			"talisman_charged", //9
			"unfired_jar", //10
			"cleansing_aura", //11
			"emanation_of_dishonesty", //12
			"foul_presence", //13
			"undying_image", //14
			"magic_leather" //15
	};
	
	public ItemMisc() {
		this.setCreativeTab(ModCreativeTabs.products);
		this.setHasSubtypes(true);
		this.setRegistryName(Reference.MID, "misc");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.misc."+names[stack.getMetadata()];
	}
	
	@Override
	public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
		if (this.isInCreativeTab(itemIn)) {
			tab.add(new ItemStack(this,1,10));
			tab.add(new ItemStack(this,1,0));
			for (int i = 2;i<6;i++) tab.add(new ItemStack(this,1,i));
			for (int i = 11;i<15;i++) tab.add(new ItemStack(this,1,i));
			tab.add(new ItemStack(this,1,6));
			tab.add(new ItemStack(this,1,1));
			tab.add(new ItemStack(this,1,7));
			tab.add(new ItemStack(this,1,8));
			tab.add(new ItemStack(this,1,9));
			tab.add(new ItemStack(this,1,15));
		}
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata()==9;
	}
	
	@Override
	public boolean canItemEditBlocks() {
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player.getHeldItem(hand).getMetadata()==1) { //ash
			if (world.getBlockState(pos).getBlock().canPlaceTorchOnTop(world.getBlockState(pos), world, pos)) {
				world.setBlockState(pos.up(), ModBlocks.confining_ash.getDefaultState());
				if (!player.isCreative()) {
					player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount()-1);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
}
