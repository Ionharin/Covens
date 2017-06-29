package zabi.minecraft.covens.client.renderer.entity;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.entity.BrewEntity;

@SideOnly(Side.CLIENT)
public class RenderBrewThrown extends RenderSnowball<BrewEntity> {

	public RenderBrewThrown(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
		super(renderManagerIn, itemIn, itemRendererIn);
	}

	@Override
	public ItemStack getStackToRender(BrewEntity entityIn) {
		return entityIn.getItemStack();
	}
	
}
