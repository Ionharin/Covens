package zabi.minecraft.covens.common.entity;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.item.ModItems;

public class EntityFlyingBroom extends Entity {
	
	private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityFlyingBroom.class, DataSerializers.VARINT);
	
	public EntityFlyingBroom(World world) {
		super(world);
	}
	
	public EntityFlyingBroom(World world, double x, double y, double z, int type) {
		this(world);
		this.setPosition(x, y, z);
		this.setType(type);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(TYPE, 0);
	}
	
	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return entityIn instanceof EntityPlayer;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (this.isBeingRidden()) {
			EntityPlayer rider = (EntityPlayer) this.getRidingEntity();
			float front = rider.moveForward, up = rider.moveVertical, straf = rider.moveStrafing;
			Vec3d look = rider.getLookVec();
			int playerAgility = 1;
			switch (getType()) {
			
			case 0: //Mundane woods - only looking direction, no strafe, no ascension
				Vec3d diff = look.subtract(this.getLookVec());
				float yawDiff = (float) ((rider.rotationYaw - this.rotationYaw)*diff.lengthSquared())/(4-playerAgility);
				this.moveRelative(0f, (float) diff.y*front*playerAgility, front*playerAgility, 0.5f);
				this.setRotation(this.rotationYaw + yawDiff, this.rotationPitch);
				break;
			case 1: //Elder - high frontal speed, low ascension, low steering
				this.setRotation(rider.rotationYaw, rider.rotationPitch);
				this.moveRelative(straf/(6-playerAgility), up/(6-playerAgility), front/(4-playerAgility), 0.1f);
				break;
			case 2: //Juniper - high manovrability, low speed
				this.setRotation(rider.rotationYaw, rider.rotationPitch);
				this.moveRelative(straf/(4-playerAgility), up/(4-playerAgility), front/(6-playerAgility), 0.3f);
				break;
			case 3: //Yew - average
				this.setRotation(rider.rotationYaw, rider.rotationPitch);
				this.moveRelative(straf/(5-playerAgility), up/(5-playerAgility), front/(5-playerAgility), 0.2f);
				break;
			}
		}
	}
	
	@Override
	public void removePassengers() {
		List<Entity> passengers = this.getPassengers();
        for (int i = passengers.size() - 1; i >= 0; --i) {
        	Entity passenger = passengers.get(i);
            passenger.dismountRidingEntity();
            passenger.setPosition((int) this.posX, world.getHeight((int) this.posX, (int) this.posZ)+1, (int) this.posZ);
        }
        if (!world.isRemote) {
        	world.spawnEntity(new EntityItem(world, (int) this.posX, world.getHeight((int) this.posX, (int) this.posZ)+1, (int) this.posZ, new ItemStack(ModItems.broom,1,getType())));
        }
        this.setDead();
    }
	
	private void setType(int type) {
		this.getDataManager().set(TYPE, type);
		this.getDataManager().setDirty(TYPE);
	}
	
	private int getType() {
		return this.getDataManager().get(TYPE);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		
	}
	

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Arrays.asList(ItemStack.EMPTY);
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {}
	
}
