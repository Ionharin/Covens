package zabi.minecraft.covens.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.proxy.Proxy;
import zabi.minecraft.covens.common.ritual.rituals.ModRituals;
import zabi.minecraft.covens.common.tileentity.ModTileEntities;

@Mod(modid = Reference.MID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "[1.12]")
public class Covens {
	
	@Mod.Instance
	public static Covens INSTANCE;
	
	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static Proxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		ModBlocks.registerAll();
		ModItems.registerAll();
		ModRituals.registerAll();
		ModTileEntities.registerAll();
		proxy.registerItemModels();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		proxy.registerRenderingStuff();
	}
}
