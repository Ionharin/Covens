package zabi.minecraft.covens.common.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.network.messages.SyncDataRequest;

public class AttachDataHandler {

	public static final ResourceLocation BASE_DATA = new ResourceLocation(Reference.MID, "covens_data");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityLivingBase) {
			Log.d("Injecting entityLivingBase capabilities");
			event.addCapability(BASE_DATA, new CovensData.Provider());
		}
	}


	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		EntityPlayer oldPlayer = event.getOriginal();
		CovensData newData = newPlayer.getCapability(CovensData.CAPABILITY, null);
		CovensData oldData = oldPlayer.getCapability(CovensData.CAPABILITY, null);
		newData.setTint(oldData.getTint());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent evt) {
		if (evt.getEntity() instanceof EntityLivingBase) {
			Covens.network.sendToServer(new SyncDataRequest((EntityLivingBase) evt.getEntity()));
		}
	}
}
