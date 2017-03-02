package com.mastereric.meta.init;

import com.mastereric.meta.Reference;

/**
 * Created by TORC on 3/1/2017.
 */
public class ModAchivements {
    public static Achievement createModMaker;
    public static Achievement createMod;
    public static Achievement createMETA;
    public static Achievement useMod;
    public static Achievement useModDumb;

    public static void initializeAchievements() {
        createModMaker = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 0, 0, ModBlocks.itemBlockModMaker);
        createMod = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 1, 0, ModBlocks.itemBlockModMaker, createModMaker);
        createMETA = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 2, 0, ModBlocks.itemBlockModMaker, createMod);
        useMod = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 2, 1, ModBlocks.itemBlockModMaker, createMETA);
        useModDumb = createAchievement(Reference.NAME_ACHIEVEMENT_CREATE_MOD_MAKER, 1, 1, ModBlocks.itemBlockModMaker, true);

        AchievementPage.registerAchievementPage(new AchievementPage(Reference.NAME_ACHIEVEMENT_PAGE,
                new Achievement[] {createModMaker, createMod, createMETA, useMod, useModDumb}));
    }

    public static Achievement createAchievement(String name, int xPos, int yPos, Item icon) {
        createAchievement(name, xPos, yPos, icon, (Achievement)null, false);
    }

    public static Achievement createAchievement(String name, int xPos, int yPos, Item icon, boolean special) {
        createAchievement(name, xPos, yPos, icon, (Achievement)null, special);
    }

    public static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent) {
        createAchievement(name, xPos, yPos, icon, parent, false);
    }

    public static Achievement createAchievement(String name, int xPos, int yPos, Item icon, Achievement parent, boolean special) {
        Achievement achievement = new Achievement(name, xPos, yPos, icon, parent);
        if (special)
            achievement.markSpecial();
        achievement.registerStat();
        return achievement;
    }

    public static void grantAchivement(EntityPlayer player, Achievement achievement) {
        player.addStat(achievement, 1);
    }
}
