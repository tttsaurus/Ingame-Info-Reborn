package com.tttsaurus.ingameinfo.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.ISetStyleProperty;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.InGameCommandHandler;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {

    }

    public void init(FMLInitializationEvent event, Logger logger)
    {
        logger.info("In-Game Info Reborn starts initializing.");

        MinecraftForge.EVENT_BUS.register(IgiGuiLifeCycle.class);
        MinecraftForge.EVENT_BUS.register(InGameCommandHandler.class);

        ElementRegistry.register();
        ImmutableList<Class<? extends Element>> elementClasses = ElementRegistry.getRegisteredElements();
        ImmutableMap<String, Map<String, ISetStyleProperty>> setters = ElementRegistry.getStylePropertySetters();
        ImmutableMap<ISetStyleProperty, IDeserializer<?>> deserializers = ElementRegistry.getStylePropertyDeserializers();
        for (Class<? extends Element> clazz: elementClasses)
        {
            logger.info("Registered element type: " + clazz.getName());
            Map<String, ISetStyleProperty> map = setters.get(clazz.getName());
            if (!map.isEmpty())
            {
                logger.info("- With style properties:");
                for (Map.Entry<String, ISetStyleProperty> entry: map.entrySet())
                {
                    String suffix = "";
                    if (deserializers.containsKey(entry.getValue()))
                        suffix = " (with deserializer: " + deserializers.get(entry.getValue()).getClass().getName() + ")";
                    logger.info("  - " + entry.getKey() + suffix);
                }
            }
        }
    }
}
