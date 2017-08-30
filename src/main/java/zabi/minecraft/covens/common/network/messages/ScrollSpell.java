package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.item.ItemGrimoire;

public class ScrollSpell implements IMessage {
	
	int direction;
	
	public ScrollSpell() {
	}
	
	public ScrollSpell(int direction) {
		this.direction = direction;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		direction=buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(direction);
	}
	
	public static class Handler implements IMessageHandler<ScrollSpell, IMessage> {

		@Override
		public IMessage onMessage(ScrollSpell message, MessageContext ctx) {
			ItemGrimoire.scrollSpell(ctx.getServerHandler().player.inventory.getCurrentItem(), message.direction);
			return null;
		}

	}

}
