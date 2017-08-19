package zabi.minecraft.covens.common.proxy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Proxy {
	public void setup() {};
	public void preInit(FMLPreInitializationEvent evt) {}
	public void init(FMLInitializationEvent evt) {}
	
	public void setupRenderHUD(World world, BlockPos pos) {};
	public boolean isFancyGraphicsEnabled() { return false; }
}
