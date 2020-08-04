package thKaguyaMod.client.render.living;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.opengl.GL11;

import thKaguyaMod.THKaguyaCore;
import thKaguyaMod.THShotLib;
import thKaguyaMod.entity.living.IDanmakuMob;

@OnlyIn(Dist.CLIENT)
public class RenderTHBoss<T extends LivingEntity, M extends EntityModel<T>> extends LivingRenderer<T,M> {
	//ボスの描画の共通処理
	
	public static ResourceLocation statusTexture = new ResourceLocation("thkaguyamod", "textures/mob/status.png");
	private final ResourceLocation texture;

	public RenderTHBoss(EntityRendererManager manager, M entityModel) {
		this(manager, entityModel, 0.25F);
	}
	
	public RenderTHBoss(EntityRendererManager manager, M entityModel, float shadowSize) {
		this(manager, entityModel, shadowSize, getTextureForModel(entityModel));
	}
	
	public RenderTHBoss(EntityRendererManager manager, M entityModel, float shadowSize, ResourceLocation texture) {
		super(manager, entityModel, shadowSize);
		this.texture = texture;
	}

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float pitch) {
    	//GL11.glDisable(GL11.GL_LIGHTING);
    	//GL11.glEnable(GL11.GL_BLEND);
    	this.renderOutlines = false;
    	super.doRender(entity, x, y, z, yaw, pitch);
    	this.renderTHBossStatus(entity, x, y, z, yaw, pitch);
    }

	public void renderTHBossStatus(T entity, double x, double y, double z, float yaw, float pitch) {
		IDanmakuMob danmakuMob = (IDanmakuMob) entity;
		if (danmakuMob.getDanmakuPattern() == IDanmakuMob.NOT_ATTACK) {
			return;
		}

		//体力ゲージを表示する
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        
    	
        TessellatorWrapper tessellator = TessellatorWrapper.instance;
        //GL11.glDepthMask(false);
        float viewY = renderManager.playerViewY % 360F;
        if(viewY > 180F)
        {
        	viewY -= 360F;
        }
        else if(viewY <= -180)
        {
        	viewY += 360F;
        }
        Vec3d look = THShotLib.getVecFromAngle(viewY, renderManager.playerViewX);
        Vec3d toEntity = new Vec3d(x, y, z);
        float span = THShotLib.getVectorAndVectorAngle(look, toEntity);
        float alpha = 1F - (Math.abs(span) - 20F) / 30F;
        
		double distance = renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ);
		float size = 1.0F + (float)distance / 64F;
		if(size > 5.0F )
		{
			size = 5.0F;
		}
        
        if(Math.abs(span) <= 20F)
        {
        	GL11.glTranslatef((float)x, (float)y + entity.getHeight() + 1.5F, (float)z);
        	GL11.glScalef(1.0F * size, 1.0F * size, 1.0F * size);
        	

        
        	GL11.glRotatef(  -renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        	GL11.glRotatef(   renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        
    		this.bindTexture(statusTexture);
    		int cardNo = danmakuMob.getUsingSpellCardNo();

    		float hp = entity.getHealth() / entity.getMaxHealth();
    		float hp2 = entity.getHealth() / entity.getMaxHealth() * 2.0F - 1.0F;
    		
			tessellator.startDrawingQuads();
			//tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
			//tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    	tessellator.addVertexWithUV(  -1.02F, 0.07F, 0.001D, 0.0F,  3F / 16F);
	    	tessellator.addVertexWithUV(  1.02F, 0.07F, 0.001D, 1.0F,  3F / 16F);
	    	tessellator.addVertexWithUV(  1.02F, -0.02F, 0.001D, 1.0F,  4F / 16F);
	    	tessellator.addVertexWithUV(  -1.02F,-0.02F, 0.001D, 0.0F,  4F / 16F);
	    	tessellator.draw();
	        
	    	//スペルカードを使用中なら
	    	if(cardNo >= 0)
	    	{
				tessellator.startDrawingQuads();
				//tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
				//tessellator.setNormal(0.0F, 1.0F, 0.0F);
		    	tessellator.addVertexWithUV(  -hp2, 0.05F, 0.0D, hp,  8F / 16F);
		    	tessellator.addVertexWithUV(  1.0F, 0.05F, 0.0D, 0.0F,  8F / 16F);
		    	tessellator.addVertexWithUV(  1.0F, 0.00F, 0.0D, 0.0F,  11F / 16F);
		    	tessellator.addVertexWithUV(  -hp2, 0.00F, 0.0D, hp,  11F / 16F);
		    	
		    	tessellator.draw();
	    	}
	    	//スペルカードを使用していないなら
	    	else
	    	{
				tessellator.startDrawingQuads();
				//tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
				//tessellator.setNormal(0.0F, 1.0F, 0.0F);
		    	tessellator.addVertexWithUV(  -hp2, 0.05F, 0.0D, hp,  0F / 16F);
		    	tessellator.addVertexWithUV(  1.0F, 0.05F, 0.0D, 0.0F,  0F / 16F);
		    	tessellator.addVertexWithUV(  1.0F, 0.00F, 0.0D, 0.0F,  3F / 16F);
		    	tessellator.addVertexWithUV(  -hp2, 0.00F, 0.0D, hp,  3F / 16F);
		    	
		    	tessellator.draw();
	    	}
	    	
	    	GL11.glRotatef( 180F, 0.0F, 0.0F, 1.0F);
	    	GL11.glScalef(0.02F, 0.02F, 0.02F);
	    	tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
	    	FontRenderer font = this.getFontRendererFromRenderManager();
	    	font.drawStringWithShadow(entity.getDisplayName().getString(), -50, 0, 0x00FF88);
	    	
	    	//スペルカードを使用中なら、スペルカード名を表示する
			if (cardNo >= 0) {
				tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
				font.drawStringWithShadow(I18n.format("item.thSpellCard." + cardNo + ".name"), -50, -12, 0xFFFFFF);
			}
    	}
    	
    	GL11.glDisable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_LIGHTING);
    	GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return texture;
	}
	
	public static ResourceLocation getTextureForModel(EntityModel<?> model) {
		if (model.getClass().isAnnotationPresent(IMobTextureProvider.class)) {
			String path = model.getClass().getAnnotation(IMobTextureProvider.class).value();
			return new ResourceLocation(THKaguyaCore.MODID, path);
		} else {
			return statusTexture;
		}
	}
}