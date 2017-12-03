package zabi.minecraft.covens.common;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zabi.minecraft.covens.common.block.BlockMoonbell;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.capability.AttachDataHandler;
import zabi.minecraft.covens.common.capability.EntityData;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.crafting.ModIRecipes;
import zabi.minecraft.covens.common.crafting.OreDict;
import zabi.minecraft.covens.common.crafting.VanillaRecipes;
import zabi.minecraft.covens.common.enchantment.ModEnchantments;
import zabi.minecraft.covens.common.entity.ModEntities;
import zabi.minecraft.covens.common.integration.CompatibilityModules;
import zabi.minecraft.covens.common.inventory.GuiHandler;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.network.NetworkModRegistry;
import zabi.minecraft.covens.common.patreon.ContributorDownloader;
import zabi.minecraft.covens.common.potion.ModPotions;
import zabi.minecraft.covens.common.proxy.Proxy;
import zabi.minecraft.covens.common.registries.brewing.ModBrewIngredients;
import zabi.minecraft.covens.common.registries.brewing.environmental.ModEnvironmentalPotionEffects;
import zabi.minecraft.covens.common.registries.chimney.ModChimneyRecipes;
import zabi.minecraft.covens.common.registries.fermenting.ModBarrelRecipes;
import zabi.minecraft.covens.common.registries.fortune.ModFortunes;
import zabi.minecraft.covens.common.registries.ritual.ModRituals;
import zabi.minecraft.covens.common.registries.spell.ModSpells;
import zabi.minecraft.covens.common.registries.threads.ModSpinningThreadRecipes;
import zabi.minecraft.covens.common.tileentity.ModTileEntities;

@Mod(modid = Reference.MID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", updateJSON=Reference.UPDATE_URL, dependencies="after:lilliputian")
@Mod.EventBusSubscriber
public class Covens {
	
	@Mod.Instance
	public static Covens INSTANCE;
	
	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static Proxy proxy;
	
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MID);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		proxy.setup();
		CompatibilityModules.preload();
		ModCreativeTabs.registerTabs();
		ModBlocks.registerAll();
		ModItems.registerAll();
		NetworkModRegistry.registerMessages(network);
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		ModEntities.registerAll();
		ModTileEntities.registerAll();
		ModChimneyRecipes.registerAll();
		ModRituals.registerAll();
		ModPotions.registerAll();
		ModBrewIngredients.registerAll();
		ModBarrelRecipes.registerAll();
		ModEnvironmentalPotionEffects.registerAll();
		ModSpinningThreadRecipes.registerAll();
		ModSpells.registerAll();
		ModIRecipes.registerAll();
		ModFortunes.registerAll();
		ModEnchantments.registerAll();
		proxy.preInit(evt);
		
		CapabilityManager.INSTANCE.register(EntityData.class, new EntityData.Storage(), EntityData.Impl.class);
		CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerData.Storage(), PlayerData.Impl.class);
		MinecraftForge.EVENT_BUS.register(new AttachDataHandler());
		
		Thread contributors = new Thread(new ContributorDownloader());
		contributors.setDaemon(true);
		contributors.setName("Covens: Patreon checker");
		contributors.start();
		
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt) {
		OreDict.registerAll();
		VanillaRecipes.registerAll();
		MinecraftForge.addGrassSeed(new ItemStack(ModItems.aconitumSeeds), 4);
		MinecraftForge.addGrassSeed(new ItemStack(ModItems.chrysanthemumSeeds), 2);
		MinecraftForge.addGrassSeed(new ItemStack(ModItems.helleboreSeeds), 4);
		MinecraftForge.addGrassSeed(new ItemStack(ModItems.sagebrushSeeds), 2);
		proxy.init(evt);
		CompatibilityModules.load();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		ForgeRegistries.BIOMES.getValues().parallelStream()
			.filter(b -> b.getBiomeName().toLowerCase().contains("roofed forest")).forEach(b -> {
			Log.i(b.getRegistryName().toString()+" is a valid moonbell biome");
			BlockMoonbell.addValidMoonbellBiome(b);
		});
	}
	
	@EventHandler
	public void setFlight(FMLServerStartedEvent evt) {
		FMLCommonHandler.instance().getMinecraftServerInstance().setAllowFlight(true);
	}
}
