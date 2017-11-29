package zabi.minecraft.covens.common.tileentity;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.util.machines.AutomatableInventory;

public class TileEntitySilverVat extends TileEntityBase {
	
	Random rng = new Random();
	
	private int acid = 0;
	private AutomatableInventory inv = new AutomatableInventory(5) {
		
		@Override
		public void onMarkDirty() {
			markTileDirty();
		}
		
		@Override
		public boolean canMachineInsert(int slot, ItemStack stack) {
			return (slot==2 && stack.getItem()==Item.getItemFromBlock(Blocks.GOLD_ORE)) || (slot==4 && stack.getItem()==Items.GUNPOWDER);
		}
		
		@Override
		public boolean canMachineExtract(int slot, ItemStack stack) {
			return (slot == 0 ) || (slot == 1) || (slot == 3);
		}
	};

	public TileEntitySilverVat() {
	}

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		inv.loadFromNBT(tag.getCompoundTag("inventory"));
		acid = tag.getInteger("acid");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setTag("inventory", inv.saveToNbt());
		tag.setInteger("acid", acid);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
		return super.getCapability(capability, facing);
	}

	public void randomTick() {
		if (acid==0) {
			if (!inv.decrStackSize(4, 1).isEmpty()) {
				acid+=18;
			}
		} else {
			if (!inv.getStackInSlot(2).isEmpty()) {
				ItemStack s0 = inv.getStackInSlot(0);
				ItemStack s1 = inv.getStackInSlot(1);
				ItemStack s3 = inv.getStackInSlot(3);
				if (s0.getCount()<64 && s1.getCount()<64 && s3.getCount()<64) {
					if (!world.isRemote) {
						acid--;
						inv.decrStackSize(2, 1);
						int yield = rng.nextInt(8);
						ItemStack silv = new ItemStack(ModItems.misc, yield, 17);
						ItemStack gold = new ItemStack(Items.GOLD_NUGGET, 8-yield);
						if (s0.isEmpty()) inv.setInventorySlotContents(0, silv);
						else s0.grow(silv.getCount());
						if (s1.isEmpty()) inv.setInventorySlotContents(1, gold);
						else s1.grow(gold.getCount());
						if (s3.isEmpty()) inv.setInventorySlotContents(3, new ItemStack(Blocks.COBBLESTONE));
						else s3.grow(1);
						if (s0.getCount()>64) s0.setCount(64);
						if (s1.getCount()>64) s1.setCount(64);
						if (s3.getCount()>64) s3.setCount(64);
					}
				}
			}
		}
		markDirty();
	}

	protected void markTileDirty() {
		this.markDirty();
	}
	
	public IInventory getInventory() {
		return inv;
	}
	
	public int getAcidLevel() {
		return acid;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return newSate.getBlock()!=ModBlocks.silver_vat;
	}

	@Override
	protected void NBTSaveUpdate(NBTTagCompound tag) {
		tag.setInteger("acid", acid);
	}

	@Override
	protected void NBTLoadUpdate(NBTTagCompound tag) {
		acid = tag.getInteger("acid");
	}
	
}
