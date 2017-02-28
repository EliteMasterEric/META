package com.mastereric.meta.common.blocks.container;

import com.mastereric.meta.common.blocks.tile.TileMETA;
import com.mastereric.meta.common.blocks.tile.TileModMaker;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMETA extends Container {
    private final TileMETA tileMETA;
    private int currentModRemainingTicks = 0;
    private int currentEnergyStorage = 0;
    private final World world;

    // Numbers to keep track of the inventory.
    // No magic numbers here!
    // MOD_MAKER slots are in the machine (slot index 0)
    // INVENTORY slots are the players's inventory slots (slot index 1
    // HOTBAR slots are the hotbar (

    // Spacing is the amount of space between slots.
    // The XPOS and YPOS are the position of the first slot of that part of the inventory.
    private final int SLOT_X_SPACING = 18;
    private final int SLOT_Y_SPACING = 18;
    private final int META_SLOT_XPOS = 71;
    private final int META_SLOT_YPOS = 32;
    private final int PLAYER_INVENTORY_XPOS = 8;
    private final int PLAYER_INVENTORY_YPOS = 84;
    private final int HOTBAR_PAD = 4;
    private final int HOTBAR_XPOS = 8;
    private final int HOTBAR_YPOS = PLAYER_INVENTORY_YPOS + (SLOT_Y_SPACING * 3) + HOTBAR_PAD;

    private final int INVENTORY_ROW_COUNT = 3;
    private final int INVENTORY_COLUMN_COUNT = 9;
    private final int HOTBAR_SLOT_COUNT = INVENTORY_COLUMN_COUNT;
    private final int INVENTORY_SLOT_COUNT = INVENTORY_ROW_COUNT * INVENTORY_COLUMN_COUNT;
    private final int PLAYER_SLOT_COUNT = INVENTORY_SLOT_COUNT + HOTBAR_SLOT_COUNT;
    private final int META_SLOT_COUNT = 1;

    private final int PLAYER_FIRST_SLOT_INDEX = 0;
    private final int META_FIRST_SLOT_INDEX = PLAYER_FIRST_SLOT_INDEX + PLAYER_SLOT_COUNT;

    public ContainerMETA(World w, InventoryPlayer playerInventory, TileMETA tileMETA) {
        this.tileMETA = tileMETA;
        this.world = w;

        // Hotbar
        for (int x = 0; x < INVENTORY_COLUMN_COUNT; x++) {
            int slotNumber = x;
            int xPos = HOTBAR_XPOS + SLOT_X_SPACING * x;
            int yPos = HOTBAR_YPOS;
            this.addSlotToContainer(new Slot(playerInventory, slotNumber, xPos, yPos));
        }

        // Player inventory
        for (int y = 0; y < INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * INVENTORY_COLUMN_COUNT + x;
                int xPos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int yPos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                this.addSlotToContainer(new Slot(playerInventory, slotNumber, xPos, yPos));
            }
        }

        // Output slot.
        this.addSlotToContainer(new SlotItemHandler(tileMETA.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP),
                0, META_SLOT_XPOS, META_SLOT_YPOS));
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.currentModRemainingTicks != this.tileMETA.getTicksRemaining()) {
                listener.sendProgressBarUpdate(this, 0, tileMETA.getTicksRemaining());
            }
            if (this.currentEnergyStorage != this.tileMETA.getEnergyStored()) {
                listener.sendProgressBarUpdate(this, 0, tileMETA.getEnergyStored());
            }
        }

        this.currentModRemainingTicks = tileMETA.getTicksRemaining();
        this.currentEnergyStorage = tileMETA.getEnergyStored();
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileMETA.isUsableByPlayer(playerIn);
    }

    /**
     * Perform an action on SHIFT-Click
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex) {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack sourceStackCopy = sourceStack.copy();

        if (PLAYER_FIRST_SLOT_INDEX <= sourceSlotIndex && sourceSlotIndex < PLAYER_FIRST_SLOT_INDEX + PLAYER_SLOT_COUNT) {
            // The source slot was in the player's inventory.
            if(sourceStack.getItem().equals(ModItems.itemMod)) {
                if (!mergeItemStack(sourceStack, META_FIRST_SLOT_INDEX, META_FIRST_SLOT_INDEX + 0, false)) {
                    // No room in M.E.T.A., stop trying to move stuff.
                    return ItemStack.EMPTY;  //EMPTY_ITEM;
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else if (sourceSlotIndex == META_FIRST_SLOT_INDEX) {
            // The source slot was in the Mod Maker's inventory.
            // Put the item into the player's inventory.
            if (!mergeItemStack(sourceStack, PLAYER_FIRST_SLOT_INDEX, PLAYER_FIRST_SLOT_INDEX + PLAYER_SLOT_COUNT, false)) {
                // No room in player's inventory, stop trying to move stuff.
                return ItemStack.EMPTY;  //EMPTY_ITEM;
            }
        } else {
            // Invalid. Error!
            LogUtility.error("Warning: Invalid slot %d selected.", sourceSlotIndex);
        }

        // Succeeded in moving something. If the stack is now empty...
        if (sourceStack.isEmpty() || sourceStack.getCount() == 0) {  //getStackSize()
            // Set it to empty.
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            // Else, flag the stack as changed so the GUI can update.
            sourceSlot.onSlotChanged();
        }

        return ItemStack.EMPTY;
    }
}