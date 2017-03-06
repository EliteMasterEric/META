package com.mastereric.meta.proxy;

import com.mastereric.meta.META;
import com.mastereric.meta.common.gui.GuiHandler;
import com.mastereric.meta.common.stats.AchievementHandler;
import com.mastereric.meta.init.*;
import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		LogUtility.info("Performing common initialization.");
		// Add items.
		ModItems.initializeItems();
		// Add Blocks
		ModBlocks.initializeBlocks();
		// Add recipes.
		ModRecipes.initializeCraftingRecipes();
		// Add achievements.
		ModAchivements.initializeAchievements();

		ModConfig.config = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "meta.cfg"));
		ModConfig.parseConfig();
	}

	public void init(FMLInitializationEvent event) {
	    // Register GUI handler.
		NetworkRegistry.INSTANCE.registerGuiHandler(META.instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new AchievementHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {
		ModConfig.saveConfig();
		// Initialize compatibility!
		ModCompat.initializeCompat();
	}
}
