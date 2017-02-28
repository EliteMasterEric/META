package com.mastereric.meta.common.blocks;

import com.mastereric.meta.META;
import com.mastereric.meta.Reference;
import com.mastereric.meta.client.particles.ParticleModMaker;
import com.mastereric.meta.common.blocks.tile.TileMETA;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import com.mastereric.meta.init.ModBlocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockModMaker extends BlockContainer {
    public BlockModMaker() {
        super(Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(META.instance, Reference.GUI_MOD_MAKER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileModMaker) {
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
        return Item.getItemFromBlock(ModBlocks.blockModMaker);
    }

    // Render particles.
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }

                if (rand.nextInt(16) == 0) {
                    for (int k = 0; k <= 1; ++k) {
                        if (isWorking(worldIn, pos)) {
                            if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                                break;
                            }
                            Particle particle = new ParticleModMaker(worldIn,
                                    (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D,(double)pos.getZ() + 0.5D,
                                    (double)((float)i + rand.nextFloat()) - 0.5D, (double)((float)k - rand.nextFloat() - 1.0F), (double)((float)j + rand.nextFloat()) - 0.5D);
                            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                        }
                    }
                }
            }
        }
    }

    // Whether this BlockModMaker is working on creating a mod.
    private boolean isWorking(World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileModMaker) {
            return ((TileModMaker) tileentity).isActive();
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileModMaker();
    }

    // Bounding box (copied from Enchanting Table)
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
