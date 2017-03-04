package com.mastereric.meta.common.stats;

import com.mastereric.meta.init.ModAchivements;
import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AchievementHandler {

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent e) {
        if (e.crafting.getItem() == ModBlocks.itemBlockModMaker) {
            ModAchivements.grantAchivement(e.player, ModAchivements.createModMaker);
        } else if (e.crafting.getItem() == ModBlocks.itemBlockMETA) {
            ModAchivements.grantAchivement(e.player, ModAchivements.createMETA);
        }
    }

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent e) {
        if(e.getEntity() instanceof EntityPlayer) {
            if(e.getItem().getEntityItem().getItem() == ModItems.itemModDumb) {
                ModAchivements.grantAchivement(e.getEntityPlayer(), ModAchivements.createModDumb);
            }
        }
    }
}
