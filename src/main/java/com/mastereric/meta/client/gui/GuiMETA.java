package com.mastereric.meta.client.gui;

import com.mastereric.meta.Reference;
import com.mastereric.meta.common.blocks.container.ContainerMETA;
import com.mastereric.meta.common.blocks.container.ContainerModMaker;
import com.mastereric.meta.common.blocks.tile.TileMETA;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import com.mastereric.meta.util.LangUtility;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class GuiMETA extends GuiContainer {
    InventoryPlayer inventoryPlayer;
    TileMETA inventoryMETA;

    private static final int NAME_TEXT_COLOR = 4210752;
    private static final int META_NAME_X = 8;
    private static final int META_NAME_Y = 6;
    private static final int INVENTORY_NAME_X = 8;
    private static final int PROGRESS_X = 72;
    private static final int PROGRESS_Y = 63;
    private static final int PROGRESS_HEIGHT = 14;
    private static final int PROGRESS_WIDTH = 14;
    private static final int ENERGY_X = 151;
    private static final int ENERGY_Y = 67;
    private static final int ENERGY_HEIGHT = 54;
    private static final int ENERGY_WIDTH = 18;
    // Where the full meters are offscreen.
    private static final int METER_TEXTURE_X = 176;

    public GuiMETA(World w, InventoryPlayer inventoryPlayer, TileMETA tileEntity) {
        super(new ContainerMETA(w, inventoryPlayer, tileEntity));
        this.inventoryPlayer = inventoryPlayer;
        this.inventoryMETA = tileEntity;
    }

    private double getTicksRemaining() {
        return inventoryMETA.getTicksRemaining();
    }

    private int getTicksRemainingScaled(int progressIndicatorPixelHeight) {
        double percent = (getTicksRemaining() / (double) TileMETA.TICKS_PER_MOD);
        int value = (int) Math.floor(percent * progressIndicatorPixelHeight);
        //LogUtility.info("Remaining: %f / %d = %f, %d", getTicksRemaining(), TileMETA.TICKS_PER_MOD, percent, value);
        return value;
    }

    private double getCurrentEnergy() {
        //LogUtility.infoSided("Energy Stored: %d", inventoryMETA.getEnergyStored());
        return inventoryMETA.getEnergyStored();
    }

    private int getCurrentEnergyScaled(int energyGaugeHeight) {
        return (int) Math.floor(getCurrentEnergy() / (double) TileMETA.MAX_ENERGY_STORED * energyGaugeHeight);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int INVENTORY_NAME_Y = this.ySize - 94;
        this.fontRendererObj.drawString(this.inventoryMETA.getName(), META_NAME_X, META_NAME_Y, NAME_TEXT_COLOR);
        this.fontRendererObj.drawString(this.inventoryPlayer.getDisplayName().getUnformattedText(), INVENTORY_NAME_X, INVENTORY_NAME_Y, NAME_TEXT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Reference.TEXTURE_GUI_META);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        if(this.getTicksRemaining() != 0F) {
            // Draw fuel gauge
            int progress = this.getTicksRemainingScaled(PROGRESS_HEIGHT);
            this.drawTexturedModalRect(xPos + PROGRESS_X, yPos + PROGRESS_Y - progress, METER_TEXTURE_X, PROGRESS_HEIGHT - progress, PROGRESS_WIDTH, progress);
        }

        if(this.getCurrentEnergy() != 0F) {
            // Draw energy gauge
            int energy = this.getCurrentEnergyScaled(ENERGY_HEIGHT);
            this.drawTexturedModalRect(xPos + ENERGY_X, yPos + ENERGY_Y - energy, METER_TEXTURE_X, ENERGY_HEIGHT - energy + PROGRESS_HEIGHT, ENERGY_WIDTH, energy);
        }


        // If hovering over energy bar...
        if (this.isPointInRegion(ENERGY_X, ENERGY_Y - ENERGY_HEIGHT, ENERGY_WIDTH, ENERGY_HEIGHT, mouseX, mouseY)) {
            List<String> hoverText = new ArrayList<String>();
            hoverText.add(LangUtility.getTranslationFormat(Reference.NAME_LANG_META_STORAGE, (int) getCurrentEnergy(), TileMETA.MAX_ENERGY_STORED));
            this.drawHoveringText(hoverText, mouseX, mouseY);
        }
    }
}
