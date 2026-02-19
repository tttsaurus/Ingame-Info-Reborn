package com.tttsaurus.ingameinfo.proxy;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.commonutils.FileUtils;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.config.ForgeConfigWriter;
import com.tttsaurus.ingameinfo.config.IgiCommonConfig;
import com.tttsaurus.ingameinfo.config.IgiDefaultLifecycleProviderConfig;
import com.tttsaurus.ingameinfo.config.IgiSpotifyIntegrationConfig;
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
        //<editor-fold desc="reflection">
//        InternalMethods.instance = new InternalMethods();
//        logger.info("Reflection setup of IGI framework finished.");
        //</editor-fold>

        //<editor-fold desc="config setup">
        IgiCommonConfig.CONFIG = new Configuration(FileUtils.makeFile("common.cfg"));
        IgiCommonConfig.loadConfig();
        // crash game immediately if the lifecycle provider was null
        try
        {
            IgiCommonConfig.GUI_LIFECYCLE_PROVIDER.getClass();
        }
        catch (Exception e)
        {
            throw new RuntimeException("GUI Lifecycle Provider is invalid. See config/ingameinfo/common.cfg", e);
        }

        IgiDefaultLifecycleProviderConfig.CONFIG = new Configuration(FileUtils.makeFile("default_lifecycle_provider.cfg"));
        IgiDefaultLifecycleProviderConfig.loadConfig();

        File spotifyConfig = FileUtils.makeFile("spotify_integration.cfg");
        IgiSpotifyIntegrationConfig.CONFIG = new Configuration(spotifyConfig);
        IgiSpotifyIntegrationConfig.CONFIG_WRITER = new ForgeConfigWriter(spotifyConfig);
        IgiSpotifyIntegrationConfig.loadConfig();

        logger.info("In-Game Info Reborn config loaded.");
        //</editor-fold>
    }

    public void init(FMLInitializationEvent event, Logger logger)
    {
        logger.info("In-Game Info Reborn starts initializing.");

        //<editor-fold desc="network">
        IgiNetwork.init();
        logger.info("Network setup finished.");
        //</editor-fold>

        //<editor-fold desc="crafttweaker support">
        if (InGameInfoReborn.isCraftTweakerLoaded().orElseThrow())
            MinecraftForge.EVENT_BUS.register(CrtEventManager.Handler.class);
        //</editor-fold>
    }
}
