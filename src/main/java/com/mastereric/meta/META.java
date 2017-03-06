package com.mastereric.meta;

import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.proxy.CommonProxy;
import com.mastereric.meta.util.LogUtility;
import mcjty.lib.compat.CompatCreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION,
        updateJSON = Reference.UPDATE_CHECK_JSON_URL)
public class META {

	@Mod.Instance(Reference.MOD_ID)
	public static META instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static CommonProxy proxy;

	//TODO test on 1.11.2

	//TODO add CompatLayer
	//TODO test on 1.9.4
	//TODO test on 1.10.2

	//TODO add JEI "Uses" documentation like Iron Backpacks

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		FMLLog.getLogger().info("Initializing mod " + Reference.MOD_ID);

		proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void onMissingMapping(FMLMissingMappingsEvent event) {
		LogUtility.info("Repairing missing mappings...");
		for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
			String resourcePath = mapping.resourceLocation.getResourcePath().toLowerCase();
			if (mapping.type == GameRegistry.Type.BLOCK) {
				if ("meta".equals(resourcePath) || "meta_inactive".equals(resourcePath) || "meta_active".equals(resourcePath)) {
					mapping.remap(ModBlocks.blockMETA);
				} else if ("mod_maker".equals(resourcePath)) {
					mapping.remap(ModBlocks.blockModMaker);
				}
			} else if (mapping.type == GameRegistry.Type.ITEM) {
				if ("mod".equals(resourcePath)) {
					mapping.remap(ModItems.itemMod);
				} else if ("mod_dumb".equals(resourcePath)) {
					mapping.remap(ModItems.itemModDumb);
				}
			}
		}

	}

	public static CompatCreativeTabs creativeTab = new CompatCreativeTabs(Reference.MOD_ID) {
		@Override
		protected Item getItem() {
			return ModItems.itemMod;
		}
	};
    
}
