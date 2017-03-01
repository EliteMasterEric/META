package com.mastereric.meta.init;


import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {
    public static Configuration config;

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_GENERAL_DESC = "General configuration";

    public static boolean MOD_190 = false;

    public static void parseConfig() {
        if (config != null) {
            try {
                config.load();
            } catch (Exception e) {
                LogUtility.error("Error loading config file! %s", e);
            } finally {
                saveConfig();
            }
        }
    }

    public static void saveConfig() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void parseConfigGeneral() {
        config.addCustomCategoryComment(CATEGORY_GENERAL, CATEGORY_GENERAL_DESC);

    }
}
