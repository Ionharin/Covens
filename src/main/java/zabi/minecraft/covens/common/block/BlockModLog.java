package zabi.minecraft.covens.common.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;

public class BlockModLog extends BlockLog {
	
	public BlockModLog(String name) {
		this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, EnumAxis.Y));
		this.setUnlocalizedName("log_"+name);
		this.setRegistryName(Reference.MID, "log_"+name);
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumAxis axis = EnumAxis.values()[meta];
		return getDefaultState().withProperty(LOG_AXIS, axis);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(this), 1, 0);
	}
}
