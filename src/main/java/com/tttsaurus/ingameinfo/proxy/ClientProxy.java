package com.tttsaurus.ingameinfo.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {
        super.preInit(event, logger);
    }

    @Override
    public void init(FMLInitializationEvent event, Logger logger)
    {
        super.init(event, logger);
    }
}
