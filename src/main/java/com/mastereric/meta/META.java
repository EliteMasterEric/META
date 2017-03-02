package com.mastereric.meta;

import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.init.ModRecipes;
import com.mastereric.meta.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import scala.tools.nsc.backend.icode.TypeKinds;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION)
public class META {

	@Mod.Instance(Reference.MOD_ID)
	public static META instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static CommonProxy proxy;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		FMLLog.getLogger().info("Initializing mod " + Reference.MOD_ID);

		//TODO add CompatLayer
		//TODO test on 1.11.2
		//TODO test on 1.10.2
		//TODO test on 1.9.4
		//TODO bugfix achievements
		//TODO add CommonCapabilites wrench to META
		//TODO add CommonCapabilites working to META
		//TODO add CommonCapabilites working to Mod Maker

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

	public static CreativeTabs creativeTab = new CreativeTabs(Reference.MOD_ID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.itemMod);
		}
	};
    
}
