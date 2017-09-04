package zabi.minecraft.covens.common.tileentity;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;


public abstract class TileEntityBase extends TileEntity {

	private static final String UNIQUE_TAG = Reference.MID;

	public TileEntityBase() {
		super();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (!tag.hasKey(UNIQUE_TAG)) tag.setTag(UNIQUE_TAG, new NBTTagCompound());
		NBTTagCompound tagData = (NBTTagCompound) tag.getTag(UNIQUE_TAG);
		NBTLoad(tagData);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		NBTTagCompound tagData = new NBTTagCompound();
		NBTSave(tagData);
		tag.setTag(UNIQUE_TAG, tagData);
		return tag;
	}

	protected abstract void NBTLoad(NBTTagCompound tag);

	protected abstract void NBTSave(NBTTagCompound tag);

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new SPacketUpdateTileEntity(this.pos, 0, nbt);

	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT((pkt.getNbtCompound()));
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		return writeToNBT(tag);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		readFromNBT(tag);
	}

}
