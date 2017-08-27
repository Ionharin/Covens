package zabi.minecraft.covens.common.entity;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import zabi.minecraft.covens.common.lib.Log;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.registries.spell.Spell;
import zabi.minecraft.covens.common.registries.spell.Spell.EnumSpellType;

public class EntitySpellCarrier extends EntityThrowable {
	
	private static final DataParameter<String> SPELL = EntityDataManager.<String>createKey(EntitySpellCarrier.class, DataSerializers.STRING);
	private static final DataParameter<String> CASTER = EntityDataManager.<String>createKey(EntitySpellCarrier.class, DataSerializers.STRING);
	
	public EntitySpellCarrier(World world) {
		super(world);
	}
	
	public EntitySpellCarrier(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	@Override
	protected void entityInit() {
		this.setEntityInvulnerable(true);
		this.setNoGravity(true);
		this.setSize(0.1f, 0.1f);
		this.getDataManager().register(SPELL, "");
		this.getDataManager().register(CASTER, "");
	}

	public void setSpell(Spell spell) {
		setSpell(spell.getRegistryName().toString());
	}
	
	private void setSpell(String spell) {
		this.getDataManager().set(SPELL, spell);
		this.getDataManager().setDirty(SPELL);
	}
	
	private void setCaster(String uuid) {
		this.getDataManager().set(CASTER, uuid);
		this.getDataManager().setDirty(CASTER);
	}
	
	public void setCaster(EntityLivingBase player) {
		setCaster(player.getUniqueID().toString());
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("spell", getSpellName());
		compound.setString("caster", getCasterUUID());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setSpell(compound.getString("spell"));
		this.setCaster(compound.getString("caster"));
	}
	
	private String getSpellName() {
		return this.getDataManager().get(SPELL);
	}
	
	@Nullable
	public Spell getSpell() {
		return Spell.REGISTRY.getValue(new ResourceLocation(getSpellName()));
	}
	
	private String getCasterUUID() {
		return this.getDataManager().get(CASTER);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (ticksExisted>40) {
			this.setDead();
		}
	}
	
	@Nullable
	public EntityLivingBase getCaster() {
		String uuid = getCasterUUID();
		EntityLivingBase player = this.world.getPlayerEntityByUUID(UUID.fromString(uuid));
		if (player!=null) return player;
		ArrayList<Entity> ent = new ArrayList<Entity>(world.getLoadedEntityList());
		for (Entity e:ent) if (e instanceof EntityLivingBase && uuid.equals(e.getUniqueID().toString())) return (EntityLivingBase) e;
		return null;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote) {
			Spell spell = getSpell();
			if (spell!=null) spell.performEffect(result, getCaster());
			else Log.w("Spell is null for "+this+" with spell reg name of "+getSpellName());
			if (result.typeOfHit == Type.BLOCK && (spell.getType()==EnumSpellType.PROJECTILE_BLOCK||spell.getType()==EnumSpellType.PROJECTILE_ALL)) this.setDead();
			if (result.typeOfHit == Type.ENTITY && (spell.getType()==EnumSpellType.PROJECTILE_ENTITY||spell.getType()==EnumSpellType.PROJECTILE_ALL)) this.setDead();
		}
	}

}
