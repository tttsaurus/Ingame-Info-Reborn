package com.tttsaurus.ingameinfo.proxy;

import com.tttsaurus.ingameinfo.common.impl.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.InGameCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {

    }

    public void init(FMLInitializationEvent event, Logger logger)
    {
        logger.info("In-Game Info starts initializing.");

        MinecraftForge.EVENT_BUS.register(IgiGuiLifeCycle.class);
        MinecraftForge.EVENT_BUS.register(InGameCommandHandler.class);
    }
}
