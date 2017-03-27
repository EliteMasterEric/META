package com.mastereric.meta.init;


import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ModConfig {
    public static Configuration config;

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_GENERAL_NAME = "General";
    public static final String CATEGORY_GENERAL_DESC = "General configuration";
    public static final String CATEGORY_GENERAL_TOOLTIP = "config.meta.general";

    public static final String CATEGORY_MOD_MAKER = "mod_maker";
    public static final String CATEGORY_MOD_MAKER_NAME = "Mod Maker";
    public static final String CATEGORY_MOD_MAKER_DESC = "Configuration for the Mod Maker.";
    public static final String CATEGORY_MOD_MAKER_TOOLTIP = "config.meta.mod_maker";

    public static final String CATEGORY_META = "meta";
    public static final String CATEGORY_META_NAME = "M.E.T.A.";
    public static final String CATEGORY_META_DESC = "Configuration for the M.E.T.A.";
    public static final String CATEGORY_META_TOOLTIP = "config.meta.meta";

    public static boolean MOD_IDEA_190 = false;
    private static String MOD_IDEA_190_NAME = "mod_idea_190";
    private static String MOD_IDEA_190_DESC = "The mods created by the Mod Maker will no longer be usable by the M.E.T.A.";

    public static int META_MAX_ENERGY_STORED = 80000; // A little over half of one mod's worth of energy.
    private static String META_MAX_ENERGY_STORED_NAME = "meta_max_energy";
    private static String META_MAX_ENERGY_STORED_DESC = "The maximum amount of energy in the M.E.T.A.'s internal buffer.";

    public static int META_FE_PER_MOD = 150000; // A little under 1 coal block of power.
    private static String META_FE_PER_MOD_NAME = "meta_energy_per_mod";
    private static String META_FE_PER_MOD_DESC = "The amount of energy released by consuming one Mod in the M.E.T.A. Time is calculated with this and energy_per_tick.";

    public static int META_FE_PER_TICK = 60; // 50% higher than an Extra Utilities furnace generator.
    private static String META_FE_PER_TICK_NAME = "meta_energy_per_tick";
    private static String META_FE_PER_TICK_DESC = "The amount of energy released per tick by consuming Mods in the M.E.T.A. Time is calculated with this and energy_per_mod.";

    public static int MOD_MAKER_WAIT_TIME = 1600; // 80 seconds
    private static String MOD_MAKER_WAIT_TIME_NAME = "mod_maker_wait_time";
    private static String MOD_MAKER_WAIT_TIME_DESC = "The amount of time it takes to create one mod, with a single bookshelf.";

    public static int META_OUTPUT = 100; // 100 fe/t
    private static String META_OUTPUT_NAME = "meta_output";
    private static String META_OUTPUT_DESC = "The FE per tick the META is capable of pushing from its storage.";

    public static void parseConfig() {
        if (config != null) {
            try {
                config.load();
                if (config != null) {
                    config.addCustomCategoryComment(CATEGORY_GENERAL, CATEGORY_GENERAL_DESC);
                    config.addCustomCategoryComment(CATEGORY_MOD_MAKER, CATEGORY_MOD_MAKER_DESC);
                    config.addCustomCategoryComment(CATEGORY_META, CATEGORY_META_DESC);

                    MOD_IDEA_190 = config.getBoolean(MOD_IDEA_190_NAME, CATEGORY_GENERAL, MOD_IDEA_190, MOD_IDEA_190_DESC);

                    META_FE_PER_MOD = config.getInt(META_FE_PER_MOD_NAME, CATEGORY_META, META_FE_PER_MOD, 0, Integer.MAX_VALUE, META_FE_PER_MOD_DESC);
                    META_FE_PER_TICK = config.getInt(META_FE_PER_TICK_NAME, CATEGORY_META, META_FE_PER_TICK, 0, Integer.MAX_VALUE, META_FE_PER_TICK_DESC);
                    META_MAX_ENERGY_STORED = config.getInt(META_MAX_ENERGY_STORED_NAME, CATEGORY_META, META_MAX_ENERGY_STORED, 0, Integer.MAX_VALUE, META_MAX_ENERGY_STORED_DESC);
                    META_OUTPUT = config.getInt(META_OUTPUT_NAME, CATEGORY_META, META_OUTPUT, 0, Integer.MAX_VALUE, META_OUTPUT_DESC);

                    MOD_MAKER_WAIT_TIME = config.getInt(MOD_MAKER_WAIT_TIME_NAME, CATEGORY_MOD_MAKER, MOD_MAKER_WAIT_TIME, 0, Integer.MAX_VALUE, MOD_MAKER_WAIT_TIME_DESC);

                    config.save();
                }
            } catch (Exception e) {
                LogUtility.error("Error loading config file! %s", e);
            }
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        parseConfig();
    }
}
