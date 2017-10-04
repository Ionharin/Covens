package zabi.minecraft.covens.common.integration;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraftforge.fml.common.Loader;

public class CompatibilityModules {
	
	private static ExtraAlchemy ea;
	
	public static void preload() {
		if (Loader.isModLoaded("extraalchemy")) {
			Log.i("Extra Alchemy found! Loading module");
			ea=new ExtraAlchemy();
		}
	}
	
	public static void load() {
		if (ea!=null) ea.load();
	}
	
}
