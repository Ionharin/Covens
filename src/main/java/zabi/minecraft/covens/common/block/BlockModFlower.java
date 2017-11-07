package zabi.minecraft.covens.common.block;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.BlockBush;
import zabi.minecraft.covens.common.item.ModCreativeTabs;

public class BlockModFlower extends BlockBush {
	
	public BlockModFlower(String name) {
		this.setRegistryName(Reference.MID, name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
	
}
