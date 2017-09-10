package zabi.minecraft.covens.common.entity;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPlayerBody extends Entity {
	
	public static final List<ItemStack> list = Collections.emptyList();
	private static final DataParameter<String> OWNER = EntityDataManager.<String>createKey(EntityPlayerBody.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> ACTIVE = EntityDataManager.<Boolean>createKey(EntityPlayerBody.class, DataSerializers.BOOLEAN);
	
	public EntityPlayerBody(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit() {
		getDataManager().register(OWNER, "");
		getDataManager().register(ACTIVE, false);
		this.setSize(0.6F, 1.8F);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		setPlayer(compound.getString("caster"));
		setActive(compound.getBoolean("active"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("caster", getPlayerUUID());
		compound.setBoolean("active", isActive());
	}
	
	public void setPlayer(@Nonnull String playerUniqueId) {
		getDataManager().set(OWNER, playerUniqueId);
		getDataManager().setDirty(OWNER);
	}
	
	public void setActive(boolean active) {
		getDataManager().set(ACTIVE, active);
		getDataManager().setDirty(ACTIVE);
	}
	
	public boolean isActive() {
		return getDataManager().get(ACTIVE).booleanValue();
	}
	
	@Nonnull
	public String getPlayerUUID() {
		return getDataManager().get(OWNER);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return list;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
	}

	@Override
	public boolean getIsInvulnerable() {
		return true;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return isActive();
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (isActive()) {
			this.setDead();
			return EnumActionResult.SUCCESS;
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}
}
