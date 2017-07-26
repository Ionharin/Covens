package zabi.minecraft.covens.common.entity;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;

@Mod.EventBusSubscriber
public class EntityFlyingBroom extends Entity {
	
	private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityFlyingBroom.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ORIG_X = EntityDataManager.<Integer>createKey(EntityFlyingBroom.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ORIG_Y = EntityDataManager.<Integer>createKey(EntityFlyingBroom.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ORIG_Z = EntityDataManager.<Integer>createKey(EntityFlyingBroom.class, DataSerializers.VARINT);
	
	
	public EntityFlyingBroom(World world) {
		super(world);
	}
	
	public EntityFlyingBroom(World world, double x, double y, double z, int type) {
		this(world);
		this.setPosition(x, y, z);
		this.setType(type);
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		this.setSize(1, 1);
	}
	
	@Override
	public double getMountedYOffset() {
		return 0.4;
	}
	
	private BlockPos getMountPos() {
		return new BlockPos(this.getDataManager().get(ORIG_X), this.getDataManager().get(ORIG_Y), this.getDataManager().get(ORIG_Z));
	}
	
	public void setMountPos(BlockPos pos) {
		this.getDataManager().set(ORIG_X, pos.getX());
		this.getDataManager().set(ORIG_Y, pos.getY());
		this.getDataManager().set(ORIG_Z, pos.getZ());
		this.getDataManager().setDirty(ORIG_X);
		this.getDataManager().setDirty(ORIG_Y);
		this.getDataManager().setDirty(ORIG_Z);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(TYPE, 0);
		this.getDataManager().register(ORIG_X, 0);
		this.getDataManager().register(ORIG_Y, 0);
		this.getDataManager().register(ORIG_Z, 0);
	}
	
	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return entityIn instanceof EntityPlayer;
	}
	
	@Override
	public boolean canBePushed() {
        return true;
    }
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
		if (!getPassengers().isEmpty() && getPassengers().get(0).equals(entityIn)) return null;
        return entityIn.getEntityBoundingBox();
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.doBlockCollisions();
		
		int broomType = getType();
		
		float friction = broomType==0?0.99f:0.98f;
		if (onGround) friction = 0.8f;
		
		this.motionX*=friction;
		this.motionY*=friction;
		this.motionZ*=friction;
		EntityPlayer rider = (EntityPlayer) this.getControllingPassenger();
		if (this.isBeingRidden()) {
			if (rider==null) {
				Log.w(this+" is being ridden by a null rider!");
				return;
			}
			float front = rider.moveForward, up = rider.isJumping?1:0, strafe = rider.moveStrafing;
			Vec3d look = rider.getLookVec();
			
			if (broomType==0) handleMundaneMovement(front, look);
			if (broomType==1) handleElderMovement(front, up, strafe, look);
			if (broomType==2) handleJuniperMovement(front, up, strafe, look);
			if (broomType==3) {
				rider.noClip = true;
				handleYewMovement(front, up, strafe, look);
			}
			this.setRotationYawHead(rider.rotationYaw);
			
		} else {
			if (!this.isCollidedVertically) {
				motionY-=0.009;
				if (motionY<-0.5) motionY=-0.5;
			}
		}
		
		if (this.isCollidedHorizontally) {
			if (this.prevPosX==this.posX) motionX=0;
			if (this.prevPosZ==this.posZ) motionZ=0;
		}
		if (this.isCollidedVertically) {
			if (this.prevPosY==this.posY) motionY=0;
		}
		
		this.move(MoverType.SELF, motionX, motionY, motionZ);
	}
	
	private void handleYewMovement(float front, float up, float strafe, Vec3d look) {
		handleMundaneMovement(front, look);
	}

	private void handleJuniperMovement(float front, float up, float strafe, Vec3d look) {
		if (front>=0) {
			Vec3d horAxis = look.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(-strafe/10);
			motionX += front*(horAxis.x+look.x)/80;
			motionZ += front*(horAxis.z+look.z)/80;
			motionY += (up/80 + front*(horAxis.y+look.y)/80);

			if (motionX*motionX + motionY*motionY + motionZ*motionZ > 1) {
				Vec3d limit = new Vec3d(motionX,motionY,motionZ).normalize().scale(2);
				motionX=limit.x;
				motionY=limit.y;
				motionZ=limit.z;
			}
		} else {
			motionX/=1.1;
			motionY/=1.1;
			motionZ/=1.1;
		}
		
	}
	
