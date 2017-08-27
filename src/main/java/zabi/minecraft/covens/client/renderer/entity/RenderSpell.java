package zabi.minecraft.covens.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.entity.EntitySpellCarrier;

public class RenderSpell extends Render<EntitySpellCarrier> {

	public RenderSpell(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpellCarrier entity) {
		return null;
	}
	
	@Override
	public void doRender(EntitySpellCarrier entity, double x, double y, double z, float entityYaw, float partialTicks) {
		entity.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, x, y, z, 0, -0.1, 0);
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
