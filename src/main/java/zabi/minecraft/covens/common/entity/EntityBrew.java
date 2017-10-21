package zabi.minecraft.covens.common.entity;

import java.util.List;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import zabi.minecraft.covens.common.capability.EntityData;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.potion.ModPotions;
import zabi.minecraft.covens.common.registries.brewing.BrewData;
import zabi.minecraft.covens.common.registries.brewing.CovenPotionEffect;
import zabi.minecraft.covens.common.registries.brewing.environmental.EnvironmentalPotionEffect;

public class EntityBrew extends EntityThrowable {
	
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityBrew.class, DataSerializers.ITEM_STACK);

	private ItemStack potion;
	
	public EntityBrew(World worldIn) {
		super(worldIn);
		setItem(ItemStack.EMPTY);
	}
	
	public EntityBrew(World worldIn, ItemStack potion) {
		this(worldIn);
		this.setItem(potion);
	}
	
	public EntityBrew(World worldIn, EntityLivingBase throwerIn, ItemStack potion) {
        super(worldIn, throwerIn.posX, throwerIn.posY + (double)throwerIn.getEyeHeight() - 0.10000000149011612D, throwerIn.posZ);
        this.setItem(potion);
    }
	
	public EntityBrew(World worldIn, double x, double y, double z, ItemStack potion) {
        this(worldIn, potion);
        this.setPosition(x, y, z);
        this.setItem(potion);
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit!=RayTraceResult.Type.MISS && result.getBlockPos()!=null) {
			ItemStack itemst = getItemStack();
			BrewData data = BrewData.getDataFromStack(itemst);
			Item type = itemst.getItem();
			if (type==ModItems.brew_splash) {
				splash(result, data);
//			} else if (type==ModItems.brew_gas) {
//				addEnvironmentalEffect(result, data);
			} else if (type==ModItems.brew_lingering) {
				linger(result, data);
			} else {
				Log.e("Invalid Brew type, please report to Covens' author");
			}
			setDead();
		}
	}

	private void linger(RayTraceResult result, BrewData data) {
		addEnvironmentalEffect(result, data);
		if (!world.isRemote) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, result.hitVec.x, result.hitVec.y, result.hitVec.z);
			entityareaeffectcloud.setOwner(this.getThrower());
			entityareaeffectcloud.setRadiusOnUse(0);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setRadiusPerTick(0);

			int persistency = 0;

			NonNullList<CovenPotionEffect> effects = data.getEffects();

			for (CovenPotionEffect pe:effects) {
				if (pe.getPotionEffect().getPotion().equals(ModPotions.tinting) || !pe.hasEntityEffect()) continue;
				entityareaeffectcloud.addEffect(pe.getPotionEffect());
				persistency += pe.getPersistency();
			}

			persistency /= effects.size();
			entityareaeffectcloud.setRadius(1+persistency);
			entityareaeffectcloud.setColor(data.getColor());
			entityareaeffectcloud.setDuration(80+persistency*60);
			this.world.spawnEntity(entityareaeffectcloud);
			this.world.playEvent(2007, new BlockPos(this), data.getColor());
		}
	}

	private void addEnvironmentalEffect(RayTraceResult result, BrewData data) {
		for (CovenPotionEffect cpe:data.getEffects()) {
			if (cpe.hasEnvironmentalEffect()) {
				EnvironmentalPotionEffect epe = EnvironmentalPotionEffect.getEffectForPotion(cpe.getPotionEffect().getPotion());
				if (epe!=null) {
					epe.splashedOn(world, result.getBlockPos(), this.getThrower(), cpe);
				}
			}
		}
	}

	private void splash(RayTraceResult result, BrewData data) {
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        if (!list.isEmpty()) {
            for (EntityLivingBase entitylivingbase : list) {
            	boolean isTinting = false;
                if (entitylivingbase.canBeHitWithPotion()) {
                    double distance = this.getDistanceSq(entitylivingbase);
                    if (distance < 16.0D) {
                        double falloffCoefficent = (1.0D - Math.sqrt(distance) / 4.0D);
                        if (entitylivingbase == result.entityHit) falloffCoefficent = 1.0D;
                        for (CovenPotionEffect cpotioneffect : data.getEffects()) {
                        	if (cpotioneffect.hasEntityEffect()) {
                        		PotionEffect potioneffect = cpotioneffect.getPotionEffect();
                        		Potion potion = potioneffect.getPotion();
                        		isTinting = potion.equals(ModPotions.tinting);
                        		if (potion.isInstant()) {
                        			potion.affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), falloffCoefficent);
                        		} else {
                        			int i = (int)(cpotioneffect.getMultiplier() * falloffCoefficent * (double)potioneffect.getDuration() + 0.5D);
                        			if (i > 20) {
                        				entitylivingbase.addPotionEffect(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
                        			}
                        		}
                        	}
                        }
                    }
                }
                if (isTinting) {
                	entitylivingbase.getCapability(EntityData.CAPABILITY, null).setTint(data.getColor());
                }
            }
        }
        addEnvironmentalEffect(result, data);
        if (!world.isRemote) this.world.playEvent(2007, new BlockPos(this), data.getColor());
	}

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		potion = new ItemStack(compound.getCompoundTag("potion"));
		setItem(potion);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = super.writeToNBT(compound);
		NBTTagCompound item = new NBTTagCompound();
		potion.writeToNBT(item);
		tag.setTag("potion", item);
		return tag;
	}
	
	public ItemStack getItemStack() {
		return getDataManager().get(ITEM);
	}

	public void setItem(ItemStack stack) {
		this.getDataManager().set(ITEM, stack);
		this.getDataManager().setDirty(ITEM);
		potion = stack;
	}
}
