package zabi.minecraft.covens.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Broom - zabi94
 * Created using Tabula 6.0.0
 */
public class ModelBroom extends ModelBase {
    public ModelRenderer handle;
    public ModelRenderer twig1;
    public ModelRenderer twig2;
    public ModelRenderer twig3;
    public ModelRenderer twig4;
    public ModelRenderer twig5;
    public ModelRenderer twig6;
    public ModelRenderer twig7;
    public ModelRenderer twig8;

    public ModelBroom() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.handle = new ModelRenderer(this, 0, 0);
        this.handle.setRotationPoint(-10.0F, 16.0F, 0.0F);
        this.handle.addBox(0.0F, 0.0F, 0.0F, 14, 1, 1, 0.0F);
        this.twig3 = new ModelRenderer(this, 0, 5);
        this.twig3.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig3.addBox(0.0F, 0.0F, 0.0F, 7, 1, 1, -0.3F);
        this.setRotateAngle(twig3, 0.0F, -0.3141592653589793F, -0.17453292519943295F);
        this.twig5 = new ModelRenderer(this, 0, 2);
        this.twig5.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig5.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, -0.3F);
        this.setRotateAngle(twig5, 0.0F, 0.15707963267948966F, 0.19198621771937624F);
        this.twig1 = new ModelRenderer(this, 0, 5);
        this.twig1.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig1.addBox(0.0F, 0.0F, 0.0F, 7, 1, 1, -0.3F);
        this.setRotateAngle(twig1, 0.0F, 0.3490658503988659F, 0.0F);
        this.twig4 = new ModelRenderer(this, 0, 5);
        this.twig4.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig4.addBox(0.0F, 0.0F, 0.0F, 7, 1, 1, -0.3F);
        this.setRotateAngle(twig4, 0.0F, 0.05235987755982988F, -0.2792526803190927F);
        this.twig6 = new ModelRenderer(this, 0, 8);
        this.twig6.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig6.addBox(0.0F, 0.0F, 0.0F, 5, 1, 1, -0.3F);
        this.setRotateAngle(twig6, 0.0F, -0.10471975511965977F, 0.05235987755982988F);
        this.twig7 = new ModelRenderer(this, 0, 2);
        this.twig7.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig7.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, -0.3F);
        this.setRotateAngle(twig7, 0.0F, -0.3141592653589793F, 0.15707963267948966F);
        this.twig8 = new ModelRenderer(this, 0, 8);
        this.twig8.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig8.addBox(0.0F, 0.0F, 0.0F, 5, 1, 1, -0.3F);
        this.setRotateAngle(twig8, 0.0F, 0.10471975511965977F, -0.10471975511965977F);
        this.twig2 = new ModelRenderer(this, 0, 5);
        this.twig2.setRotationPoint(3.2F, 16.0F, 0.0F);
        this.twig2.addBox(0.0F, 0.0F, 0.0F, 7, 1, 1, -0.3F);
        this.setRotateAngle(twig2, 0.0F, -0.17453292519943295F, 0.3490658503988659F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.handle.render(f5);
        this.twig3.render(f5);
        this.twig5.render(f5);
        this.twig1.render(f5);
        this.twig4.render(f5);
        this.twig6.render(f5);
        this.twig7.render(f5);
        this.twig8.render(f5);
        this.twig2.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
