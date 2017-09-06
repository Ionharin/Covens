package zabi.minecraft.covens.common.registries.fortune;

import javax.annotation.Nonnull;

import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public abstract class Fortune extends IForgeRegistryEntry.Impl<Fortune> {
	
	public static final IForgeRegistry<Fortune> REGISTRY = new RegistryBuilder<Fortune>().setName(new ResourceLocation(Reference.MID, "fortunes")).setType(Fortune.class).setIDRange(0, 200).create();
	
	private int weight;
	
	public Fortune(int weight, @Nonnull String name, @Nonnull String modid) {
		this.setRegistryName(modid, name);
		this.weight = weight;
	}
	
	public int getDrawingWeight() {
		return weight;
	}
	
	public abstract boolean canBeUsedFor(@Nonnull EntityPlayer player);
	public abstract boolean canShouldBeAppliedNow(@Nonnull EntityPlayer player);
	public abstract boolean apply(@Nonnull EntityPlayer player);
	
	public String getUnlocalizedName() {
		return "fortunes."+this.getRegistryName().getResourceDomain()+"."+this.getRegistryName().getResourcePath()+".name";
	}
	
	public String getLocalizedName(@Nonnull EntityPlayer player) { //Override this to format the fortune differently
		return I18n.format(getUnlocalizedName());
	}
	
}
