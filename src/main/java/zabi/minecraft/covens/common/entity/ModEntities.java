package zabi.minecraft.covens.common.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

public class ModEntities {
	public static void registerAll() {
		Log.i("Registering entities");
		int id=0;
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MID, "brew"), BrewEntity.class, "thrown_brew", id++, Covens.INSTANCE, 64, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MID, "broom"), EntityFlyingBroom.class, "broom", id++, Covens.INSTANCE, 64, 1, true);
	}
}
