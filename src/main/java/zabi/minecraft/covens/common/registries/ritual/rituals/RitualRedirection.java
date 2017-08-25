package zabi.minecraft.covens.common.registries.ritual.rituals;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class RitualRedirection extends Ritual {

	public static final int TP_SENSITIVITY = 4, RADIUS = 20;
	
	public RitualRedirection(NonNullList<Ingredient> input, NonNullList<ItemStack> output, int timeInTicks, int circles, int altarStartingPower, int powerPerTick) {
		super(input, output, timeInTicks, circles, altarStartingPower, powerPerTick);
		this.setRegistryName(Reference.MID, "redirection");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onUpdate(EntityPlayer player, TileEntityGlyph tile, World world, BlockPos pos, NBTTagCompound data, int ticks) {
		if (world.isRemote) return;
		NBTTagCompound dest = null;
		for (String iname:data.getCompoundTag("itemsUsed").getKeySet()) {
			ItemStack stack = new ItemStack(data.getCompoundTag("itemsUsed").getCompoundTag(iname));
			if (stack.getItem().equals(ModItems.cardinal_stone) && stack.getMetadata()==2) {
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null || !dest.hasKey("x")) {
			Log.w("Error in nbt ritual data: missing destination for redirection");
			return;
		}
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(RADIUS)).stream()
			.filter(e -> shouldRedirect(e))
			.filter(e -> !tile.isInList(e))
			.forEach(e -> {
				if (tile.consumePower((int) (pos.distanceSq(x, y, z)/10))) e.attemptTeleport(x, y, z);
			});
	}
	
	@Override
	public boolean isValid(EntityPlayer player, World world, BlockPos pos, List<ItemStack> recipe) {
		NBTTagCompound dest = null;
		for (ItemStack stack:recipe) {
			if (stack.getItem().equals(ModItems.cardinal_stone) && stack.getMetadata()==2) {
				dest = stack.getOrCreateSubCompound("pos");
				break;
			}
		}
		if (dest==null || !dest.hasKey("x")) {
			return false;
		}
		double x = dest.getDouble("x"),y = dest.getDouble("y"),z = dest.getDouble("z");
		if (Math.abs(x-pos.getX())<RADIUS && Math.abs(y - pos.getY())<RADIUS && Math.abs(z - pos.getZ())<RADIUS) {
			return false; //Destination is inside, it would cause it to intercept itself
		}
		return true;
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void markTeleported(EntityTravelToDimensionEvent evt) {
		if (evt.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
			entity.addTag("TPMK:"+entity.ticksExisted);
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void removeMarks(EntityJoinWorldEvent evt) {
		for (String s:evt.getEntity().getTags()) if (s.startsWith("TPMK:")) evt.getEntity().removeTag(s);
	}
	
	private static boolean shouldRedirect(EntityLivingBase e) {
		boolean sameDim = e.getDistanceSq(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ)>TP_SENSITIVITY;
		if (sameDim) return true;
		boolean teleDim = false;
		for (String s:e.getTags()) for (int dt=0;dt>-4;dt--) {
			if (s.equals("TPMK:"+(e.ticksExisted+dt))) {
				teleDim = true;
				break;
			}
		}
		return teleDim;
	}

}
