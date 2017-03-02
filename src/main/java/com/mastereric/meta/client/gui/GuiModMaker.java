package com.mastereric.meta.client.gui;

import com.mastereric.meta.Reference;
import com.mastereric.meta.common.blocks.container.ContainerModMaker;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import com.mastereric.meta.util.LangUtility;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import org.apache.commons.codec.language.bm.Lang;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class GuiModMaker extends GuiContainer {
    InventoryPlayer inventoryPlayer;
    TileModMaker inventoryModMaker;

    private static final int NAME_TEXT_COLOR = 4210752;
    private static final int MOD_MAKER_NAME_X = 8;
    private static final int MOD_MAKER_NAME_Y = 6;
    private static final int INVENTORY_NAME_X = 8;
    private static final int PROGRESS_X = 104;
    private static final int PROGRESS_Y = 37;
    private static final int PROGRESS_HEIGHT = 23;

    public GuiModMaker(World w, InventoryPlayer inventoryPlayer, TileModMaker tileEntity) {
        super(new ContainerModMaker(w, inventoryPlayer, tileEntity));
        this.inventoryPlayer = inventoryPlayer;
        this.inventoryModMaker = tileEntity;
    }

    private double getProgress() {
        double ticksRemaining = inventoryModMaker.getTicksRemaining();
        if (ticksRemaining == 0)
            return 100;
        if (ticksRemaining == TileModMaker.DEFAULT_WAIT_TIME)
            return 0;
        return (TileModMaker.DEFAULT_WAIT_TIME - ticksRemaining)/TileModMaker.DEFAULT_WAIT_TIME;
    }

    private int getProgressScaled(int progressIndicatorPixelHeight) {
        return (int) Math.floor(getProgress() * progressIndicatorPixelHeight);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        final int INVENTORY_NAME_Y = this.ySize - 128;
        this.fontRendererObj.drawString(this.inventoryModMaker.getName(), MOD_MAKER_NAME_X, MOD_MAKER_NAME_Y, NAME_TEXT_COLOR);
        this.fontRendererObj.drawString(this.inventoryPlayer.getDisplayName().getUnformattedText(), INVENTORY_NAME_X, INVENTORY_NAME_Y, NAME_TEXT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Reference.TEXTURE_GUI_MOD_MAKER);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        LogUtility.info("PROGRESS: %f", this.getProgress());
        int progress = this.getProgressScaled(PROGRESS_HEIGHT);
        this.drawTexturedModalRect(xPos + PROGRESS_X, yPos + PROGRESS_Y - progress, 176, PROGRESS_HEIGHT - progress, 16, progress);


        // If hovering over progress bar...
        if (this.isPointInRegion(PROGRESS_X, PROGRESS_Y - PROGRESS_HEIGHT, 16, PROGRESS_HEIGHT, mouseX, mouseY)) {
            List<String> hoverText = new ArrayList<String>();
            if (inventoryModMaker.isActive()) {
                hoverText.add(LangUtility.getTranslationFormat(Reference.NAME_LANG_MOD_MAKER_PROGRESS, getProgressScaled(100)));
            } else if (inventoryModMaker.getLastSpeedMultiplier() == 0) {
                hoverText.add(LangUtility.getTranslation(Reference.NAME_LANG_MOD_MAKER_ZERO_MULT));
            } else {
                hoverText.add(LangUtility.getTranslation(Reference.NAME_LANG_MOD_MAKER_COMPLETE));
            }
            this.drawHoveringText(hoverText, mouseX, mouseY);
        }
    }
}
