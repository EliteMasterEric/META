package com.mastereric.meta.init;

import com.mastereric.meta.META;
import com.mastereric.meta.Reference;
import com.mastereric.meta.common.items.ItemMod;

import com.mastereric.meta.common.items.ItemModDumb;
import com.mastereric.meta.util.LogUtility;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModItems {
	public static Item itemMod;
	public static Item itemModDumb;

    public static void initializeItems() {
		LogUtility.info("Initializing items.");
		itemMod = new ItemMod();
		registerItem(itemMod, Reference.NAME_ITEM_MOD);
		itemModDumb = new ItemModDumb();
		registerItem(itemModDumb, Reference.NAME_ITEM_MOD_DUMB, true);
	}

	@SideOnly(Side.CLIENT)
	public static void initializeItemModels() {
		LogUtility.info("Initializing item models.");
    	// Run this on the ClientProxy after running initializeItems.
    	registerItemModel(itemMod);
        registerItemModel(itemModDumb);
    }

    private static void registerItem(Item item, String registryName) {
        registerItem(item, registryName, true);
    }

    private static void registerItem(Item item, String registryName, boolean creativeTab) {
    	// Set the registry name.
    	item.setRegistryName(Reference.MOD_ID, registryName);
		item.setUnlocalizedName(Reference.MOD_ID + "." + registryName);
    	// Add to the game registry.
    	GameRegistry.register(item);

    	if(creativeTab)
		    item.setCreativeTab(META.creativeTab);

    	LogUtility.info("Registered item ~ %s", item.getRegistryName());
    }

	@SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item) {
    	// Function overloads make everything simpler.
    	registerItemModel(item, 0);
    }

    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item, int metadata) {
    	// Register the item model.
    	ModelLoader.setCustomModelResourceLocation(item, metadata,
    			new ModelResourceLocation(item.getRegistryName(), "inventory"));

		LogUtility.info("Registered item model ~ %s", item.getRegistryName());
    }
}