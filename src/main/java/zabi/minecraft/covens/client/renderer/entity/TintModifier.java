package zabi.minecraft.covens.client.renderer.entity;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.capability.CovensData;
import zabi.minecraft.covens.common.potion.ModPotions;

@SideOnly(Side.CLIENT)
public class TintModifier {
	
	@SubscribeEvent
	public void onPreRender(RenderLivingEvent.Pre<EntityLivingBase> evt) {
		if (evt.getEntity().isPotionActive(ModPotions.tinting)) {
			CovensData data = evt.getEntity().getCapability(CovensData.CAPABILITY, null);
			if (data!=null && data.getTint()>=0) {
				Color color = new Color(data.getTint());
				GL11.glColor3d((double)color.getRed()/255d, (double)color.getGreen()/255d, (double)color.getBlue()/255d);
			}
		}
	}
	
}
