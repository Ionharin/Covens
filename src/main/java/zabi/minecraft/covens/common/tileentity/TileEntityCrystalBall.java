package zabi.minecraft.covens.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.api.altar.IAltarUser;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.capability.PlayerData;
import zabi.minecraft.covens.common.entity.EntityCrystalBallObserver;
import zabi.minecraft.covens.common.item.ItemBrewBase;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.network.messages.SpectatorStart;
import zabi.minecraft.covens.common.network.messages.SpectatorStop;

public class TileEntityCrystalBall extends TileEntityBase implements IAltarUser {

	private TileEntityAltar te = null;
	private ItemStack locator = ItemStack.EMPTY;

	@Override
	protected void NBTLoad(NBTTagCompound tag) {
		locator.writeToNBT(tag);
	}

	@Override
	protected void NBTSave(NBTTagCompound tag) {
		locator = new ItemStack(tag);
	}

	public EnumCrystalBallResult tryAddItem(ItemStack is) {
		Item i = is.getItem();
		if (locator.isEmpty()) {
			if ((i==ModItems.soulstring && is.getMetadata()==1) || (i==ModItems.cardinal_stone && is.getMetadata()==2 && is.getOrCreateSubCompound("pos").hasKey("x"))) {
				locator = is.copy();
				return EnumCrystalBallResult.INSERT_ITEM;
			}
			return EnumCrystalBallResult.BLOCK;
		} else {
			if (is.isEmpty()) {
				return EnumCrystalBallResult.FORTUNE;
			} else if (i instanceof ItemBrewBase) {
				return applyBrew(is)?
						EnumCrystalBallResult.INSERT_ITEM : EnumCrystalBallResult.BLOCK;
			} else if (i==ModItems.candle && is.getMetadata()==0) {
				return EnumCrystalBallResult.SPECTATE;
			}
			return EnumCrystalBallResult.BLOCK;
		}
	}

	private boolean applyBrew(ItemStack is) {
		locator = ItemStack.EMPTY;
		return false;
	}

	@Nullable
	public Tuple<BlockPos, Integer> getDestinationPosition() {
		if (locator.getItem()!=ModItems.cardinal_stone) return null;
		if (locator.getOrCreateSubCompound("pos").hasKey("x")) {
			NBTTagCompound tag = locator.getOrCreateSubCompound("pos");
			BlockPos pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
			return new Tuple<BlockPos, Integer>(pos, tag.getInteger("dim"));
		}
		return null;
	}

	@Nullable
	public EntityLivingBase getDestinationEntity() {
		if (locator.getItem()!=ModItems.soulstring) return null;
		if (locator.getOrCreateSubCompound("boundData").hasKey("uid")) {
			String uuids = locator.getOrCreateSubCompound("boundData").getString("uid");
			List<Entity> list = new ArrayList<Entity>(getWorld().loadedEntityList);
			return (EntityLivingBase) list.parallelStream().filter(e-> (e instanceof EntityLivingBase)).filter(e -> e.getUniqueID().toString().equals(uuids)).findFirst().orElse(null);
		}
		return null;
	}

	@Override
	@Nullable
	public TileEntityAltar getAltar(boolean allowRebind) {
		if ((te==null || te.isInvalid()) && allowRebind) te = TileEntityAltar.getClosest(pos, world);
		if (te==null || te.isInvalid()) return null;
		return te;
	}

	public static enum EnumCrystalBallResult {
		BLOCK, INSERT_ITEM, SPECTATE, FORTUNE;
	}

	public boolean fortune(EntityPlayer reader) {
		if (consumePower(1000, false)) {
			EntityLivingBase de = getDestinationEntity();
			if (de instanceof EntityPlayer) readFortune((EntityPlayer)de, reader);
			else readFortune(reader, null);
			return true;
		}
		reader.sendStatusMessage(new TextComponentString("nopower"), true);
		return false;
	}

