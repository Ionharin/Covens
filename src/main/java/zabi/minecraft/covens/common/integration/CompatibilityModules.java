package zabi.minecraft.covens.common.integration;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraftforge.fml.common.Loader;

public class CompatibilityModules {
	
	public static ExtraAlchemy ea;
	public static Lilliputian lpt;
	
	public static void preload() {
		if (Loader.isModLoaded("extraalchemy")) {
			Log.i("Extra Alchemy found! Loading module");
			ea=new ExtraAlchemy();
		}
		if (Loader.isModLoaded("lilliputian")) {
			Log.i("Lilliputian found! Loading module");
			lpt=new Lilliputian();
		}
	}
	
	public static void load() {
		if (ea!=null) ea.load();
		if (lpt!=null) lpt.load();
	}
	
}
