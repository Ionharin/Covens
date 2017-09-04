package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;

public class SpectatorStop implements IMessage {
	
	public SpectatorStop() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<SpectatorStop, IMessage> {

		@Override
		public IMessage onMessage(SpectatorStop message, MessageContext ctx) {
			ctx.getServerHandler().player.setSpectatingEntity(null);
			PlayerData data = ctx.getServerHandler().player.getCapability(PlayerData.CAPABILITY, null);
			BlockPos pos = data.getSpectatingPoint();
			ctx.getServerHandler().player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
			data.setSpectatingPoint(null);
			return null;
		}

	}

}
