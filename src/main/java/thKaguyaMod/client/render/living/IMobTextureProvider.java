package thKaguyaMod.client.render.living;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface IMobTextureProvider {
	String value();
}
