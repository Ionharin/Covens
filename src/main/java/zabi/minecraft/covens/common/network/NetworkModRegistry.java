package zabi.minecraft.covens.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zabi.minecraft.covens.common.network.messages.NotifyTint;
import zabi.minecraft.covens.common.network.messages.SyncDataRequest;
import zabi.minecraft.covens.common.network.messages.SyncDataResponse;

public class NetworkModRegistry {

	public static void registerMessages(SimpleNetworkWrapper net) {
		int id=0;
		
		net.registerMessage(NotifyTint.Handler.class, NotifyTint.class, id++, Side.CLIENT);
		net.registerMessage(SyncDataResponse.Handler.class, SyncDataResponse.class, id++, Side.CLIENT);
		
		net.registerMessage(SyncDataRequest.Handler.class, SyncDataRequest.class, id++, Side.SERVER);
	}

}
