package zabi.minecraft.covens.common.block;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import zabi.minecraft.covens.common.lib.Log;
import zabi.minecraft.covens.common.lib.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zabi.minecraft.covens.client.particle.ParticleSmallFlame;
import zabi.minecraft.covens.common.Covens;
import zabi.minecraft.covens.common.block.BlockCircleGlyph.GlyphType;
import zabi.minecraft.covens.common.item.ModCreativeTabs;
import zabi.minecraft.covens.common.item.ModItems;
import zabi.minecraft.covens.common.registries.ritual.Ritual;
import zabi.minecraft.covens.common.tileentity.TileEntityAltar;
import zabi.minecraft.covens.common.tileentity.TileEntityGlyph;

public class BlockCandlePlate extends Block {

	private static final AxisAlignedBB bounding_box = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.21875, 0.875);
	
	public BlockCandlePlate() {
		super(Material.IRON);
		this.setUnlocalizedName("candle_plate");
		this.setCreativeTab(ModCreativeTabs.machines);
		this.setRegistryName(Reference.MID, "candle_plate");
		this.setHarvestLevel("pickaxe", 0);
		this.setLightOpacity(0);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return bounding_box;
	}
	
	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextBoolean()) {
			ParticleSmallFlame p = new ParticleSmallFlame(worldIn, pos.getX()+0.35, pos.getY()+0.22, pos.getZ()+0.35, 0, 0, 0, 0.03f);
			Covens.proxy.spawnParticle(p);
		}
		if (rand.nextBoolean()) {
			ParticleSmallFlame p1 = new ParticleSmallFlame(worldIn, pos.getX()+0.65, pos.getY()+0.273, pos.getZ()+0.37, 0, 0, 0, 0.03f);
			Covens.proxy.spawnParticle(p1);
		}
		if (rand.nextBoolean()) {
			ParticleSmallFlame p2 = new ParticleSmallFlame(worldIn, pos.getX()+0.65, pos.getY()+0.15, pos.getZ()+0.65, 0, 0, 0, 0.03f);
			Covens.proxy.spawnParticle(p2);
		}
		if (rand.nextBoolean()) {
			ParticleSmallFlame p3 = new ParticleSmallFlame(worldIn, pos.getX()+0.35, pos.getY()+0.22, pos.getZ()+0.65, 0, 0, 0, 0.03f);
			Covens.proxy.spawnParticle(p3);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem()==ModItems.ritual_knife) {
			startRitual(playerIn, worldIn, pos);
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	public void startRitual(EntityPlayer player, World world, BlockPos pos) {
		if (player.getEntityWorld().isRemote) return;
		List<EntityItem> itemsOnGround = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(3, 0, 3));
		List<ItemStack> recipe = itemsOnGround.stream().map(i -> i.getItem()).collect(Collectors.toList());
		for (Ritual rit:Ritual.REGISTRY) {
			if (rit.isValidInput(recipe, hasCircles(rit, pos, world))) {
				if (rit.isValid(player, world, pos, recipe)) {
					if (consumePower(rit.getRequiredStartingPower(), world, pos)) {
						NBTTagCompound ritualData = new NBTTagCompound();
						NBTTagCompound itemsUsed = new NBTTagCompound();
						ritualData.setTag("itemsUsed", itemsUsed);
						itemsOnGround.forEach(ei -> {
							NBTTagCompound item = new NBTTagCompound();
							ei.getItem().writeToNBT(item);
							ritualData.getCompoundTag("itemsUsed").setTag("item"+ritualData.getCompoundTag("itemsUsed").getKeySet().size(), item);
							ei.getItem().setCount(ei.getItem().getCount()-1);
							if (ei.getItem().getCount()<1) ei.setDead();
						});
						ItemStack stack = new ItemStack(ModBlocks.ritual_candle);
						NBTTagCompound tag = stack.getOrCreateSubCompound("ritual_data");
						tag.setString("ritual", rit.getRegistryName().toString());
						tag.setString("player", player.getPersistentID().toString());
						tag.setTag("data", ritualData);
						EntityItem eires = new EntityItem(world, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, stack);
						eires.setDefaultPickupDelay();
						world.spawnEntity(eires);
						EntityItem candles = new EntityItem(world, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, new ItemStack(ModItems.candle,3));
						candles.setDefaultPickupDelay();
						world.spawnEntity(candles);
						world.setBlockToAir(pos);
						return;
					} else {
						player.sendStatusMessage(new TextComponentTranslation("ritual.failure.power", new Object[0]), true);
						return;
					}
				} else {
					player.sendStatusMessage(new TextComponentTranslation("ritual.failure.precondition", new Object[0]), true);
					return;
				}

			}
		}
		player.sendStatusMessage(new TextComponentTranslation("ritual.failure.unknown", new Object[0]), true);
	}
	
	private boolean consumePower(int requiredStartingPower, World world, BlockPos pos) {
		TileEntityAltar altar = TileEntityAltar.getClosest(pos, world);
		if (altar!=null && !altar.isInvalid()) return altar.consumePower(requiredStartingPower, false);
		return false;
	}

	private boolean hasCircles(Ritual rit, BlockPos pos, World world) {
		int requiredCircles = rit.getCircles() & 3;
		if (requiredCircles==3) return false;
		GlyphType typeFirst = BlockCircleGlyph.GlyphType.values()[rit.getCircles()>>2 & 3];
		GlyphType typeSecond = BlockCircleGlyph.GlyphType.values()[rit.getCircles()>>4 & 3];
		GlyphType typeThird = BlockCircleGlyph.GlyphType.values()[rit.getCircles()>>6 & 3];
		if (requiredCircles>1) if (!checkThird(typeThird, pos, world)) {
			Log.d(rit.getRegistryName()+" -> Misplaced Circle 3, expected: "+typeThird);
			return false;
		}
		if (requiredCircles>0) if (!checkSecond(typeSecond, pos, world)) {
			Log.d(rit.getRegistryName()+" -> Misplaced Circle 2, expected: "+typeSecond);
			return false;
		}
		if (!checkFirst(typeFirst, pos, world)) {
			Log.d(rit.getRegistryName()+" -> Misplaced Circle 1, expected: "+typeFirst);
			return false;
		}
		return true;
	}
	
	private boolean checkFirst(GlyphType typeFirst, BlockPos pos, World world) {
		for (int[] c:TileEntityGlyph.getSmall()) {
			BlockPos bp = pos.add(c[0], 0, c[1]);
			IBlockState bs = world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.glyphs) || !bs.getValue(BlockCircleGlyph.TYPE).equals(typeFirst)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkSecond(GlyphType typeSecond, BlockPos pos, World world) {
		for (int[] c:TileEntityGlyph.getMedium()) {
			BlockPos bp = pos.add(c[0], 0, c[1]);
			IBlockState bs = world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.glyphs) || !bs.getValue(BlockCircleGlyph.TYPE).equals(typeSecond)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkThird(GlyphType typeThird, BlockPos pos, World world) {
		for (int[] c:TileEntityGlyph.getBig()) {
			BlockPos bp = pos.add(c[0], 0, c[1]);
			IBlockState bs = world.getBlockState(bp);
			if (!bs.getBlock().equals(ModBlocks.glyphs) || !bs.getValue(BlockCircleGlyph.TYPE).equals(typeThird)) {
				return false;
			}
		}
		return true;
	}
	
	
}
