package com.mastereric.meta.proxy;

import com.mastereric.meta.META;
import com.mastereric.meta.common.gui.GuiHandler;
import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModConfig;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.init.ModRecipes;
import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
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

		ModConfig.config = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "meta.cfg"));
		ModConfig.parseConfig();
	}

	public void init(FMLInitializationEvent event) {
	    // Register GUI handler.
		NetworkRegistry.INSTANCE.registerGuiHandler(META.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {
		ModConfig.saveConfig();
	}
}
