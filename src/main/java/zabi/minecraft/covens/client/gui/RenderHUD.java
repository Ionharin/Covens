package zabi.minecraft.covens.client.gui;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.block.BlockCircleGlyph;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.block.ModBlocks;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

@SideOnly(Side.CLIENT)
public class RenderHUD {
	
	public static final RenderHUD INSTANCE = new RenderHUD();
	public static final int MAX_TIME_TICKS = 1000;
	
	private RenderHUD() {}
	
	private HUDSubject subject = null;
	
	public void setup(BlockPos pos, World world) {
		if (pos!=null && world!=null) {
			boolean flag = subject==null;
			subject = new HUDSubject(pos, world);
			if (flag) MinecraftForge.EVENT_BUS.register(this);
		} else {
			subject = null;
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
	
	@SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent evt) {
		
		if (Minecraft.getMinecraft().player==null || Minecraft.getMinecraft().world==null) subject = null;
		
		if (subject == null) {
			MinecraftForge.EVENT_BUS.unregister(this);
			return;
		}
		if (subject.shownFor>=MAX_TIME_TICKS) {
			subject = null;
			MinecraftForge.EVENT_BUS.unregister(this);
			return;
		}
		subject.shownFor++;
		Random r = new Random();
		switch (subject.type) {
		case GLYPH_INFO:
			if (r.nextDouble()<0.1) renderSelectedGlyph(evt.renderTickTime, r);
			renderWhiteList(evt.renderTickTime);
			renderBoundAltar(evt.renderTickTime, r);
		}
	}
	
	private void renderSelectedGlyph(float renderTickTime, Random r) {
		if (subject == null) return;
		subject.world.spawnParticle(EnumParticleTypes.FLAME, subject.pos.getX()+r.nextDouble(), subject.pos.getY()+r.nextDouble()*0.1, subject.pos.getZ()+r.nextDouble(), 0, 0.05, 0);
	}

	private void renderBoundAltar(float renderTickTime, Random r) {
		if (subject == null) return;
		TileEntityGlyph glyph = (TileEntityGlyph) subject.world.getTileEntity(subject.pos);
		if (glyph==null) {
			subject = null;
			MinecraftForge.EVENT_BUS.unregister(this);
			return;
		}
		BlockPos pos=glyph.getBoundAltar(!subject.fetchedAltar);
		subject.fetchedAltar=true;
		if (pos!=null) {
			subject.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, pos.getX()-2+r.nextDouble()*5, pos.getY()+1.5*r.nextDouble(), pos.getZ()-2+r.nextDouble()*5, 0, 0.1, 0);
		}
	}
	
	private void renderWhiteList(float renderTickTime) {
		if (subject == null) return;
		TileEntityGlyph glyph = (TileEntityGlyph) subject.world.getTileEntity(subject.pos);
		if (glyph==null) {
			subject = null;
			MinecraftForge.EVENT_BUS.unregister(this);
			return;
		}
		List<String> names = glyph.getWhitelistEntries().parallelStream().map(t -> t.getSecond()).collect(Collectors.toList());
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		for (int i=0;i<names.size();i++) {
			fr.drawString(names.get(i), 3, 3+fr.FONT_HEIGHT*i, 0xffffff);
		}
	}

	protected static class HUDSubject {
		
		public BlockPos pos;
		public World world;
		public int shownFor = 0;
		public boolean fetchedAltar = false;
		public EnumTypeHUD type;
		
		public HUDSubject(BlockPos pos, World world) {
			this.pos = pos;
			this.world = world;
			if (world.getBlockState(pos).getBlock().equals(ModBlocks.glyphs) && world.getBlockState(pos).getValue(BlockCircleGlyph.TYPE).equals(GlyphType.GOLDEN)) {
				type = EnumTypeHUD.GLYPH_INFO;
			}
		}
	}
	
	protected enum EnumTypeHUD {
		GLYPH_INFO
	}
	
}
