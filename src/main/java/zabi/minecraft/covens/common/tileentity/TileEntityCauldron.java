package zabi.minecraft.covens.common.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.block.BlockCauldron;
import zabi.minecraft.covens.common.registries.brewing.PotionDigester;

public class TileEntityCauldron extends TileEntityBase implements IAltarUser {
	
	public void setNoLiquid() {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockCauldron.FULL, false), 3);
	}

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
		if (world.getBlockState(pos).getValue(BlockCauldron.FULL)) {
			if (pickArea==null) pickArea = new AxisAlignedBB(pos).shrink(0.35).contract(0, 0.4, 0);
			for (EntityItem entityIn:world.getEntitiesWithinAABB(EntityItem.class, pickArea)) {
				if (world.getTileEntity(pos)!=null && world.getBlockState(pos.down()).getBlock().equals(Blocks.FIRE)) {
					EntityItem ei = (EntityItem) entityIn;
					TileEntityCauldron tec = (TileEntityCauldron) world.getTileEntity(pos);
					tec.dropItemInside(ei.getItem());
					ei.setDead();
					this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 0.8F, 1f, false);
				}
			}
		}
	}	
	
	public ItemStack getResult() {
		return PotionDigester.digestPotion(stacks);
	}
	
	public void emptyContents() {
		stacks = NonNullList.<ItemStack>create();
		hasItemsInside = false;
		setNoLiquid();
		markDirty();
	}

	public void dropItemInside(ItemStack stack) {
		if (!stack.isEmpty()) {
			if (stack.getItem().equals(Items.CLAY_BALL)) {
				emptyContents();
			} else {
				hasItemsInside=true;
				for (int i=0;i<stack.getCount();i++) { //Split stacks bigger than 1 in multiple stacks
					ItemStack drop = new ItemStack(stack.getItem(), 1, stack.getMetadata());
					drop.setTagCompound(drop.getTagCompound());
					stacks.add(drop);
				}
			}
			markDirty();
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.removeTag("inv");//Don't need to know what items are inside on the client
		return tag;
	}
	
	public boolean getHasItems() {
		return hasItemsInside;
	}
	
	public boolean consumePower(int power, boolean simulate) {
		if (power==0) return true;
		if (te==null || te.isInvalid()) te = TileEntityAltar.getClosest(pos, world);
		if (te==null) return false;
		return te.consumePower(power, simulate);
	}
	
	@Override
	public TileEntityAltar getAltar(boolean rebind) {
		if ((te==null || te.isInvalid()) && rebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}
}
