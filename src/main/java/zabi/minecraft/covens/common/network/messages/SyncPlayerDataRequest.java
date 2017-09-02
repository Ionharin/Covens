package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;

public class SyncPlayerDataRequest implements IMessage {
	
	String uid;
	
	public SyncPlayerDataRequest() {
	}
	
	public SyncPlayerDataRequest(EntityPlayer entity) {
		uid = entity.getUniqueID().toString();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		uid = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, uid);
	}
	
	public static class Handler implements IMessageHandler<SyncPlayerDataRequest, SyncPlayerDataResponse> {

		@Override
		public SyncPlayerDataResponse onMessage(SyncPlayerDataRequest message, MessageContext ctx) {
			return new SyncPlayerDataResponse(message.uid, (NBTTagCompound) PlayerData.CAPABILITY.getStorage().writeNBT(PlayerData.CAPABILITY, ctx.getServerHandler().player.getCapability(PlayerData.CAPABILITY, null), null));
		}

	}

}