	private void readFortune(@Nonnull EntityPlayer endPlayer, @Nullable EntityPlayer externalReader) {
		endPlayer.sendStatusMessage(new TextComponentString("fortune_message"), true);
		if (externalReader!=null) {
			externalReader.sendStatusMessage(new TextComponentString("fortune_message_read_to"), true);
			locator = ItemStack.EMPTY;
		}
	}

	public boolean spectate(EntityPlayer p) {
		if (p instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) p;
			EntityLivingBase elb = getDestinationEntity();
			if (elb!=null && elb.world.provider.getDimension()==p.world.provider.getDimension() && consumePower(3000, false)) {
				player.getCapability(PlayerData.CAPABILITY, null).setSpectatingPoint(player.getPosition());
				player.setSpectatingEntity(elb);
				Covens.network.sendTo(new SpectatorStart(), player);
				locator = ItemStack.EMPTY;
				return true;
			} else {
				Tuple<BlockPos, Integer> posdim = getDestinationPosition();
				if (pos!=null) {
					BlockPos pos = posdim.getFirst();
					int dim = posdim.getSecond();
					if (p.world.provider.getDimension()==dim && consumePower(2000, false)) {
						player.getCapability(PlayerData.CAPABILITY, null).setSpectatingPoint(player.getPosition());
						EntityCrystalBallObserver observer = new EntityCrystalBallObserver(world);
						observer.setPosition(pos.getX(), pos.getY(), pos.getZ());
						world.spawnEntity(observer);
						player.setSpectatingEntity(observer);
						player.setPositionAndUpdate(pos.getX(), pos.getY()+0.2, pos.getZ());
						Covens.network.sendTo(new SpectatorStart(), player);
						locator = ItemStack.EMPTY;
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean consumePower(int power, boolean simulate) {
		if (power==0) return true;
		if (te==null || te.isInvalid()) te = TileEntityAltar.getClosest(pos, world);
		if (te==null) return false;
		return te.consumePower(power, simulate);
	}

	
	@SideOnly(Side.CLIENT)
	public static class Handler {

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public void renderPlayerHand(RenderHandEvent evt) {
			EntityPlayer p = net.minecraft.client.Minecraft.getMinecraft().player;
			evt.setCanceled(
					p!=null && p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint()!=null
			);
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public void renderPlayer(RenderLivingEvent.Pre<EntityPlayer> evt) {
			if (evt.getEntity() instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) evt.getEntity();
				evt.setCanceled(
						p!=null && p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint()!=null
				);
			}
		}
		
		@SideOnly(Side.CLIENT)
		@SubscribeEvent(receiveCanceled=true)
		public void onMouseEvent(MouseEvent evt) {
			EntityPlayer p = net.minecraft.client.Minecraft.getMinecraft().player;
			if (evt.isButtonstate() && p!=null && p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint()!=null) {
				evt.setCanceled(true);
				BlockPos point = p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint();
				p.setPositionAndUpdate(point.getX(), point.getY(), point.getZ());
				p.getCapability(PlayerData.CAPABILITY, null).setSpectatingPoint(null);
				Covens.network.sendToServer(new SpectatorStop());
				net.minecraft.client.Minecraft.getMinecraft().renderGlobal.loadRenderers();
			}
		}
		
		@SideOnly(Side.CLIENT)
		@SubscribeEvent(receiveCanceled=true)
		public void onKeyPressed(KeyInputEvent evt) {
			EntityPlayer p = net.minecraft.client.Minecraft.getMinecraft().player;
			if (p!=null && p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint()!=null) {
				BlockPos point = p.getCapability(PlayerData.CAPABILITY, null).getSpectatingPoint();
				p.setPositionAndUpdate(point.getX(), point.getY(), point.getZ());
				p.getCapability(PlayerData.CAPABILITY, null).setSpectatingPoint(null);
				Covens.network.sendToServer(new SpectatorStop());
				net.minecraft.client.Minecraft.getMinecraft().renderGlobal.loadRenderers();
			}
		}
	}
}
