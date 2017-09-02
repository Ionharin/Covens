package zabi.minecraft.covens.client.gui;

import org.lwjgl.opengl.GL11;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import zabi.minecraft.covens.api.utils.IInfusionPowerUser;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.registries.Enums.EnumInfusion;

public class RenderInfusionBar {

	private static final ResourceLocation bar_frame = new ResourceLocation(Reference.MID, "textures/gui/infusion_frame.png");
	private static final ResourceLocation bar_content = new ResourceLocation(Reference.MID, "textures/gui/infusion_content.png");

	@SubscribeEvent
	public void render(TickEvent.RenderTickEvent evt) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player!=null) {
			if (player.getHeldItemMainhand().getItem() instanceof IInfusionPowerUser || player.getHeldItemOffhand().getItem() instanceof IInfusionPowerUser) {
				PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
				EnumInfusion inf = data.getInfusion(); 
				if (inf!=null) {
					GL11.glPushMatrix();
					GlStateManager.enableAlpha();
					GlStateManager.enableAlpha();
					float progress = (float)data.getInfusionPower()/(float)(data.getMaxPower()+1);
					int color = inf.color();
					GlStateManager.color((color>>16 & 0xff)/255f, (color>>8 & 0xff)/255f, (color & 0xff)/255f);
					Minecraft.getMinecraft().renderEngine.bindTexture(bar_content);
					GuiIngame.drawModalRectWithCustomSizedTexture(10, 101+((int)(64 * (1-progress))), 0, (1-progress)*64, 16, (int) (64*progress), 16, 64);
					GlStateManager.color(1f, 1f, 1f);
					Minecraft.getMinecraft().renderEngine.bindTexture(bar_frame);
					GuiIngame.drawModalRectWithCustomSizedTexture(10, 100, 0, 0, 16, 64, 16, 64);
					GL11.glPopMatrix();
				}
			}
		}
	}
}
