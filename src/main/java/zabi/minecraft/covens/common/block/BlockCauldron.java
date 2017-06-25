package zabi.minecraft.covens.common.block;

import java.util.Collections;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.crafting.brewing.CovenPotionEffect;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityCauldron;

public class BlockCauldron extends Block implements ITileEntityProvider {
	
	public static final PropertyBool FULL = PropertyBool.create("filled");

	public BlockCauldron() {
		super(Material.IRON);
		this.setUnlocalizedName("cauldron");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setRegistryName(Reference.MID, "cauldron");
		this.setDefaultState(blockState.getBaseState().withProperty(FULL, false));
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(1);
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCauldron();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;//state.getValue(FULL);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FULL);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FULL, meta==1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FULL)?1:0;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hand==EnumHand.MAIN_HAND && worldIn.getBlockState(pos.down()).getBlock().equals(Blocks.FIRE)) {//Check if full too
			if (playerIn.getHeldItem(hand).getItem().equals(Items.GLASS_BOTTLE)) {
				TileEntityCauldron cauldron = (TileEntityCauldron) worldIn.getTileEntity(pos);
				ItemStack stack = new ItemStack(Items.POTIONITEM);
				for (CovenPotionEffect cpe:cauldron.getResult().getEffects()) PotionUtils.appendEffects(stack, Collections.singleton(cpe.getPotionEffect()));
				worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
				return true;
			}
		}
		Log.i(worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound()));
		return false;
	}
}
