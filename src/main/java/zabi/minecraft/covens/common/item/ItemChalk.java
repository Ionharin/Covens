package zabi.minecraft.covens.common.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemChalk extends Item {
	
	private static final int MAX_USES = 40;

	public ItemChalk() {
		this.setUnlocalizedName("chalk");
		GameRegistry.register(this, new ResourceLocation(Reference.MID, "chalk"));
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setHasSubtypes(true);
		this.setCreativeTab(ModCreativeTabs.rituals);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack)+"_"+BlockCircleGlyph.GlyphType.values()[stack.getMetadata()].name().toLowerCase();
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
	@Override
	public boolean canItemEditBlocks() {
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (!stack.hasTagCompound()) stack.setTagCompound(getDefaultInstance().getTagCompound().copy());
		int usesLeft = MAX_USES - stack.getTagCompound().getInteger("usesLeft");
		return (double)usesLeft/(double)MAX_USES;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getDurabilityForDisplay(stack)>0;
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack original = super.getDefaultInstance();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("usesLeft", MAX_USES);
		original.setTagCompound(tag);
		return original;
	}
	
	@Override
	public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
		if (this.getCreativeTab().equals(itemIn)) {
			for (int i=0;i<4;i++) {
				ItemStack stack = getDefaultInstance();
				stack.setItemDamage(i);
				tab.add(stack);
			}
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean isReplacing = worldIn.getBlockState(pos).getBlock().equals(ModBlocks.glyphs);
		if (!worldIn.isRemote && (facing == EnumFacing.UP && ModBlocks.glyphs.canPlaceBlockAt(worldIn, pos.up()) || isReplacing)) {
			ItemStack chalk = player.getHeldItem(hand);
			if (!chalk.hasTagCompound()) chalk.setTagCompound(getDefaultInstance().getTagCompound().copy());
			int type = chalk.getItemDamage();
			int usesLeft = chalk.getTagCompound().getInteger("usesLeft") - 1;
			chalk.getTagCompound().setInteger("usesLeft", usesLeft);
			if (usesLeft<1) chalk.setCount(0);
			IBlockState state = ModBlocks.glyphs.getExtendedState(ModBlocks.glyphs.getDefaultState(), worldIn, pos);
			state = state.withProperty(BlockCircleGlyph.FACING, EnumFacing.HORIZONTALS[(int)(Math.random()*4)]);
			state = state.withProperty(BlockCircleGlyph.TYPE, BlockCircleGlyph.GlyphType.values()[type]);
			worldIn.setBlockState(isReplacing?pos:pos.up(), state, 2);
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
