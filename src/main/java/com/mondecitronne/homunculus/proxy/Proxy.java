package com.mondecitronne.homunculus.proxy;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import com.mondecitronne.homunculus.EntityHomunculus;
import com.mondecitronne.homunculus.Homunculus;

@Mod.EventBusSubscriber
public class Proxy {
	public void preInit(FMLPreInitializationEvent e) {
		EntityRegistry.registerModEntity(new ResourceLocation(Homunculus.MODID, "homunculus"), EntityHomunculus.class, "homunculus", 0, Homunculus.instance, 64, 3, true);
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
	}
}