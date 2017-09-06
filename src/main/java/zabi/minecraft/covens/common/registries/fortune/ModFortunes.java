package zabi.minecraft.covens.common.registries.fortune;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.registries.fortune.fortunes.FortuneMeetZombie;

public class ModFortunes {
	
	private static Fortune zombie;
	
	public static void registerAll() {
		Log.i("Creating fortunes");
		MinecraftForge.EVENT_BUS.register(new ModFortunes());
		zombie = new FortuneMeetZombie(10, "meet_zombie", Reference.MID);
	}
	
	@SubscribeEvent
	public void onRegisterFortunes(RegistryEvent.Register<Fortune> evt) {
		Log.i("Registering fortunes");
		evt.getRegistry().registerAll(
				zombie
		);
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent evt) {
		if (evt.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
			PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
			Fortune f = data.getFortune();
			if (f!=null) {
				if (f.canShouldBeAppliedNow(player)) {
					f.apply(player);
					data.setFortune(null);
				}
			}
		}
	}
	
}
