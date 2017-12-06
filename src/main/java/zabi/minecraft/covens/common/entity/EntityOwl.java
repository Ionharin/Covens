package zabi.minecraft.covens.common.entity;

import java.util.UUID;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zabi.minecraft.covens.api.entity.IFamiliar;

public class EntityOwl extends EntityAnimal implements IFamiliar {

	public EntityOwl(World worldIn) {
		super(worldIn);
		this.setSize(0.5f, 0.5f);
		
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityOwl(this.world);
	}

	public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == Items.WHEAT;
    }
	
	@Override
	protected boolean canDespawn() {
		if (this instanceof IFamiliar) return ((IFamiliar)this).isBound();
		return super.canDespawn();
	}

	@Override
	public UUID getOwnerUUID() {
		return null;
	}

	@Override
	public boolean isBound() {
		return false;
	}
}
