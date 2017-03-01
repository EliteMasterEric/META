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
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMETA extends BlockContainer {
    private boolean isActive;

    public BlockMETA(boolean active) {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
        this.isActive = active;
        if(this.isActive)
            this.setLightLevel(0.625F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(META.instance, Reference.GUI_META, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        // TODO fix placement facing.
        if (!worldIn.isRemote) {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = state.getValue(BlockHorizontal.FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, enumfacing), 2);
        }
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
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
        if (tileentity instanceof TileModMaker) {
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.blockMETAInactive);
    }

    // Render particles.
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (isActive) {
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

    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        //keepInventory = true;

        if (active) {
            worldIn.setBlockState(pos, ModBlocks.blockMETAActive.getDefaultState().withProperty(BlockHorizontal.FACING, iblockstate.getValue(BlockHorizontal.FACING)), 3);
            worldIn.setBlockState(pos, ModBlocks.blockMETAActive.getDefaultState().withProperty(BlockHorizontal.FACING, iblockstate.getValue(BlockHorizontal.FACING)), 3);
        } else {
            worldIn.setBlockState(pos, ModBlocks.blockMETAInactive.getDefaultState().withProperty(BlockHorizontal.FACING, iblockstate.getValue(BlockHorizontal.FACING)), 3);
            worldIn.setBlockState(pos, ModBlocks.blockMETAInactive.getDefaultState().withProperty(BlockHorizontal.FACING, iblockstate.getValue(BlockHorizontal.FACING)), 3);
        }
        //keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
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

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockHorizontal.FACING).getIndex();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)));
    }
}
