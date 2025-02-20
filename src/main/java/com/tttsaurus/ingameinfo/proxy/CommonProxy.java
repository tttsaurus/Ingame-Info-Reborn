package com.tttsaurus.ingameinfo.proxy;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.config.ForgeConfigWriter;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtEventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import java.io.File;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {
        // config setup
        File file = event.getSuggestedConfigurationFile();
        IgiConfig.CONFIG = new Configuration(file);
        IgiConfig.loadConfig();
        IgiConfig.CONFIG_WRITER = new ForgeConfigWriter(file);
        logger.info("In-Game Info Reborn config loaded.");
    }

    public void init(FMLInitializationEvent event, Logger logger)
    {
        logger.info("In-Game Info Reborn starts initializing.");

        InternalMethods.instance = new InternalMethods();
        logger.info("Reflection setup finished.");

        IgiNetwork.init();
        logger.info("Network setup finished.");

        InGameInfoReborn.crafttweakerLoaded = Loader.isModLoaded("crafttweaker");
        InGameInfoReborn.bloodmagicLoaded = Loader.isModLoaded("bloodmagic");
        InGameInfoReborn.sereneseasonsLoaded = Loader.isModLoaded("sereneseasons");
        InGameInfoReborn.thaumcraftLoaded = Loader.isModLoaded("thaumcraft");
        InGameInfoReborn.rftoolsdimLoaded = Loader.isModLoaded("rftoolsdim");
        InGameInfoReborn.deepresonanceLoaded = Loader.isModLoaded("deepresonance");
        InGameInfoReborn.toughasnailsLoaded = Loader.isModLoaded("toughasnails");
        InGameInfoReborn.simpledifficultyLoaded = Loader.isModLoaded("simpledifficulty");

        // crt support
        if (InGameInfoReborn.crafttweakerLoaded)
            MinecraftForge.EVENT_BUS.register(CrtEventManager.Handler.class);
    }
}
