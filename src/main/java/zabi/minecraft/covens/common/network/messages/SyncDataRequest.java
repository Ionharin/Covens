package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.CovensData;

public class SyncDataRequest implements IMessage {
	
	String uid;
	
	public SyncDataRequest() {
	}
	
	public SyncDataRequest(EntityLivingBase entity) {
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
	
	public static class Handler implements IMessageHandler<SyncDataRequest, SyncDataResponse> {

		@Override
		public SyncDataResponse onMessage(SyncDataRequest message, MessageContext ctx) {
			return new SyncDataResponse(message.uid, (NBTTagCompound) CovensData.CAPABILITY.getStorage().writeNBT(CovensData.CAPABILITY, ctx.getServerHandler().player.getCapability(CovensData.CAPABILITY, null), null));
		}

	}

}
