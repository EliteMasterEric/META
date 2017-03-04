package com.mastereric.meta.common.blocks;

import com.mastereric.meta.META;
import com.mastereric.meta.Reference;
import com.mastereric.meta.client.particles.ParticleMETAFlame;
import com.mastereric.meta.client.particles.ParticleModMaker;
import com.mastereric.meta.common.blocks.tile.TileMETA;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import com.mastereric.meta.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMETA extends BlockContainer {
    public static final PropertyDirection direction = BlockHorizontal.FACING;

    //TODO debug META orientation, ask Discord?
    //TODO add meta craft achievement trigger
    //TODO breaking does not drop contents

    public BlockMETA() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        //this.setLightLevel(0.625F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(META.instance, Reference.GUI_META, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(PROPERTY_FACING, placer.getHorizontalFacing().getOpposite()).withProperty(PROPERTY_ACTIVE, false), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileMETA) {
                ((TileModMaker)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileMETA) {

            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.blockMETA);
    }

    // Render particles.
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (stateIn.getValue(PROPERTY_ACTIVE)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            // Play furnace fire sound.
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D,
                        SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Particle particle;
            switch (stateIn.getValue(BlockHorizontal.FACING)) {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    particle = new ParticleMETAFlame(worldIn,
                            d0 - 0.52D, d1, d2 + d4,
                            0.0D, 0.0D, 0.0D);
                    Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                    particle = new ParticleMETAFlame(worldIn,
                            d0 + 0.52D, d1, d2 + d4,
                            0.0D, 0.0D, 0.0D);
                    Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                    particle = new ParticleMETAFlame(worldIn,
                            d0 + d4, d1, d2 - 0.52D,
                            0.0D, 0.0D, 0.0D);
                    Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                    particle = new ParticleMETAFlame(worldIn,
                            d0 + d4, d1, d2 + + 0.52D,
                            0.0D, 0.0D, 0.0D);
                    Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    break;
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMETA();
    }

    @Override
    public boolean hasTileEntity() { return true; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState blockState) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isOpaqueCube(IBlockState iBlockState) { return true; }

    @Override
    public boolean isFullCube(IBlockState iBlockState) { return true; }

    public static final PropertyDirection PROPERTY_FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PROPERTY_ACTIVE = PropertyBool.create("active");

    //@Override
    //@SideOnly(Side.CLIENT)
    //public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
    //    // Make this a list with multiple metadata valuse
    //    list.add(new ItemStack(itemIn, 1));
    //}

    // getStateFromMeta, getMetaFromState are used to interconvert between the block's property values and
    //   the stored metadata (which must be an integer in the range 0 - 15 inclusive)
    // The property is encoded as:
    // - lower two bits = facing direction (i.e. 0, 1, 2, 3)
    // - upper middle bit = active (i.e. 0, 4)
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        int activebit = (meta & 0x0c) >> 2; // 0x0c is hexadecimal, in binary 1100 - the upper middle bits, corresponding to the activity
        boolean active = (activebit == 1);
        return this.getDefaultState().withProperty(PROPERTY_FACING, facing).withProperty(PROPERTY_ACTIVE, active);
    }

    private static final int ACTIVE_LIGHT_VALUE = 13;
    private static final int INACTIVE_LIGHT_VALUE = 0;

    public boolean isMachineActive(IBlockAccess world, BlockPos pos) {
        TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tileentity instanceof TileMETA) {
            return ((TileMETA) tileentity).isActive();
        }
        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return isMachineActive(world, pos) ? ACTIVE_LIGHT_VALUE : INACTIVE_LIGHT_VALUE;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = state.getValue(PROPERTY_FACING);
        boolean active = state.getValue(PROPERTY_ACTIVE);

        int facingbits = facing.getHorizontalIndex();
        int activebit = (active ? 1 : 0) << 2;
        return activebit | facingbits;
    }

    // this method isn't required if your properties only depend on the stored metadata.
    // it is required if:
    // 1) you are making a multiblock which stores information in other blocks eg BlockBed, BlockDoor
    // 2) your block's state depends on other neighbours (eg BlockFence)
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)  {
        return getDefaultState().withProperty(PROPERTY_ACTIVE, isMachineActive(worldIn, pos))
                .withProperty(PROPERTY_FACING, state.getValue(PROPERTY_FACING));
    }

    // necessary to define which properties your blocks use
    // will also affect the variants listed in the blockstates model file
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {PROPERTY_ACTIVE, PROPERTY_FACING});
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PROPERTY_FACING, placer.getHorizontalFacing().getOpposite()).withProperty(PROPERTY_ACTIVE, false);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

}
