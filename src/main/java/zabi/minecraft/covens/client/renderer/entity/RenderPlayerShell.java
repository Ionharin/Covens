package zabi.minecraft.covens.client.renderer.entity;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.entity.EntityPlayerShell;

public class RenderPlayerShell extends Render<EntityPlayerShell> {
	
	public RenderPlayerShell(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerShell entity) {
		return null;
	}
	
	@Override
	public void doRender(EntityPlayerShell entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EntityPlayer p = entity.getPlayer();
		if (p!=null && p instanceof AbstractClientPlayer) {
			Log.d("drawing");
			drawShellAt(entity, (AbstractClientPlayer) p, partialTicks);
		} else {
			Log.d("not found");
		}
		entity.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, x, y, z, 0, 0.2, 0);
	}
 
	public static void drawShellAt(EntityPlayerShell shell, AbstractClientPlayer player, float pticks) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        float f = player.renderYawOffset;
        float f1 = player.rotationYaw;
        float f2 = player.rotationPitch;
        float f3 = player.prevRotationYawHead;
        float f4 = player.rotationYawHead;
        double px = player.posX;
        double py = player.posY;
        double pz = player.posZ;
        double ppx = player.prevPosX;
        double ppy = player.prevPosY;
        double ppz = player.prevPosZ;
        RenderHelper.enableStandardItemLighting();
        player.renderYawOffset = shell.renderYawOffset;
        player.rotationYaw = shell.rotationYaw;
        player.rotationPitch = shell.rotationPitch;
        player.rotationYawHead = shell.rotationYawHead;
        player.prevRotationYawHead = shell.prevRotationYawHead;
        player.posX = shell.posX;
        player.posY = shell.posY;
        player.posZ = shell.posZ;
        player.prevPosX = shell.prevPosX;
        player.prevPosY = shell.prevPosY;
        player.prevPosZ = shell.prevPosZ;
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(player, player.posX, player.posY, player.posZ, player.rotationYaw, pticks, false);
        rendermanager.setRenderShadow(true);
        player.renderYawOffset = f;
        player.rotationYaw = f1;
        player.rotationPitch = f2;
        player.prevRotationYawHead = f3;
        player.rotationYawHead = f4;
        player.posX = px;
        player.posY = py;
        player.posZ = pz;
        player.prevPosX = ppx;
        player.prevPosY = ppy;
        player.prevPosZ = ppz;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
	

	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
