package com.mastereric.meta.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by eric on 2/27/2017.
 */
public class METAItemStackHandler extends ItemStackHandler {
    public METAItemStackHandler(int slotCount) {
        super(slotCount);
    }

    @Override
    public int getSlots() {
        // Number of slots.
        return 1;
    }

    @Override
    public int getSlotLimit(int slot) {
        // Slot stack size limit.
        return 1;
    }
}
