package com.mastereric.meta.init;

import com.mastereric.meta.Reference;
import com.mastereric.meta.util.LangUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public final class ModAchivements {
    public static Achievement createModMaker;
    public static Achievement createMod;
    public static Achievement createMETA;
    public static Achievement createModDumb;
    public static Achievement useMod;

    public static void initializeAchievements() {
        createModMaker = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 0, 0, ModBlocks.itemBlockModMaker);
        createMod = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD, 2, 0, ModItems.itemMod, createModMaker);
        createMETA = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_META, 4, 0, ModBlocks.itemBlockMETA, createMod);
        createModDumb = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_DUMB, 2, 2, ModItems.itemModDumb, true);
        useMod = createAchievement(Reference.NAME_ACHIEVEMENT_USE_MOD, 4, 2, ModItems.itemMod, createMETA);

        AchievementPage.registerAchievementPage(new AchievementPage(LangUtility.getTranslation(Reference.NAME_ACHIEVEMENT_PAGE),
                createModMaker, createMod, createMETA, useMod, createModDumb));
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon) {
        return createAchievement(name, xPos, yPos, icon, null, false);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, boolean special) {
        return createAchievement(name, xPos, yPos, icon, null, special);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent) {
        return createAchievement(name, xPos, yPos, icon, parent, false);
    }

    private static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent, boolean special) {
        Achievement achievement = new Achievement("achievement." + name, name, xPos, yPos, icon, parent);
        if (special)
            achievement.setSpecial();
        achievement.registerStat();
        return achievement;
    }

    public static void grantAchivement(EntityPlayer player, Achievement achievement) {
        player.addStat(achievement, 1);
    }
}
