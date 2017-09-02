package zabi.minecraft.covens.common.network.messages;

import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.EntityData;

public class NotifyTint implements IMessage {

	private int color;
	private String entity;

	public NotifyTint() {
	}
	
	public NotifyTint(EntityLivingBase entity, int color) {
		this.color = color;
		this.entity = entity.getUniqueID().toString();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		color = buf.readInt();
		entity = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(color);
		ByteBufUtils.writeUTF8String(buf, entity);
	}

	public static class Handler implements IMessageHandler<NotifyTint, IMessage> {

		@Override
		public IMessage onMessage(NotifyTint message, MessageContext ctx) {
			List<Entity> list = Collections.unmodifiableList(Minecraft.getMinecraft().world.loadedEntityList);
			list.parallelStream().filter(e -> e.getUniqueID().toString().equals(message.entity)).forEach(e -> e.getCapability(EntityData.CAPABILITY, null).setTint(message.color));
			return null;
		}

	}

}
