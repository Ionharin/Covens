package zabi.minecraft.covens.common.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import zabi.minecraft.covens.common.crafting.brewing.BrewData;
import zabi.minecraft.covens.common.crafting.brewing.PotionDigester;

public class TileEntityCauldron extends TileEntityBase {
	
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>create();
	private boolean hasItemsInside = false;

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		NBTTagCompound inv = tag.getCompoundTag("inv");
		for (String s:inv.getKeySet()) {
			stacks.set(Integer.parseInt(s), new ItemStack(inv.getCompoundTag(s)));
		}
		hasItemsInside = tag.getBoolean("hasItems");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		NBTTagCompound inv = new NBTTagCompound();
		int i = 0;
		for (ItemStack s:stacks) {
			NBTTagCompound stackTag = new NBTTagCompound();
			if (!s.isEmpty()) s.writeToNBT(stackTag);
			inv.setTag(""+i, stackTag);
			i++;
		}
		tag.setTag("inv", inv);
		tag.setBoolean("hasItems", hasItemsInside);
	}

	@Override
	protected void tick() {
		for (EntityItem entityIn:world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up()))){
			if (world.getTileEntity(pos)!=null && world.getBlockState(pos.down()).getBlock().equals(Blocks.FIRE)) {
				EntityItem ei = (EntityItem) entityIn;
				TileEntityCauldron tec = (TileEntityCauldron) world.getTileEntity(pos);
				tec.dropItem(ei.getItem());
				ei.setDead();
			}
		}
	}	
	
	public BrewData getResult() {
		BrewData data = PotionDigester.digestPotion(stacks);
		stacks = NonNullList.<ItemStack>create();
		hasItemsInside = false;
		return data;
	}

	public void dropItem(ItemStack stack) {
		if (!stack.isEmpty()) {
			hasItemsInside=true;
			for (int i=0;i<stack.getCount();i++) { //Split stacks bigger than 1 in multiple stacks
				ItemStack drop = new ItemStack(stack.getItem(), 1, stack.getMetadata());
				drop.setTagCompound(drop.getTagCompound());
				stacks.add(drop);
			}
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.removeTag("inv");//Don't need to know what items are inside on the client
		return tag;
	}

}
