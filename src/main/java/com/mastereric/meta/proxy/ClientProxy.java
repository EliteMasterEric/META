package com.mastereric.meta.proxy;

import com.mastereric.meta.client.particles.ParticleClientEventHandler;
import com.mastereric.meta.init.ModBlocks;
import com.mastereric.meta.init.ModItems;
import com.mastereric.meta.util.LogUtility;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Suppress warnings about calling @SideOnly(Side.CLIENT) methods in the ClientProxy,
// this class is only used on the client.
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
	    super.preInit(event);
		LogUtility.info("Performing client initialization.");
		ModItems.initializeItemModels();
		ModBlocks.initializeBlockModels();
		MinecraftForge.EVENT_BUS.register(new ParticleClientEventHandler());
	}
}
