package zabi.minecraft.covens.client.renderer.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.entity.EntityCrystalBallObserver;

public class RenderObserver extends Render<EntityCrystalBallObserver> {
	
	public RenderObserver(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCrystalBallObserver entity) {
		return null;
	}
	
	@Override
	public void doRender(EntityCrystalBallObserver entity, double x, double y, double z, float entityYaw, float partialTicks) {
	}
 
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
	}

}
