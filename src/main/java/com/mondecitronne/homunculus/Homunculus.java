package com.mondecitronne.homunculus;

import java.io.File;

import org.apache.logging.log4j.Logger;
import com.mondecitronne.homunculus.proxy.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Homunculus.MODID, name = Homunculus.NAME, version = Homunculus.VERSION)
public class Homunculus {
	public static final String MODID = "homunculus";
	public static final String NAME = "Homunculus";
	public static final String VERSION = "1.0";

	@SidedProxy(clientSide = "com.mondecitronne.homunculus.proxy.ClientProxy", serverSide = "com.mondecitronne.homunculus.proxy.ServerProxy")
	public static Proxy proxy;

	@Mod.Instance
	public static Homunculus instance;

	public Logger logger;
	File dataDir;

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

	public File getDataDir() {
		return dataDir;
	}

	public void putDataDirIn(File dir) {
		assert (dataDir == null);
		dataDir = new File(dir, MODID + "_data");
		dataDir.mkdir();
	}
}
