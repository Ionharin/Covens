package zabi.minecraft.covens.common.network.messages;

import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.entity.EntityPlayerShell;

public class ChangeShellOwner implements IMessage {

	private String entity, caster;

	public ChangeShellOwner() {
	}
	
	public ChangeShellOwner(EntityPlayerShell entity, String caster) {
		this.entity = entity.getUniqueID().toString();
		this.caster = caster;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entity = ByteBufUtils.readUTF8String(buf);
		caster = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, entity);
		ByteBufUtils.writeUTF8String(buf, caster);
	}

	public static class Handler implements IMessageHandler<ChangeShellOwner, IMessage> {

		@Override
		public IMessage onMessage(ChangeShellOwner message, MessageContext ctx) {
			List<Entity> list = Collections.unmodifiableList(Minecraft.getMinecraft().world.loadedEntityList);
			list.parallelStream().filter(e -> e.getUniqueID().toString().equals(message.entity)).map(e -> (EntityPlayerShell) e).forEach(e -> {
				e.setCaster(message.caster);
			});
			return null;
		}

	}

}
