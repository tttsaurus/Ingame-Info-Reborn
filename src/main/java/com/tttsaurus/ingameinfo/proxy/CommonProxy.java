package com.tttsaurus.ingameinfo.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.config.ForgeConfigWriter;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.api.event.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPost;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPre;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyCommandHandler;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegisterEventHandler;
import com.tttsaurus.ingameinfo.plugin.crt.impl.CrtEventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.Map;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {
        // config setup
        File file = event.getSuggestedConfigurationFile();
        IgiConfig.CONFIG = new Configuration(file);
        IgiConfig.loadConfig();
        IgiConfig.CONFIG_WRITER = new ForgeConfigWriter(file);

        IgiGuiLifeCycle.setEnableFbo(IgiConfig.ENABLE_FRAMEBUFFER);
        IgiGuiLifeCycle.setMaxFps_FixedUpdate(IgiConfig.FIXED_UPDATE_LIMIT);
        IgiGuiLifeCycle.setMaxFps_RenderUpdate(IgiConfig.RENDER_UPDATE_LIMIT);

        SpotifyOAuthUtils.CLIENT_ID = IgiConfig.SPOTIFY_CLIENT_ID;
        SpotifyOAuthUtils.CLIENT_SECRET = IgiConfig.SPOTIFY_CLIENT_SECRET;
    }

    public void init(FMLInitializationEvent event, Logger logger)
    {
        logger.info("In-Game Info Reborn starts initializing.");

        InternalMethods.instance = new InternalMethods();

        // core
        MinecraftForge.EVENT_BUS.register(IgiGuiLifeCycle.class);
        MinecraftForge.EVENT_BUS.register(MvvmRegisterEventHandler.class);

        // crt support
        if (Loader.isModLoaded("crafttweaker"))
            MinecraftForge.EVENT_BUS.register(CrtEventManager.Handler.class);

        // app communication
        MinecraftForge.EVENT_BUS.register(SpotifyCommandHandler.class);

        String myPackage = "com.tttsaurus.ingameinfo";
        ElementRegistry.register();

        logger.info("");
        logger.info("Registered usable elements: ");
        for (Class<? extends Element> clazz: ElementRegistry.getConstructableElements())
            logger.info("  - " + (TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName()));
        logger.info("");

        ImmutableList<Class<? extends Element>> elementClasses = ElementRegistry.getRegisteredElements();
        ImmutableMap<String, Map<String, IStylePropertySetter>> setters = ElementRegistry.getStylePropertySetters();
        ImmutableMap<IStylePropertySetter, IDeserializer<?>> deserializers = ElementRegistry.getStylePropertyDeserializers();
        ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPre> setterCallbacksPre = ElementRegistry.getStylePropertySetterCallbacksPre();
        ImmutableMap<IStylePropertySetter, IStylePropertyCallbackPost> setterCallbacksPost = ElementRegistry.getStylePropertySetterCallbacksPost();
        ImmutableMap<IStylePropertySetter, Class<?>> classes = ElementRegistry.getStylePropertyClasses();

        for (Class<? extends Element> clazz: elementClasses)
        {
            String parentMsg = "";
            if (!clazz.getSuperclass().equals(Element.class)) parentMsg = " extends " + (TypeUtils.isFromParentPackage(clazz.getSuperclass(), myPackage) ? clazz.getSuperclass().getSimpleName() : clazz.getSuperclass().getName());
            logger.info("Element type: " + (TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName()) + parentMsg);
            Map<String, IStylePropertySetter> map = setters.get(clazz.getName());
            if (!map.isEmpty())
            {
                logger.info("- With style properties:");
                for (Map.Entry<String, IStylePropertySetter> entry: map.entrySet())
                {
                    IStylePropertySetter primaryKey = entry.getValue();

                    String suffix = "";
                    if (deserializers.containsKey(primaryKey))
                        suffix = " (with deserializer: " + (TypeUtils.isFromParentPackage(deserializers.get(primaryKey).getClass(), myPackage) ? deserializers.get(primaryKey).getClass().getSimpleName() : deserializers.get(primaryKey).getClass().getName()) + ")";
                    logger.info("  - [" + classes.get(primaryKey).getSimpleName() + "] " + entry.getKey() + suffix);
                    if (setterCallbacksPre.containsKey(primaryKey))
                        logger.info("    - Setter callback pre: " + setterCallbacksPre.get(primaryKey).name());
                    if (setterCallbacksPost.containsKey(primaryKey))
                        logger.info("    - Setter callback post: " + setterCallbacksPost.get(primaryKey).name());
                }
            }
            logger.info("");
        }

        logger.info("Starts registering mvvm.");
        MinecraftForge.EVENT_BUS.post(new MvvmRegisterEvent());
    }
}
