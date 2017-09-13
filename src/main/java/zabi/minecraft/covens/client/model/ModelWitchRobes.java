package zabi.minecraft.covens.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitchRobes extends ModelBiped {
	private ModelRenderer hatBase;
	private ModelRenderer hatBody;
	private ModelRenderer hatPoint;

	public ModelWitchRobes(float scale) {
		super(scale, 0, 128, 64);

		hatBase = new ModelRenderer(this, 0, 0);
		hatBase.addBox(-8.0F, -6.0F, -8.0F, 16, 1, 16);
		hatBase.setRotationPoint(0F, 0F, 0F);
		hatBase.setTextureSize(64, 64);
		hatBase.mirror = true;
		setRotation(hatBase, 0F, 0F, 0F);
		hatBody = new ModelRenderer(this, 48, 0);
		hatBody.addBox(-5.0F, -12.0F, -5.0F, 10, 6, 10);
		hatBody.setRotationPoint(0F, 0F, 0F);
		hatBody.setTextureSize(64, 64);
		hatBody.mirror = true;
		setRotation(hatBody, 0F, 0F, 0F);
		hatPoint = new ModelRenderer(this, 88, 0);
		hatPoint.addBox(-3.0F, -18.0F, -3.0F, 6, 6, 6);
		hatPoint.setRotationPoint(0F, 0F, 0F);
		hatPoint.setTextureSize(64, 64);
		hatPoint.mirror = true;
		setRotation(hatPoint, 0F, 0F, 0F);

		bipedHead.addChild(hatBase);
		bipedHead.addChild(hatBody);
		bipedHead.addChild(hatPoint);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		hatBase.render(f5);
		hatBody.render(f5);
		hatPoint.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
