package thKaguyaMod.client.render.living;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TessellatorWrapper {
	public static TessellatorWrapper instance = new TessellatorWrapper();
	private static final Tessellator tessellator = Tessellator.getInstance();

	public void startDrawingQuads() {
		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
	}

	public void addVertexWithUV(double x, double y, double z, double u, double v) {
		tessellator.getBuffer().pos(x, y, z).tex(u, v).endVertex();
	}

	public void draw() {
		tessellator.draw();
	}
	
	public void setColorRGBA_F(double r, double g, double b, double alpha) {
		// TODO: rikka does not know what to place here
	}
}
