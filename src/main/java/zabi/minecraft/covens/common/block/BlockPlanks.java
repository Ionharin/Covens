package zabi.minecraft.covens.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.lib.Reference;

public class BlockPlanks extends Block {

	public BlockPlanks(String name) {
		super(Material.WOOD);
		this.setUnlocalizedName("planks_"+name);
		this.setRegistryName(Reference.MID, "planks_"+name);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(ModCreativeTabs.herbs);
	}
}
