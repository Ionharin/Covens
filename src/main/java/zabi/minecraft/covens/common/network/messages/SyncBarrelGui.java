package zabi.minecraft.covens.common.network.messages;

import zabi.minecraft.covens.common.lib.Log;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.covens.common.registries.fermenting.BarrelRecipe;
import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;

public class SyncBarrelGui implements IMessage {
	
	int x,y,z, brewTime, powerAbsorbed;
	String recipe;
	
	public SyncBarrelGui() {
	}
	
	public SyncBarrelGui(BlockPos pos, int brewTime, int powerAbsorbed, String recipe) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		this.brewTime = brewTime;
		this.powerAbsorbed = powerAbsorbed;
		this.recipe = recipe;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		brewTime = buf.readInt();
		powerAbsorbed = buf.readInt();
		recipe = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(brewTime);
		buf.writeInt(powerAbsorbed);
		ByteBufUtils.writeUTF8String(buf, recipe);
	}
	
	public static class Handler implements IMessageHandler<SyncBarrelGui, IMessage> {

		@Override
		public IMessage onMessage(SyncBarrelGui message, MessageContext ctx) {
			TileEntity tex = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (tex instanceof TileEntityBarrel) {
				TileEntityBarrel te = (TileEntityBarrel) tex;
				te.setField(0, message.brewTime);
				te.setField(1, message.powerAbsorbed);
				if (message.recipe==null || message.recipe.length()==0) te.refreshRecipeStatus(BarrelRecipe.REGISTRY.getValue(new ResourceLocation(message.recipe)));
				te.markDirty();
				Log.i("Updated TE");
			} else {
				Log.w("Wrong SyncBarrelGui message received");
			}
			return null;
		}

	}

}
