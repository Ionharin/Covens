package zabi.minecraft.covens.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.entity.EntityCrystalBallObserver;
import zabi.minecraft.covens.common.entity.EntityPlayerShell;

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
			Entity spectating = ctx.getServerHandler().player.getSpectatingEntity();
			ctx.getServerHandler().player.setSpectatingEntity(null);
			if (spectating instanceof EntityCrystalBallObserver) spectating.setDead();
			PlayerData data = ctx.getServerHandler().player.getCapability(PlayerData.CAPABILITY, null);
			BlockPos pos = data.getSpectatingPoint();
			ctx.getServerHandler().player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
			data.setSpectatingPoint(null);
			EntityPlayerShell.removePlayerShells(ctx.getServerHandler().player);
			return null;
		}

	}

}
