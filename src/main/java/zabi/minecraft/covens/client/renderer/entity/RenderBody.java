package zabi.minecraft.covens.client.renderer.entity;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.covens.common.entity.EntityPlayerBody;

public class RenderBody extends Render<EntityPlayerBody> {

	public RenderBody(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerBody entity) {
		return null;
	}
	
	@Override
	public void doRender(EntityPlayerBody entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (!entity.getPlayerUUID().equals("")) {
			EntityPlayer p = entity.world.getPlayerEntityByUUID(UUID.fromString(entity.getPlayerUUID()));
			if (p!=null) {
				//Save status
				float swing = p.limbSwingAmount;
				float yaw = p.rotationYaw;
				float swing2 = p.prevLimbSwingAmount;
				float yawOff = p.renderYawOffset;
				float prevYawOff = p.prevRenderYawOffset;
				float swingArm = p.swingProgress;
				float swingArmPrev = p.prevSwingProgress;
				float head = p.rotationYawHead;
				float headPrev = p.prevRotationYawHead;
				float pitch = p.rotationPitch;
				float pitchPrev = p.prevRotationPitch;
				ItemStack held_main = p.getHeldItem(EnumHand.MAIN_HAND);
				ItemStack held_off = p.getHeldItem(EnumHand.OFF_HAND);
				ArrayList<ItemStack> armor = new ArrayList<ItemStack>(p.inventory.armorInventory);
				
				//Reset player
				p.limbSwingAmount = 0;
				p.prevLimbSwingAmount = 0;
				p.rotationYaw = entity.rotationYaw;
				p.renderYawOffset = entity.rotationYaw;
				p.prevRenderYawOffset = entity.rotationYaw;
				p.swingProgress = 0;
				p.prevSwingProgress = 0;
				p.rotationYawHead = entity.rotationYaw;
				p.prevRotationYawHead = entity.rotationYaw;
				p.rotationPitch = entity.rotationPitch;
				p.prevRotationPitch = entity.rotationPitch;
				p.inventory.mainInventory.set(p.inventory.currentItem, ItemStack.EMPTY);
				p.inventory.offHandInventory.set(0, ItemStack.EMPTY);
				for (int i=0;i<p.inventory.armorInventory.size();i++) p.inventory.armorInventory.set(i, ItemStack.EMPTY);
				
				//Render
				Minecraft.getMinecraft().getRenderManager().doRenderEntity(p, x, y, z, p.rotationYaw, 0, false);
				
				//Restore
				p.limbSwingAmount = swing;
				p.rotationYaw = yaw;
				p.prevLimbSwingAmount = swing2;
				p.renderYawOffset = yawOff;
				p.prevRenderYawOffset = prevYawOff;
				p.swingProgress = swingArm;
				p.prevSwingProgress = swingArmPrev;
				p.rotationYawHead = head;
				p.prevRotationYawHead = headPrev;
				p.rotationPitch = pitch;
				p.prevRotationPitch = pitchPrev;
				p.inventory.mainInventory.set(p.inventory.currentItem, held_main);
				p.inventory.offHandInventory.set(0, held_off);
				for (int i=0;i<p.inventory.armorInventory.size();i++) p.inventory.armorInventory.set(i, armor.get(i));
			}
		}
		
	}

}
