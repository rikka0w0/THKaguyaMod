package thKaguyaMod.client.render.living;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import thKaguyaMod.client.model.living.ModelCirno;
import thKaguyaMod.entity.living.EntityCirno;

public class RenderCirno extends RenderTHBoss<EntityCirno, ModelCirno> {
	//チルノを描画する
	
	ResourceLocation cirnoTexture = new ResourceLocation("thkaguyamod", "textures/mob/cirno.png");

	public RenderCirno(EntityRendererManager renderManager) {
		super(renderManager, new ModelCirno(), 0.25F);
	}
    
    @Override
	public void doRender(EntityCirno entity, double x, double y, double z, float yaw, float pitch) {
		super.doRender(entity, x, y, z, yaw, pitch);
		this.renderTHFairyCirno((EntityCirno) entity, x, y, z, yaw, pitch);
	}

	public void renderTHFairyCirno(EntityCirno thFairyCirno, double x, double y, double z, float yaw, float pitch) {
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCirno cirno) {
		return cirnoTexture;
	}
}
