package zabi.minecraft.covens.common.network.messages;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.EntityData;

public class SyncEntityDataResponse implements IMessage {
	
	NBTTagCompound data;
	String id;
	
	public SyncEntityDataResponse() {
	}
	
	public SyncEntityDataResponse(String uid, NBTTagCompound data) {
		this.data = data;
		id = uid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
		id = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
		ByteBufUtils.writeUTF8String(buf, id);
	}
	
	public static class Handler implements IMessageHandler<SyncEntityDataResponse, IMessage> {

		@Override
		public IMessage onMessage(SyncEntityDataResponse message, MessageContext ctx) {
			List<Entity> list = new ArrayList<Entity>(Minecraft.getMinecraft().world.loadedEntityList);
			list.parallelStream().filter(e -> e.getUniqueID().toString().equals(message.id)).forEach(e -> {
				EntityData.CAPABILITY.getStorage().readNBT(EntityData.CAPABILITY, e.getCapability(EntityData.CAPABILITY, null), null, message.data);
			});
			return null;
		}

	}

}
