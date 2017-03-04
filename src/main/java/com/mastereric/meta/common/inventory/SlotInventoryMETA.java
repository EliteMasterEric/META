package com.mastereric.meta.common.inventory;

import com.mastereric.meta.init.ModAchivements;
import com.mastereric.meta.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by eric on 3/2/2017.
 */
public class SlotInventoryMETA extends SlotItemHandler {
    private final EntityPlayer player;

    public SlotInventoryMETA(IItemHandler itemHandler, int index, int xPosition, int yPosition, EntityPlayer player) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public void putStack(ItemStack item) {
        if (item.getItem() == ModItems.itemMod) {
            ModAchivements.grantAchivement(player, ModAchivements.useMod);
        }
        super.putStack(item);
    }
}
