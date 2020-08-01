package thKaguyaMod.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelSuwakoHat<T extends LivingEntity> extends BipedModel<T> {
	RendererModel hatBase;
    RendererModel eyeright;
    RendererModel eyeleft;
    RendererModel brim;
	
    public ModelSuwakoHat(float scale, int width, int height) {
        textureWidth = width;
        textureHeight = height;
        
        
        hatBase = new RendererModel(this, 0, 0);
        hatBase.addBox(-4F, -9F, -4F, 8, 4, 8, scale);
        hatBase.setRotationPoint(0F, 0F, 0F);
        hatBase.setTextureSize(64, 32);
        hatBase.mirror = true;
        bipedHead.addChild(hatBase);
        setRotation(hatBase, 0F, 0F, 0F);
        eyeright = new RendererModel(this, 0, 0);
        eyeright.addBox(-1F, -2F, -1F, 2, 2, 2, scale);
        eyeright.setRotationPoint(-4F, -9F, -4F);
        eyeright.setTextureSize(64, 32);
        eyeright.mirror = true;
        hatBase.addChild(eyeright);
        setRotation(eyeright, 0F, 0F, 0F);
        eyeleft = new RendererModel(this, 24, 0);
        eyeleft.addBox(-1F, -2F, -1F, 2, 2, 2, scale);
        eyeleft.setRotationPoint(4F, -9F, -4F);
        eyeleft.setTextureSize(64, 32);
        eyeleft.mirror = true;
        hatBase.addChild(eyeleft);
        setRotation(eyeleft, 0F, 0F, 0F);
        brim = new RendererModel(this, 0, 16);
        brim.addBox(-6F, -1F, -6F, 12, 1, 12, scale);
        brim.setRotationPoint(0F, -5F, 0F);
        brim.setTextureSize(64, 32);
        brim.mirror = true;
        hatBase.addChild(brim);
        setRotation(brim, 0F, 0F, 0F);
    }
    
    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	bipedHead.render(scale);
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
    }
}
