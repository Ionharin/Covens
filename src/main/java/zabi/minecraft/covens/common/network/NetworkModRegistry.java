package zabi.minecraft.covens.common.network;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zabi.minecraft.covens.common.network.messages.NotifyTint;
import zabi.minecraft.covens.common.network.messages.ScrollSpell;
import zabi.minecraft.covens.common.network.messages.SyncEntityDataRequest;
import zabi.minecraft.covens.common.network.messages.SyncEntityDataResponse;
import zabi.minecraft.covens.common.network.messages.SyncPlayerDataRequest;
import zabi.minecraft.covens.common.network.messages.SyncPlayerDataResponse;

public class NetworkModRegistry {

	public static void registerMessages(SimpleNetworkWrapper net) {
		
		Log.i("Registering network messages");
		
		int id=0;
		
		net.registerMessage(NotifyTint.Handler.class, NotifyTint.class, id++, Side.CLIENT);
		net.registerMessage(SyncEntityDataResponse.Handler.class, SyncEntityDataResponse.class, id++, Side.CLIENT);
		net.registerMessage(SyncPlayerDataResponse.Handler.class, SyncPlayerDataResponse.class, id++, Side.CLIENT);
		
		net.registerMessage(SyncEntityDataRequest.Handler.class, SyncEntityDataRequest.class, id++, Side.SERVER);
		net.registerMessage(SyncPlayerDataRequest.Handler.class, SyncPlayerDataRequest.class, id++, Side.SERVER);
		net.registerMessage(ScrollSpell.Handler.class, ScrollSpell.class, id++, Side.SERVER);
	}

}
