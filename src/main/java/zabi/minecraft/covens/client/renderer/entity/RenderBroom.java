package zabi.minecraft.covens.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
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

		GlStateManager.pushMatrix();
		bindEntityTexture(entity);
        
		float smoothYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
		
		GlStateManager.translate(x, y-0.5d, z);
		GlStateManager.scale(0.0625d, 0.0625d, 0.0625d);
		GlStateManager.rotate(90-smoothYaw, 0, 1, 0);
		model.render(entity, 0f, 0f, 0f, 0f, 0f, 1f);
		GlStateManager.popMatrix();
	}

}
