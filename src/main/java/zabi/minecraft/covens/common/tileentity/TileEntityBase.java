package zabi.minecraft.covens.common.tileentity;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.lib.Reference;


public abstract class TileEntityBase extends TileEntity implements ITickable {

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

	protected abstract void tick();

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
	public void update() {
//		if (!Config.disableTileEntities) tick();
		tick();
	}

	@SideOnly(Side.CLIENT)
	public int getUniqueAnimationDiscriminator() {
		return pos.getX()+pos.getY()+pos.getZ();
	}

}
