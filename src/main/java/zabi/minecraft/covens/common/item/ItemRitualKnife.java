package zabi.minecraft.covens.common.item;

import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import zabi.minecraft.covens.common.lib.Reference;

public class ItemRitualKnife extends ItemSword {
	
	private static final ToolMaterial knife_material = EnumHelper.addToolMaterial("witchery_blades", 0, 131, 1, 0.5f, 2);

	public ItemRitualKnife() {
		super(knife_material);
		setRegistryName(Reference.MID, "ritual_knife");
		this.setUnlocalizedName("ritual_knife");
		this.setCreativeTab(ModCreativeTabs.products);
	}
}
