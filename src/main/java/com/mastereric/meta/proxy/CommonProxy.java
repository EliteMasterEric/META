package com.mastereric.meta.proxy;

import com.mastereric.meta.META;
import com.mastereric.meta.common.gui.GuiHandler;
import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.init.ModRecipes;
import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		LogUtility.info("Performing common initialization.");
		// Add items.
		ModItems.initializeItems();
		// Add Blocks
		ModBlocks.initializeBlocks();
		// Add recipes.
		ModRecipes.initializeCraftingRecipes();
	}

	public void init(FMLInitializationEvent event) {
	    // Register GUI handler.
		NetworkRegistry.INSTANCE.registerGuiHandler(META.instance, new GuiHandler());
	}
}
