package zabi.minecraft.covens.client.renderer.entity;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;

public class RenderSpell extends Render<EntitySpellCarrier> {
	
	private static Random rnd = new Random();

	public RenderSpell(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpellCarrier entity) {
		return null;
	}
	
	@Override
	public void doRender(EntitySpellCarrier entity, double x, double y, double z, float entityYaw, float partialTicks) {
		Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.END_ROD, entity.posX, entity.posY, entity.posZ, 0.02*rnd.nextGaussian(), 0.02*rnd.nextGaussian(), 0.02*rnd.nextGaussian());
	}
 
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
