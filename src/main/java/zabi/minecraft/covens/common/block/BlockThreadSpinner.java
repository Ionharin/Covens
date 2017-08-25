package zabi.minecraft.covens.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;

public class BlockThreadSpinner extends Block implements ITileEntityProvider {

	public BlockThreadSpinner() {
		super(Material.WOOD);
		this.setRegistryName(Reference.MID, "thread_spinner");
		this.setUnlocalizedName("thread_spinner");
		this.setCreativeTab(ModCreativeTabs.machines);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityThreadSpinner();
	}
	
}
