package zabi.minecraft.covens.common.network.messages;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.capability.PlayerData;

public class InfusionPowerChanged implements IMessage {

	private int power;
	private String player;

	public InfusionPowerChanged() {
	}
	
	public InfusionPowerChanged(EntityPlayer player, int power) {
		this.power = power;
		this.player = player.getUniqueID().toString();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		power = buf.readInt();
		player = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(power);
		ByteBufUtils.writeUTF8String(buf, player);
	}

	public static class Handler implements IMessageHandler<InfusionPowerChanged, IMessage> {

		@Override
		public IMessage onMessage(InfusionPowerChanged message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft(); 
			if (mc.world!=null) {
				EntityPlayer p = mc.world.getPlayerEntityByUUID(UUID.fromString(message.player));
				if (p!=null) {
					p.getCapability(PlayerData.CAPABILITY, null).setPower(message.power);
				}
			}
			return null;
		}

	}

}
