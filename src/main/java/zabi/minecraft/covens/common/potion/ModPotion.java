package zabi.minecraft.covens.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.lib.Reference;

public abstract class ModPotion extends Potion {
	
	public static final ResourceLocation EXTRA_EFFECTS = new ResourceLocation(Reference.MID, "textures/icons/potions.png");

	protected ModPotion(boolean isBadEffectIn, int liquidColorIn, String name) {
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName("effect."+name);
		this.setRegistryName(Reference.MID, name);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(EXTRA_EFFECTS);
		return super.getStatusIconIndex();
	}
	

}
