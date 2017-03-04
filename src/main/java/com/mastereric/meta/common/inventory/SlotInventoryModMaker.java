package com.mastereric.meta.common.inventory;

import com.mastereric.meta.init.ModAchivements;
import com.mastereric.meta.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotInventoryModMaker extends SlotItemHandler {
    public SlotInventoryModMaker(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack item) {
        if (item.getItem() == ModItems.itemMod) {
            ModAchivements.grantAchivement(player, ModAchivements.createMod);
        } else if (item.getItem() == ModItems.itemModDumb) {
            ModAchivements.grantAchivement(player, ModAchivements.createModDumb);
        }
        return super.onTake(player, item);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

}
