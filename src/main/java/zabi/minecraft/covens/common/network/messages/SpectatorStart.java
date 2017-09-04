package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;

public class SpectatorStart implements IMessage {
	
	public SpectatorStart() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<SpectatorStart, IMessage> {

		@Override
		public IMessage onMessage(SpectatorStart message, MessageContext ctx) {
			Minecraft.getMinecraft().player.getCapability(PlayerData.CAPABILITY, null).setSpectatingPoint(new BlockPos(0,0,0));
			return null;
		}

	}

}
