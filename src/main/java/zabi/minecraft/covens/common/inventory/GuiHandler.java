package zabi.minecraft.covens.common.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zabi.minecraft.covens.client.gui.GuiBarrel;
import zabi.minecraft.covens.client.gui.GuiBook;
import zabi.minecraft.covens.client.gui.GuiChimney;
import zabi.minecraft.covens.client.gui.GuiSilverVat;
import zabi.minecraft.covens.client.gui.GuiThreadSpinner;
import zabi.minecraft.covens.common.lib.Log;

import zabi.minecraft.covens.common.tileentity.TileEntityBarrel;
import zabi.minecraft.covens.common.tileentity.TileEntityChimney;
import zabi.minecraft.covens.common.tileentity.TileEntitySilverVat;
import zabi.minecraft.covens.common.tileentity.TileEntityThreadSpinner;

public class GuiHandler implements IGuiHandler {

	public enum IDs {
		CHIMNEY, THREAD_SPINNER, BOOK_TEST, BARREL, SILVER_VAT
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos=new BlockPos(x, y, z);
		switch (IDs.values()[ID]) {
		case CHIMNEY:
			return new ContainerChimney(player.inventory, (TileEntityChimney) world.getTileEntity(pos));
		case THREAD_SPINNER:
			return new ContainerThreadSpinner(player.inventory, (TileEntityThreadSpinner) world.getTileEntity(pos));
		case BOOK_TEST:
			return null;
		case BARREL:
			return new ContainerBarrel(player.inventory, (TileEntityBarrel) world.getTileEntity(pos));
		case SILVER_VAT:
			return new ContainerSilverVat(player.inventory, (TileEntitySilverVat) world.getTileEntity(pos));
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
		case THREAD_SPINNER:
			return new GuiThreadSpinner((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityThreadSpinner) world.getTileEntity(pos));
		case BOOK_TEST:
			return new GuiBook("testBook");
		case BARREL:
			return new GuiBarrel((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityBarrel) world.getTileEntity(pos));
		case SILVER_VAT:
			return new GuiSilverVat((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntitySilverVat) world.getTileEntity(pos));
		default:
			Log.w("invalid GUI requested");
			return null;

		}
	}

}