	private void handleElderMovement(float front, float up, float strafe, Vec3d look) {
		Vec3d horAxis = look.crossProduct(new Vec3d(0, 1, 0)).normalize().scale(-strafe/10);
		motionX += front*(horAxis.x+look.x)/20;
		motionZ += front*(horAxis.z+look.z)/20;
		motionY += up/60;
		
		if (motionX*motionX + motionY*motionY + motionZ*motionZ > 8) {
			Vec3d limit = new Vec3d(motionX,motionY,motionZ).normalize().scale(2);
			motionX=limit.x;
			motionY=limit.y;
			motionZ=limit.z;
		}
		
	}
	
	@Override
	public void setRotationYawHead(float rotation) {
		this.prevRotationYaw = this.rotationYaw;
		this.rotationYaw = rotation;
	}

	private void handleMundaneMovement(float front, Vec3d look) {
		if (front>=0) {
			motionX += (0.1)*look.x/8;
			motionY += (0.1)*look.y/8;
			motionZ += (0.1)*look.z/8;
		} 
		
		if (motionX*motionX + motionZ*motionZ > 1) {
			Vec3d limit = new Vec3d(motionX,0,motionZ).normalize();
			motionX=limit.x;
			motionZ=limit.z;
		}
	}

	private void setType(int type) {
		this.getDataManager().set(TYPE, type);
		this.getDataManager().setDirty(TYPE);
	}
	
	public int getType() {
		return this.getDataManager().get(TYPE);
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
		if (this.getPassengers().size()==0) return null;
        return this.getPassengers().get(0);
    }
	
	@Override
	public boolean canPassengerSteer() {
		return true;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		this.setLocationAndAngles(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), tag.getFloat("yaw"), tag.getFloat("pitch"));
		this.setType(tag.getInteger("type"));
		this.setMountPos(new BlockPos(tag.getInteger("mx"), tag.getInteger("my"), tag.getInteger("mz")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setDouble("x", this.posX);
		compound.setDouble("y", this.posY);
		compound.setDouble("z", this.posZ);
		compound.setFloat("yaw", this.rotationYaw);
		compound.setFloat("pitch", this.rotationPitch);
		compound.setInteger("type", getType());
		BlockPos mount = getMountPos();
		compound.setInteger("mx", mount.getX());
		compound.setInteger("my", mount.getY());
		compound.setInteger("mz", mount.getZ());
		
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (isEntityInvulnerable(source)) return false;
		if (!world.isRemote && source.getTrueSource() instanceof EntityPlayer && !source.getTrueSource().equals(getControllingPassenger())) {
			EntityItem ei = new EntityItem(world, source.getTrueSource().posX, source.getTrueSource().posY, source.getTrueSource().posZ, new ItemStack(ModItems.broom,1,getType()));
			world.spawnEntity(ei);
			this.setDead();
			ei.onCollideWithPlayer((EntityPlayer) source.getTrueSource());
			return true;
		}
		return false;
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (!player.isRiding() && !player.isSneaking()) {
			player.rotationYaw = this.rotationYaw;
	        player.rotationPitch = this.rotationPitch;
			player.startRiding(this);
			if (getType()==3) this.setMountPos(player.getPosition());
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return Arrays.asList(ItemStack.EMPTY);
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {}
	
	@SubscribeEvent(receiveCanceled=false, priority=EventPriority.LOWEST)
	public static void onUnmounting(EntityMountEvent evt) {
		if (evt.getEntityBeingMounted() instanceof EntityFlyingBroom) {
			EntityFlyingBroom broom = (EntityFlyingBroom)evt.getEntityBeingMounted();
			World world = broom.world;
			EntityPlayer source = (EntityPlayer) evt.getEntityMounting();
			if (evt.isDismounting()) {
				if (broom.getType()==1) evt.getEntityMounting().fallDistance = -200;
				if (broom.getType()==3) {
					BlockPos start = broom.getMountPos();
					broom.setPositionAndUpdate(start.getX(), start.getY(), start.getZ());
					source.attemptTeleport(start.getX(), start.getY(), start.getZ());
				}
				if (!world.isRemote) { //TODO check for owl
					EntityItem ei = new EntityItem(world, source.posX, source.posY, source.posZ, new ItemStack(ModItems.broom,1, broom.getType()));
					world.spawnEntity(ei);
					broom.setDead();
					ei.onCollideWithPlayer(source);
				}
			}
		}
	}
	
}
