package zabi.minecraft.covens.client.gui;

import org.lwjgl.opengl.GL11;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zabi.minecraft.covens.api.utils.IInfusionPowerUser;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;

public class RenderInfusionBar {

	private static final ResourceLocation bar_frame_ovw = new ResourceLocation(Reference.MID, "textures/gui/infusion_frame_overworld.png");
	private static final ResourceLocation bar_content_ovw = new ResourceLocation(Reference.MID, "textures/gui/infusion_content_overworld.png");
	private static final ResourceLocation bar_frame_ntr = new ResourceLocation(Reference.MID, "textures/gui/infusion_frame_nether.png");
	private static final ResourceLocation bar_content_ntr = new ResourceLocation(Reference.MID, "textures/gui/infusion_content_nether.png");
	private static final ResourceLocation bar_frame_end = new ResourceLocation(Reference.MID, "textures/gui/infusion_frame_end.png");
	private static final ResourceLocation bar_content_end = new ResourceLocation(Reference.MID, "textures/gui/infusion_content_end.png");
	
	private static final int textureHeight = 64;

	@SubscribeEvent
	public void render(TickEvent.RenderTickEvent evt) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player!=null) {
			if (player.getHeldItemMainhand().getItem() instanceof IInfusionPowerUser || player.getHeldItemOffhand().getItem() instanceof IInfusionPowerUser) {
				PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
				EnumInfusion inf = data.getInfusion(); 
				if (inf!=null) {
					ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
					GL11.glPushMatrix();
					GlStateManager.enableAlpha();
					GlStateManager.enableAlpha();
					float remaining = (float)data.getInfusionPower()/(float)(data.getMaxPower()+1);
					GlStateManager.color(1, 1, 1);
					Minecraft.getMinecraft().renderEngine.bindTexture(getTextureContent(inf));
					int startPixels = (sr.getScaledHeight()-textureHeight)/2;
					int actualPixels = (int) Math.ceil(textureHeight*remaining);
					int paddingPixels = textureHeight-actualPixels;
					
					GuiIngame.drawModalRectWithCustomSizedTexture(10, startPixels+paddingPixels, 0, paddingPixels, 16, actualPixels, 16, textureHeight);
					
					GlStateManager.color(1f, 1f, 1f);
					Minecraft.getMinecraft().renderEngine.bindTexture(getTextureFrame(inf));
					GuiIngame.drawModalRectWithCustomSizedTexture(10, startPixels, 0, 0, 16, textureHeight, 16, textureHeight);
					GL11.glPopMatrix();
				}
			}
		}
	}

	private ResourceLocation getTextureContent(EnumInfusion inf) {
		switch (inf) {
		case END:
			return bar_content_end;
		case NETHER:
			return bar_content_ntr;
		case OVERWORLD:
			return bar_content_ovw;
		default:
			return null;
		}
	}
	
	private ResourceLocation getTextureFrame(EnumInfusion inf) {
		switch (inf) {
		case END:
			return bar_frame_end;
		case NETHER:
			return bar_frame_ntr;
		case OVERWORLD:
			return bar_frame_ovw;
		default:
			return null;
		}
	}
}
