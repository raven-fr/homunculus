package com.mondecitronne.homunculus.proxy;

import com.mondecitronne.homunculus.GameProfileFetcher;
import com.mondecitronne.homunculus.Homunculus;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends Proxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		Homunculus.instance.putDataDirIn(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory());
		GameProfileFetcher.init(Homunculus.instance.getDataDir());
	}
}
