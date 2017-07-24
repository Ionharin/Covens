package zabi.minecraft.covens.common.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Proxy {
	public void setup() {};
	public void preInit(FMLPreInitializationEvent evt) {}
	public void init(FMLInitializationEvent evt) {}
}
