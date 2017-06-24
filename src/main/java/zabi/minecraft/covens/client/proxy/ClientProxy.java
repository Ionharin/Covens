package zabi.minecraft.covens.client.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.item.ItemFlowers;
import zabi.minecraft.covens.common.item.ItemMisc;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.proxy.Proxy;

public class ClientProxy extends Proxy {
	@Override
	public void registerRenderingStuff() {
		registerBlockColors();
	}

	@Override
	public void registerItemModels() {
		registerModel(ModItems.chalk, 0);
		registerModel(ModItems.chalk, 1);
		registerModel(ModItems.chalk, 2);
		registerModel(ModItems.chalk, 3);
		registerModel(ModItems.altar, 0);
		registerModel(ModItems.chimney, 0);
		registerModel(ModItems.eerie_seeds, 0);
		registerModel(ModItems.helleboreSeeds, 0);
		registerModel(ModItems.aconitumSeeds, 0);
		registerModel(ModItems.sagebrushSeeds, 0);
		registerModel(ModItems.chrysanthemumSeeds, 0);
		for (int i=0;i<ItemFlowers.names.length;i++) registerModel(ModItems.flowers, i);
		for (int i=0;i<ItemMisc.names.length;i++) registerModel(ModItems.misc, i);
	}

	private void registerModel(Item item, int meta) {
		ResourceLocation rl = new ResourceLocation(item.getRegistryName()+ (meta>0?("_"+meta):""));
		ModelResourceLocation mrl = new ModelResourceLocation(rl, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, mrl);
	}

	private void registerBlockColors() {
		BlockColors bc = Minecraft.getMinecraft().getBlockColors();
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
	}
}
