package zabi.minecraft.covens.client.proxy;

import java.awt.Color;
import java.util.Random;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEndRod;
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
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.client.gui.RenderCandleData;
import zabi.minecraft.covens.client.gui.RenderInfusionBar;
import zabi.minecraft.covens.client.gui.ScrollHijacker;
import zabi.minecraft.covens.client.gui.books.BookManager;
import zabi.minecraft.covens.client.renderer.entity.RenderBody;
import zabi.minecraft.covens.client.renderer.entity.RenderBrewThrown;
import zabi.minecraft.covens.client.renderer.entity.RenderBroom;
import zabi.minecraft.covens.client.renderer.entity.RenderObserver;
import zabi.minecraft.covens.client.renderer.entity.RenderSpell;
import zabi.minecraft.covens.client.renderer.entity.TintModifier;
import zabi.minecraft.covens.common.block.BlockBarrel;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.block.BlockModSapling.EnumSaplingType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.entity.EntityBrew;
import zabi.minecraft.covens.common.entity.EntityCrystalBallObserver;
import zabi.minecraft.covens.common.entity.EntityFlyingBroom;
import zabi.minecraft.covens.common.entity.EntityPlayerBody;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;
import zabi.minecraft.covens.common.item.ItemBrewDrinkable;
import zabi.minecraft.covens.common.item.ItemFlowers;
import zabi.minecraft.covens.common.item.ItemMisc;
import zabi.minecraft.covens.common.item.ItemSpellPage;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.proxy.Proxy;
import zabi.minecraft.covens.common.registries.spell.Spell;

public class ClientProxy extends Proxy {
	
	private void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBrew.class, m -> new RenderBrewThrown(m, ModItems.brew_drinkable, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBroom.class, m -> new RenderBroom(m));
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellCarrier.class, m -> new RenderSpell(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityCrystalBallObserver.class, m -> new RenderObserver(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerBody.class, m -> new RenderBody(m));
	}

	@Override
	public void setup() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new TintModifier());
		MinecraftForge.EVENT_BUS.register(new ScrollHijacker());
		MinecraftForge.EVENT_BUS.register(new RenderInfusionBar());
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		registerEntityRenderers();
	}
	
	@Override
	public void init(FMLInitializationEvent evt) {
		registerColors();
		BookManager.registerPagesReloadListener();
	}
	
	public void registerItemModels() {
		Log.i("Registering models");
		
		registerModels(ModItems.chalk, ModItems.altar, ModItems.chimney, ModItems.cauldron, ModItems.brew_drinkable,
				ModItems.ritual_knife, ModItems.brew_lingering, ModItems.brew_splash, ModItems.cardinal_stone,
				ModItems.eerie_seeds, ModItems.helleboreSeeds, ModItems.aconitumSeeds, ModItems.sagebrushSeeds, 
				ModItems.chrysanthemumSeeds, ModItems.log_elder, ModItems.log_juniper, ModItems.log_yew,
				ModItems.leaves_elder, ModItems.leaves_juniper, ModItems.leaves_yew, ModItems.planks_yew, 
				ModItems.planks_juniper, ModItems.planks_elder, ModItems.goblet, ModItems.candle_plate, ModItems.soulstring, 
				ModItems.candle, ModItems.thread_spinner, ModItems.spell_page, ModItems.grimoire, ModItems.crystal_ball, 
				ModItems.ritual_candle, ModItems.talisman_aquamarine_crown, ModItems.talisman_emerald_pendant, 
				ModItems.talisman_diamond_star, ModItems.talisman_ruby_orb, ModItems.talisman_watching_eye, ModItems.moonbell, 
				ModItems.silver_vat
		);
		
		registerModel(ModItems.chalk, 1);
		registerModel(ModItems.chalk, 2);
		registerModel(ModItems.chalk, 3);
		registerModel(ModItems.brew_drinkable, 1, 0);
		registerModel(ModItems.brew_lingering, 1, 0);
		registerModel(ModItems.brew_splash, 1, 0);
		registerModel(ModItems.cardinal_stone, 1);
		registerModel(ModItems.cardinal_stone, 2, 1);
		registerModel(ModItems.soulstring, 1, 0);
		registerModel(ModItems.candle, 1, 0);
		for (int i=0;i<4;i++) registerModel(ModItems.broom, i);
		for (int i=0;i<ItemFlowers.names.length;i++) registerModel(ModItems.flowers, i);
		for (int i=0;i<ItemMisc.names.length;i++) if (i!=9) registerModel(ModItems.misc, i);
		for (int i=0;i<BlockBarrel.WoodType.values().length;i++) registerModel(ModItems.barrel, i);
		registerModel(ModItems.misc, 9, 8);
		for (EnumSaplingType est:EnumSaplingType.values()) registerModel(ModItems.sapling, est.ordinal(), est.getName());
	}

	private void registerModel(Item item, int meta) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+ (meta>0?("_"+meta):""));
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, mrl);
	}
	
	private void registerModels(Item... items) {
		for (int i=0;i<items.length;i++) {
			registerModel(items[i],0);
		}
	}
	
	private void registerModel(Item item, int realMeta, int modelMeta) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+ (modelMeta>0?("_"+modelMeta):""));
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, realMeta, mrl);
	}
	
	private void registerModel(Item item, int meta, String variant) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+"_"+variant);
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, mrl);
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
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (tintIndex==0) return ItemBrewDrinkable.getPotionColor(stack);
				return -1;
			}
		}, ModItems.brew_drinkable, /*ModItems.brew_gas,*/ ModItems.brew_lingering, ModItems.brew_splash);
		
		ic.registerItemColorHandler(new IItemColor() {
			
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (tintIndex==0) {
					Spell s = ItemSpellPage.getSpellFromItemStack(stack);
					if (s!=null) return s.getColor();
				}
				return -1;
			}
		}, ModItems.spell_page);

		ic.registerItemColorHandler(new IItemColor() {

			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (tintIndex==0) {
					return Minecraft.getMinecraft().world!=null?Color.HSBtoRGB(Minecraft.getMinecraft().world.getTotalWorldTime() % 180 / 180f, 0.6f, 0.8f):-1;
				}
				return -1;
			}
		}, ModItems.grimoire);
		
		bc.registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				if (tintIndex==1) {
					return Color.HSBtoRGB((float)((pos.getX()+pos.getY()+pos.getZ())%50)/50f, 0.4f, 1f);
				}
				return -1;
			}
		}, ModBlocks.crystal_ball);
	}

	@SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
		registerItemModels();
		ModCreativeTabs.registerIcons();
    }
	
	@Override
	public void setupRenderHUD(World world, BlockPos pos) {
		RenderCandleData.INSTANCE.setup(pos, world);
	}
	
	@Override
	public boolean isFancyGraphicsEnabled() {
		return Minecraft.getMinecraft().gameSettings.fancyGraphics;
	}
	
	@Override
	public void spawnParticleExplosionSpell(double posX, double posY, double posZ, Random rand) {
		Particle p = new ParticleEndRod(Minecraft.getMinecraft().world, posX, posY, posZ, 0.2*rand.nextGaussian(), 0.2*rand.nextGaussian(), 0.2*rand.nextGaussian());
		p.setMaxAge(15);
		spawnParticle(p);
	}
	
	@Override
	public void spawnParticle(Particle p) {
		Minecraft.getMinecraft().effectRenderer.addEffect(p);
	}
}
