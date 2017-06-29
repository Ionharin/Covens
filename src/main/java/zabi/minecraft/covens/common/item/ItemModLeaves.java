package zabi.minecraft.covens.common.item;

import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemStack;

public class ItemModLeaves extends ItemLeaves {

	public ItemModLeaves(BlockLeaves block) {
		super(block);
		this.setHasSubtypes(false);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = stack.getItem().getRegistryName().getResourcePath();
		return "tile.leaves."+name.substring(name.indexOf("log_")+8);
	}

}
