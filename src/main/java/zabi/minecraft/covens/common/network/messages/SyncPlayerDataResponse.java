package zabi.minecraft.covens.common.network.messages;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;

public class SyncPlayerDataResponse implements IMessage {
	
	NBTTagCompound data;
	String id;
	
	public SyncPlayerDataResponse() {
	}
	
	public SyncPlayerDataResponse(String uid, NBTTagCompound data) {
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
	
	public static class Handler implements IMessageHandler<SyncPlayerDataResponse, IMessage> {

		@Override
		public IMessage onMessage(SyncPlayerDataResponse message, MessageContext ctx) {
			EntityPlayer witch = Minecraft.getMinecraft().world.getPlayerEntityByUUID(UUID.fromString(message.id));
			if (witch!=null) PlayerData.CAPABILITY.getStorage().readNBT(PlayerData.CAPABILITY, witch.getCapability(PlayerData.CAPABILITY, null), null, message.data);
			return null;
		}

	}

}
