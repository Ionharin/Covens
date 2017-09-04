package zabi.minecraft.covens.common.tileentity;

import net.minecraft.util.ITickable;

public abstract class TileEntityBaseTickable extends TileEntityBase implements ITickable {

	public TileEntityBaseTickable() {
		super();
	}

	protected abstract void tick();

	@Override
	public void update() {
//		if (!Config.disableTileEntities) tick();
		tick();
	}

}
