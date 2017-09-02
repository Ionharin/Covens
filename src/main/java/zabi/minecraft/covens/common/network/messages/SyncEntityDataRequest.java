package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.EntityData;

public class SyncEntityDataRequest implements IMessage {
	
	String uid;
	
	public SyncEntityDataRequest() {
	}
	
	public SyncEntityDataRequest(EntityLivingBase entity) {
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
	
	public static class Handler implements IMessageHandler<SyncEntityDataRequest, SyncEntityDataResponse> {

		@Override
		public SyncEntityDataResponse onMessage(SyncEntityDataRequest message, MessageContext ctx) {
			return new SyncEntityDataResponse(message.uid, (NBTTagCompound) EntityData.CAPABILITY.getStorage().writeNBT(EntityData.CAPABILITY, ctx.getServerHandler().player.getCapability(EntityData.CAPABILITY, null), null));
		}

	}

}
