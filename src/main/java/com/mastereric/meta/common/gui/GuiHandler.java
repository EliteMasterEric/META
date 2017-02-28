package com.mastereric.meta.common.gui;

import com.mastereric.meta.Reference;
import com.mastereric.meta.client.gui.GuiMETA;
import com.mastereric.meta.client.gui.GuiModMaker;
import com.mastereric.meta.common.blocks.container.ContainerMETA;
import com.mastereric.meta.common.blocks.container.ContainerModMaker;
import com.mastereric.meta.common.blocks.tile.TileMETA;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler{

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case Reference.GUI_MOD_MAKER:
                return new ContainerModMaker(world, player.inventory, (TileModMaker) tileEntity);
            case Reference.GUI_META:
                return new ContainerMETA(world, player.inventory, (TileMETA) tileEntity);
        }
        return null;
    }
    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case Reference.GUI_MOD_MAKER:
                return new GuiModMaker(world, player.inventory, (TileModMaker) tileEntity);
            case Reference.GUI_META:
                return new GuiMETA(world, player.inventory, (TileMETA) tileEntity);
        }
        return null;
    }
}
