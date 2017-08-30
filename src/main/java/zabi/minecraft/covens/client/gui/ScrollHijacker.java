package zabi.minecraft.covens.client.gui;

import java.lang.reflect.Field;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.item.ItemGrimoire;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.network.messages.ScrollSpell;

@SideOnly(Side.CLIENT)
public class ScrollHijacker {
	
	private static final Field highlight = ReflectionHelper.findField(GuiIngame.class, "remainingHighlightTicks", "field_92017_k");
	
	@SubscribeEvent
	public void onScroll(MouseEvent evt) {
		EntityPlayer p = Minecraft.getMinecraft().player;
		if (evt.getDwheel()==0) return;
		int dir = evt.getDwheel()>0?1:-1; 
		if (dir!=0 && p!=null && p.isSneaking() && p.inventory.getCurrentItem().getItem()==ModItems.grimoire && Minecraft.getMinecraft().ingameGUI!=null) {
			evt.setCanceled(true);
			Covens.network.sendToServer(new ScrollSpell(dir));
			ItemGrimoire.scrollSpell(p.inventory.getCurrentItem(), dir);
			try {
				highlight.set(Minecraft.getMinecraft().ingameGUI, 40);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				Log.e("[COVENS] Couldn't access field remainingHighlightTicks/field_92017_k via reflection. This is a bug, report it please");
			}
		}
	}
	
}
