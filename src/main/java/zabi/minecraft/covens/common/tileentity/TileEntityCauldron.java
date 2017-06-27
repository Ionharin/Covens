package zabi.minecraft.covens.common.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.covens.common.crafting.brewing.BrewData;
import zabi.minecraft.covens.common.crafting.brewing.PotionDigester;

public class TileEntityCauldron extends TileEntityBase {
	
	private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>create();
	private boolean hasItemsInside = false;
	private AxisAlignedBB pickArea = null;
	private TileEntityAltar te = null;

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		NBTTagCompound inv = tag.getCompoundTag("inv");
		NonNullList<ItemStack> stacksTemp = NonNullList.<ItemStack>withSize(inv.getKeySet().size(), ItemStack.EMPTY);
		for (String s:inv.getKeySet()) {
			stacksTemp.set(Integer.parseInt(s), new ItemStack(inv.getCompoundTag(s)));
		}
		for (ItemStack is:stacksTemp) {
			if (!is.isEmpty()) stacks.add(is);
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
		if (pickArea==null) pickArea = new AxisAlignedBB(pos.up()).contract(0, 0.8, 0);
		for (EntityItem entityIn:world.getEntitiesWithinAABB(EntityItem.class, pickArea)) {
			if (world.getTileEntity(pos)!=null && world.getBlockState(pos.down()).getBlock().equals(Blocks.FIRE)) {
				EntityItem ei = (EntityItem) entityIn;
				TileEntityCauldron tec = (TileEntityCauldron) world.getTileEntity(pos);
				tec.dropItemInside(ei.getItem());
				ei.setDead();
			}
		}
	}	
	
	public BrewData getResult() {
		return PotionDigester.digestPotion(stacks);
	}
	
	public void emptyContents() {
		stacks = NonNullList.<ItemStack>create();
		hasItemsInside = false;
	}

	public void dropItemInside(ItemStack stack) {
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
	
	public boolean canTakePotion() {
		return hasItemsInside;
	}
	
	public boolean getHasItems() {
		return hasItemsInside;
	}
	
	public boolean consumePower(int power, boolean simulate) {
		final BlockPos pos = getPos();
		if (te==null || te.isInvalid()) te = getWorld().loadedTileEntityList.stream()
		.filter(te -> (te instanceof TileEntityAltar))
		.filter(te -> te.getDistanceSq(pos.getX(), pos.getY(), pos.getZ() ) <= 256)
		.map(te -> (TileEntityAltar) te)
		.filter(te -> te.getAltarPower()>=power)
		.findFirst().orElse(null);
		if (te==null) return false;
		return te.consumePower(power, simulate);
	}

}
