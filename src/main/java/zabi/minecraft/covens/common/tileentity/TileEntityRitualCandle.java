package zabi.minecraft.covens.common.tileentity;

import java.util.UUID;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import zabi.minecraft.covens.common.capability.IRitualHandler;
import zabi.minecraft.covens.common.registries.ritual.Ritual;

public class TileEntityRitualCandle extends TileEntityBaseTickable implements IRitualHandler {

	private Ritual ritual = null;
	private int cooldown = 0, waxLeft = 10000;
	private UUID entityPlayer;
	private NBTTagCompound ritualData = null;
	private int internalAP = 3000;
	private boolean lit = false, depleted = false;

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		cooldown = tag.getInteger("cooldown");
		internalAP = tag.getInteger("internalAP");
		lit = tag.getBoolean("lit");
		depleted = tag.getBoolean("depleted");
		waxLeft = tag.getInteger("waxLeft");
		if (tag.hasKey("ritual")) ritual = Ritual.REGISTRY.getValue(new ResourceLocation(tag.getString("ritual")));
		if (tag.hasKey("player")) entityPlayer = UUID.fromString(tag.getString("player"));
		if (tag.hasKey("data")) ritualData = tag.getCompoundTag("data");
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		tag.setInteger("cooldown", cooldown);
		tag.setInteger("internalAP", internalAP);
		tag.setBoolean("lit", lit);
		tag.setBoolean("depleted", depleted);
		tag.setInteger("waxLeft", waxLeft);
		if (ritual!=null) tag.setString("ritual", ritual.getRegistryName().toString());
		if (entityPlayer!=null) tag.setString("player", entityPlayer.toString());
		if (ritualData!=null) tag.setTag("data", ritualData);
	}

	@Override
	protected void tick() {
		if (lit) {
			waxLeft--;
			if (ritual!=null) {
				EntityPlayer player = getWorld().getPlayerEntityByUUID(entityPlayer);
				boolean hasPowerToUpdate = consumePower(ritual.getRunningPower());
				if (ritual.getTime()>=0 && hasPowerToUpdate) cooldown++;
				if (ritual.getTime()<=cooldown && ritual.getTime()>=0) {
					ritual.onFinish(player, this, getWorld(), getPos(), ritualData);
					deplete();
					Log.d("Ritual Finished: "+ritual.getRegistryName());
					if (!getWorld().isRemote) for (ItemStack stack : ritual.getOutput()) {
						EntityItem ei = new EntityItem(getWorld(), getPos().getX(), getPos().up().getY(), getPos().getZ(), stack);
						getWorld().spawnEntity(ei);
					}
					entityPlayer = null;
					cooldown = 0;
					ritual = null;
					return;
				}
				if (hasPowerToUpdate) {
					ritual.onUpdate(player, this, getWorld(), getPos(), ritualData, cooldown);
				} else {
					ritual.onLowPower(player, this, world, pos, ritualData, cooldown);
					deplete();
				}
			}
			if (waxLeft<=0) deplete();
		}
	}

	private void deplete() {
		if (!depleted) {
			this.world.playEvent(2001, pos, Block.getStateId(this.world.getBlockState(getPos())));
			this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			depleted=true;
		}
	}

	public void startRitual(EntityPlayer player, NonNullList<ItemStack> itemsUsed) {
		if (player.getEntityWorld().isRemote) return;
		this.ritualData = new NBTTagCompound();
		NBTTagCompound itemsUsedTag = new NBTTagCompound();
		ritualData.setTag("itemsUsed", itemsUsedTag);
		itemsUsed.forEach(ei -> {
			NBTTagCompound item = new NBTTagCompound();
			ei.writeToNBT(item);
			ritualData.getCompoundTag("itemsUsed").setTag("item"+ritualData.getCompoundTag("itemsUsed").getKeySet().size(), item);
		});
		ritual.onStarted(player, this, getWorld(), getPos(), ritualData);
		player.sendStatusMessage(new TextComponentTranslation("ritual."+ritual.getRegistryName().toString().replace(':', '.')+".name", new Object[0]), true);
		world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
		markDirty();
		Log.d("Ritual Started: "+ritual.getRegistryName());
		return;
	}


	public void stopRitual(EntityPlayer player) {
		if (ritual!=null && !this.isInvalid()) {
			ritual.onStopped(player, this, world, pos, ritualData);
			deplete();
		}
	}
	
	public boolean hasRunningRitual() {
		return ritual!=null;
	}
	
	public boolean consumePower(int power) {
		if (power==0) return true;
		if (internalAP<power) return false;
		internalAP-=power;
		markDirty();
		return true;
	}
	
	@Override
	public void invalidate() {
		stopRitual(null);
		super.invalidate();
	}

	public boolean isLit() {
		return lit;
	}

	public void setLit(boolean lit) {
		this.lit = lit;
	}
}
