package com.mastereric.meta.client.gui;

import com.mastereric.meta.Reference;
import com.mastereric.meta.init.ModConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiModConfig extends GuiConfig {
    public GuiModConfig(GuiScreen parent) {
        super(parent, getConfigElements(), Reference.MOD_ID, true, false, GuiConfig.getAbridgedConfigPath(ModConfig.config.toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(categoryElement(ModConfig.CATEGORY_GENERAL, ModConfig.CATEGORY_GENERAL_NAME, ModConfig.CATEGORY_GENERAL_TOOLTIP));
        list.add(categoryElement(ModConfig.CATEGORY_MOD_MAKER, ModConfig.CATEGORY_MOD_MAKER_NAME, ModConfig.CATEGORY_MOD_MAKER_TOOLTIP));
        list.add(categoryElement(ModConfig.CATEGORY_META, ModConfig.CATEGORY_META_NAME, ModConfig.CATEGORY_META_TOOLTIP));

        return list;
    }

    /** Creates a button linking to another screen where all options of the category are available */
    private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
        return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
                new ConfigElement(ModConfig.config.getCategory(category)).getChildElements());
    }
}