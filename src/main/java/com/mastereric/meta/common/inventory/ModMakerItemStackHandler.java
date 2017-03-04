package com.mastereric.meta.common.inventory;

import com.mastereric.meta.util.LogUtility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by eric on 2/27/2017.
 */
public class ModMakerItemStackHandler extends ItemStackHandler {
    public ModMakerItemStackHandler(int slotCount) {
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
