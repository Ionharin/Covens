package zabi.minecraft.covens.common.entity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.network.messages.ChangeShellOwner;

public class EntityPlayerShell extends EntityLivingBase {

	private static final DataParameter<String> CASTER = EntityDataManager.<String>createKey(EntityPlayerShell.class, DataSerializers.STRING);
	
	public static HashMap<String, EntityPlayerShell> map = new HashMap<String, EntityPlayerShell>();
	
	private String playerUUID = null;
	private WeakReference<EntityPlayer> player;

	public EntityPlayerShell(World worldIn) {
		super(worldIn);
		setSize(1, 2);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(CASTER, "");
	}
	
	public EntityPlayerShell(World worldIn, EntityPlayer player) {
		this(worldIn);
		setCaster(player.getUniqueID().toString());
		this.player = new WeakReference<EntityPlayer>(player);
	}
	
	@Override
	public void onLivingUpdate() {
		Log.d("I'm at "+this.getPosition());
	}
	
	public void setCaster(String uuid) {
		this.getDataManager().set(CASTER, uuid);
		this.getDataManager().setDirty(CASTER);
		if (uuid==null||uuid.equals("")) playerUUID = null;
		else playerUUID = uuid;
		if (!world.isRemote) {
			Covens.network.sendToDimension(new ChangeShellOwner(this, uuid), world.provider.getDimension());
			if (map.containsKey(uuid)) {
				map.remove(uuid).setDead();
			}
			map.put(uuid, this);
		}
	}
	
	private String getCaster() {
		return this.getDataManager().get(CASTER);
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.<ItemStack>create();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return EnumHandSide.RIGHT;
	}
	
	private boolean canFindPlayer() {
		if (player!=null && player.get()!=null) return true; //Already loaded
		if (playerUUID==null) { 
			return false; //Cannot retrieve
		}
		UUID uid = null;
		try {
			uid = UUID.fromString(playerUUID);
		} catch (IllegalArgumentException e) {
			return false; //Wrong uuid
		}
		EntityPlayer p = world.getPlayerEntityByUUID(uid);
		if (p!=null) {
			player = new WeakReference<EntityPlayer>(p);
			return true; //Retrieved and found
		}
		return false; //Disconnected
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("caster", getCaster());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setCaster(compound.getString("caster"));
	}
	
	public void recallPlayer() {
		if (canFindPlayer()) {
			EntityPlayer original = player.get();
			if (original.dimension!=this.dimension) original.changeDimension(this.dimension);
			original.setPositionAndUpdate(this.posX, this.posY, this.posZ);
			original.motionX = this.motionX;
			original.motionY = this.motionY;
			original.motionZ = this.motionZ;
			original.rotationPitch = this.rotationPitch;
			original.rotationYaw = this.rotationYaw;
			this.setDead();
		}
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		recallPlayer();
		return true;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		recallPlayer();
		return true;
	}
	
	@Override
	public boolean attemptTeleport(double x, double y, double z) {
		recallPlayer();
		return true;
	}
	
	@Override
	public boolean isInvisible() {
		return !canFindPlayer();
	}
	
	@Override
	public void setFire(int seconds) {
		recallPlayer();
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		recallPlayer();
	}
	
	@Nullable
	public EntityPlayer getPlayer() {
		canFindPlayer();
		return player==null?null:player.get();
	}
	
	public static void removePlayerShells(EntityPlayer p) {
		if (map.containsKey(p.getUniqueID().toString())) {
			map.remove(p.getUniqueID().toString()).setDead();
		}
	}
}
