package zabi.minecraft.covens.common.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityCrystalBallObserver extends Entity {
	
	private static final DataParameter<String> CASTER = EntityDataManager.<String>createKey(EntitySpellCarrier.class, DataSerializers.STRING);

	public EntityCrystalBallObserver(World worldIn) {
		super(worldIn);
		this.setSize(3, 0.1f);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(CASTER, "");
		this.setNoGravity(true);
		this.setEntityInvulnerable(true);
		this.setInvisible(true);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public boolean canBePushed() {
		return true;
	}
	
	private void setCaster(String uuid) {
		this.getDataManager().set(CASTER, uuid);
		this.getDataManager().setDirty(CASTER);
	}
	
	public void setCaster(EntityPlayerMP player) {
		if (player!=null) setCaster(player.getUniqueID().toString());
		else setCaster("");
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setString("caster", getCasterUUID());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.setCaster(compound.getString("caster"));
	}
	
	private String getCasterUUID() {
		return this.getDataManager().get(CASTER);
	}
	
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return getEntityBoundingBox();
	}
	
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (ticksExisted%400==0) {
			String uuid = getCasterUUID();
			if (uuid.equals("")) {
				setDead();
				return;
			}
			EntityPlayer p = world.getPlayerEntityByUUID(UUID.fromString(uuid));
			if (p==null) {
				setDead();
				return;
			}
			if (p instanceof EntityPlayerMP) {
				EntityPlayerMP mp = (EntityPlayerMP) p;
				if (mp.getSpectatingEntity()!=this) setDead();
			}
		}
	}

}
