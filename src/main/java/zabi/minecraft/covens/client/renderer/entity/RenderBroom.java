package zabi.minecraft.covens.client.renderer.entity;

import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.client.model.ModelBroom;
import zabi.minecraft.covens.common.entity.EntityFlyingBroom;
import zabi.minecraft.covens.common.lib.Reference;

public class RenderBroom extends Render<EntityFlyingBroom> {
	
	private static final ModelBroom model  = new ModelBroom();
	
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(Reference.MID, "textures/entity/broom_0.png"),
			new ResourceLocation(Reference.MID, "textures/entity/broom_1.png"),
			new ResourceLocation(Reference.MID, "textures/entity/broom_2.png"),
			new ResourceLocation(Reference.MID, "textures/entity/broom_3.png")			
	};

	public RenderBroom(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingBroom entity) {
		return TEXTURES[entity.getType()];
	}

	@Override
	public void doRender(EntityFlyingBroom entity, double x, double y, double z, float entityYaw, float partialTicks) {

		Random r = new Random();
		GlStateManager.pushMatrix();
		bindEntityTexture(entity);
        double smoothPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double smoothPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double smoothPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
		
		GlStateManager.translate(smoothPosX, smoothPosY, smoothPosZ);
		model.render(entity, 0, 0, 0, 0, 0, partialTicks);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks); //Do I really need this?
		
		//Until I find out why this isn't working you see particles
		entity.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, smoothPosX + r.nextDouble(), smoothPosY+ r.nextDouble(), smoothPosZ+ r.nextDouble(), 0,0,0);
	}

}
