package zabi.minecraft.covens.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.covens.common.lib.Reference;
import zabi.minecraft.covens.common.potion.ModPotions;

public class BlockConfiningAsh extends Block {
	
	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool WEST = PropertyBool.create("west");
	
	public static final String MARKED_UNDEAD = "marked_undead";
	
	protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0025D, 1.0D);
	protected static final AxisAlignedBB BLOCKING_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 3.0D, 1.0D);

	public BlockConfiningAsh() {
		super(Material.CIRCUITS);
		this.setUnlocalizedName("confining_ash");
		this.setRegistryName(Reference.MID, "confining_ash");
		MinecraftForge.EVENT_BUS.register(this);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(EAST, false).withProperty(WEST, false));
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state
				.withProperty(NORTH, world.getBlockState(pos.north()).getBlock().equals(ModBlocks.confining_ash))
				.withProperty(SOUTH, world.getBlockState(pos.south()).getBlock().equals(ModBlocks.confining_ash))
				.withProperty(EAST, world.getBlockState(pos.east()).getBlock().equals(ModBlocks.confining_ash))
				.withProperty(WEST, world.getBlockState(pos.west()).getBlock().equals(ModBlocks.confining_ash));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState floor = worldIn.getBlockState(pos.down());
		return floor.getBlock().canPlaceTorchOnTop(floor, worldIn, pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FLAT_AABB;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<ItemStack>(0);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
		return PathNodeType.WALKABLE;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return null;
	}

    @Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	world.notifyBlockUpdate(fromPos, state, getActualState(state, world, pos), 10);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
    	return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST);
    }
    
    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    	super.onBlockClicked(worldIn, pos, playerIn);

    }
    
    @SubscribeEvent
    public void onPlayerClicked(LeftClickBlock event) {
    	if (event.getWorld().getBlockState(event.getPos()).getBlock().equals(this)) {
    		event.setCancellationResult(EnumActionResult.FAIL);
    		EntityPlayer playerIn = event.getEntityPlayer();
    		if (playerIn.getActivePotionEffect(ModPotions.skin_rotting)!=null) {
    			event.setCanceled(true);
    		}
    	}
    }
    
    private static final AxisAlignedBB wall = new AxisAlignedBB(0, 0, 0, 1, 3, 1);
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
    	if (entityIn instanceof EntityLivingBase && (((EntityLivingBase) entityIn).getCreatureAttribute()==EnumCreatureAttribute.UNDEAD || ((EntityLivingBase) entityIn).getActivePotionEffect(ModPotions.skin_rotting)!=null)) {
    		addCollisionBoxToList(pos, entityBox, collidingBoxes, wall);
    		if (worldIn.isRemote && Math.random()<0.3) worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.getX()+Math.random(), pos.getY()+2*Math.random(), pos.getZ()+Math.random(), 0, 0, 0);
    	}
    }
	
}
