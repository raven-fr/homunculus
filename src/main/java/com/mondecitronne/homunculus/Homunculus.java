package com.mondecitronne.homunculus;

import com.mondecitronne.homunculus.proxy.Proxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Homunculus.MODID, name = Homunculus.NAME, version = Homunculus.VERSION)
public class Homunculus {
	public static final String MODID = "homunculus";
	public static final String NAME = "Homunculus";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide = "com.mondecitronne.homunculus.proxy.ClientProxy", serverSide = "com.mondecitronne.homunculus.proxy.Proxy")
	public static Proxy proxy;

	@Mod.Instance
	public static Homunculus instance;

	static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
