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
import zabi.minecraft.covens.common.network.messages.SyncEntityDataRequest;
import zabi.minecraft.covens.common.network.messages.SyncPlayerDataRequest;

public class AttachDataHandler {

	public static final ResourceLocation ENTITY_DATA = new ResourceLocation(Reference.MID, "covens_entity_data");
	public static final ResourceLocation PLAYER_DATA = new ResourceLocation(Reference.MID, "covens_player_data");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityLivingBase) {
			Log.d("Injecting entityLivingBase capabilities");
			event.addCapability(ENTITY_DATA, new CovensData.Provider());
			if (event.getObject() instanceof EntityPlayer) {
				Log.d("Injecting entityPlayer capabilities");
				event.addCapability(PLAYER_DATA, new WitchData.Provider());
			}
		}
	}


	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		EntityPlayer oldPlayer = event.getOriginal();
		CovensData newEntityData = newPlayer.getCapability(CovensData.CAPABILITY, null);
		CovensData oldEntityData = oldPlayer.getCapability(CovensData.CAPABILITY, null);
		WitchData newPlayerData = newPlayer.getCapability(WitchData.CAPABILITY, null);
		WitchData oldPlayerData = oldPlayer.getCapability(WitchData.CAPABILITY, null);
		newEntityData.setTint(oldEntityData.getTint());
		newPlayerData.setInfusion(oldPlayerData.getInfusion());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent evt) {
		if (evt.getEntity() instanceof EntityLivingBase) {
			Covens.network.sendToServer(new SyncEntityDataRequest((EntityLivingBase) evt.getEntity()));
			if (evt.getEntity() instanceof EntityPlayer) {
				Covens.network.sendToServer(new SyncPlayerDataRequest((EntityPlayer) evt.getEntity()));
			}
		}
	}
}
