package zabi.minecraft.covens.client.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.client.renderer.entity.RenderBrewThrown;
import zabi.minecraft.covens.client.renderer.entity.RenderBroom;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.entity.BrewEntity;
import zabi.minecraft.covens.common.entity.EntityFlyingBroom;
import zabi.minecraft.covens.common.item.ItemBrewDrinkable;
import zabi.minecraft.covens.common.item.ItemFlowers;
import zabi.minecraft.covens.common.item.ItemMisc;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.proxy.Proxy;

public class ClientProxy extends Proxy {
	
	private void registerEntityRenderers() {
//		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(BrewEntity.class, new RenderBrewThrown(Minecraft.getMinecraft().getRenderManager(), ModItems.brew_drinkable, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(BrewEntity.class, m -> new RenderBrewThrown(m, ModItems.brew_drinkable, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBroom.class, m -> new RenderBroom(m));
	}

	@Override
	public void setup() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		registerEntityRenderers();
	}
	
	@Override
	public void init(FMLInitializationEvent evt) {
		registerColors();
	}
	
	public void registerItemModels() {
		Log.i("Registering models");
		registerModel(ModItems.chalk, 0);
		registerModel(ModItems.chalk, 1);
		registerModel(ModItems.chalk, 2);
		registerModel(ModItems.chalk, 3);
		registerModel(ModItems.altar, 0);
		registerModel(ModItems.chimney, 0);
		registerModel(ModItems.cauldron, 0);
		registerModel(ModItems.brew_drinkable, 0);
		registerModel(ModItems.brew_drinkable, 1, 0);
		registerModel(ModItems.brew_gas, 0);
		registerModel(ModItems.brew_gas, 1, 0);
		registerModel(ModItems.ritual_knife,0);
		registerModel(ModItems.brew_lingering, 0);
		registerModel(ModItems.brew_lingering, 1, 0);
		registerModel(ModItems.brew_splash, 0);
		registerModel(ModItems.brew_splash, 1, 0);
		registerModel(ModItems.cardinal_stone, 0);
		registerModel(ModItems.cardinal_stone, 1);
		registerModel(ModItems.cardinal_stone, 2, 1);
		registerModel(ModItems.eerie_seeds, 0);
		registerModel(ModItems.helleboreSeeds, 0);
		registerModel(ModItems.aconitumSeeds, 0);
		registerModel(ModItems.sagebrushSeeds, 0);
		registerModel(ModItems.chrysanthemumSeeds, 0);
		registerModel(ModItems.log_elder, 0);
		registerModel(ModItems.log_juniper, 0);
		registerModel(ModItems.log_yew, 0);
		registerModel(ModItems.leaves_elder, 0);
		registerModel(ModItems.leaves_juniper, 0);
		registerModel(ModItems.leaves_yew, 0);
		registerModel(ModItems.yewPlanks,0);
		registerModel(ModItems.juniperPlanks,0);
		registerModel(ModItems.elderPlanks,0);
		registerModel(ModItems.goblet, 0);
		registerModel(ModItems.candle_plate, 0);
		registerModel(ModItems.soulstring, 0);
		registerModel(ModItems.soulstring, 1, 0);
		for (int i=0;i<ItemFlowers.names.length;i++) registerModel(ModItems.flowers, i);
		for (int i=0;i<ItemMisc.names.length;i++) if (i!=9) registerModel(ModItems.misc, i);
		registerModel(ModItems.misc, 9, 8);
	}

	private void registerModel(Item item, int meta) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+ (meta>0?("_"+meta):""));
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, mrl);
	}
	
	private void registerModel(Item item, int realMeta, int modelMeta) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+ (modelMeta>0?("_"+modelMeta):""));
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, realMeta, mrl);
	}
	

	private void registerColors() {
		BlockColors bc = Minecraft.getMinecraft().getBlockColors();
		ItemColors ic = Minecraft.getMinecraft().getItemColors();
		bc.registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				GlyphType type = state.getValue(BlockCircleGlyph.TYPE);
				switch (type) {
				case ENDER:
					return 0x770077;
				case GOLDEN:
					return 0xe3dc3c;
				case NETHER:
					return 0xbb0000;
				default:
				case NORMAL:
					return 0xFFFFFF;
				}
			}
		}, ModBlocks.glyphs);
				
		ic.registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				if (tintIndex==0) return ItemBrewDrinkable.getPotionColor(stack);
				return -1;
			}
		}, ModItems.brew_drinkable, ModItems.brew_gas, ModItems.brew_lingering, ModItems.brew_splash);
		
	}
	
	@SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
		registerItemModels();
		ModCreativeTabs.registerIcons();
    }
}
