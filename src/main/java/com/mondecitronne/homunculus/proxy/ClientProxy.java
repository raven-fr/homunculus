package com.mondecitronne.homunculus.proxy;

import com.mondecitronne.homunculus.EntityHomunculus;
import com.mondecitronne.homunculus.client.RenderHomunculus;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends Proxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		//PlayerSkin.initProfileCache();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityHomunculus.class, RenderHomunculus.FACTORY);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
	}
}

