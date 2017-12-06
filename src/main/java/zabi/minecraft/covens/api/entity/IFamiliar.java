package zabi.minecraft.covens.api.entity;

import java.util.UUID;

import javax.annotation.Nullable;

public interface IFamiliar {
	@Nullable public UUID getOwnerUUID();
	public boolean isBound();
}
