package zabi.minecraft.covens.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zabi.minecraft.covens.common.lib.Reference;

//Wow, the default vanilla implementation for BlockCrops sucks
public class BlockModCrop extends BlockCrops {
	
	ItemStack seedType = ItemStack.EMPTY;
	ItemStack dropType = ItemStack.EMPTY;
	
	public BlockModCrop(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setRegistryName(Reference.MID, name);
		GameRegistry.register(this);
	}
	
	public BlockModCrop setSeeds(ItemStack seed) {
		seedType=seed;
		return this;
	}
	
	@Override
	protected Item getSeed() {
		return seedType.getItem();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return seedType.copy();
	}
	
	@Override
	protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.getInt(worldIn.rand, 1, 4);
    }
	
	@Override
	protected Item getCrop() {
        return dropType.getItem();
    }
	
	@Override
	public int damageDropped(IBlockState state) {
		return ((BlockModCrop)state.getBlock()).getDropType().getMetadata();
	}
	
	public BlockModCrop setDropType(ItemStack stack) {
		dropType = stack.copy();
		return this;
	}
	
	public ItemStack getDropType() {
		return dropType.copy();
	}
	
	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            List<ItemStack> items = NonNullList.create();
            items.add(seedType.copy());
            if (this.getAge(state)>=this.getMaxAge()) {
            	ItemStack harvested = getDropType();
            	if (Math.random()>0.7) harvested.setCount(2);
            	items.add(harvested);
            }
            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());
            for (ItemStack item : items) {
            	spawnAsEntity(worldIn, pos, item);
            }
        }
	}
	
	@Override
    public List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		
		 List<ItemStack> ret = new ArrayList<ItemStack>();
		 Item item = getItemDropped(state, null, 0);
		 if (item != Items.AIR) {
			 ret.add(new ItemStack(item, 1, damageDropped(state)));
		 }
		 int age = getAge(state);
		 if (age >= getMaxAge()) {
			 ret.add(new ItemStack(getSeed()));
        }
        return ret;
    }
	
}
