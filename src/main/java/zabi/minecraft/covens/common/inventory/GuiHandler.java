package zabi.minecraft.covens.common.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zabi.minecraft.covens.client.gui.GuiChimney;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.tileentity.TileEntityChimney;

public class GuiHandler implements IGuiHandler {

	public enum IDs {
		CHIMNEY
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos=new BlockPos(x, y, z);
		switch (IDs.values()[ID]) {
		case CHIMNEY:
			return new ContainerChimney(player.inventory, (TileEntityChimney) world.getTileEntity(pos));
		default:
			Log.w("invalid GUI requested: " + ID);
			return null;

		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos=new BlockPos(x, y, z);
		switch (IDs.values()[ID]) {
		case CHIMNEY:
			return new GuiChimney((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityChimney) world.getTileEntity(pos));
		default:
			Log.w("invalid GUI requested");
			return null;

		}
	}

}
