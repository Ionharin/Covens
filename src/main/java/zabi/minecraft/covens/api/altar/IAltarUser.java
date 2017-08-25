package zabi.minecraft.covens.api.altar;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.tileentity.TileEntityAltar;

public interface IAltarUser {
	@Nullable
	public TileEntityAltar getAltar(boolean allowRebind);
}
