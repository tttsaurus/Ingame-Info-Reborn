package com.tttsaurus.ingameinfo.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.api.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.api.event.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPost;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertyCallbackPre;
import com.tttsaurus.ingameinfo.common.api.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.api.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.api.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.api.render.RenderHints;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.api.shutdown.ShutdownHooks;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyCommandHandler;
import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import com.tttsaurus.ingameinfo.common.impl.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegisterEventHandler;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {
        super.preInit(event, logger);

        int majorGlVersion = RenderHints.getMajorGlVersion();
        int minorGlVersion = RenderHints.getMinorGlVersion();

        logger.info("Raw OpenGl version: " + RenderHints.getRawGlVersion());
        if (majorGlVersion == -1 || minorGlVersion == -1)
            logger.info("Error: Can't parse OpenGl version");
        else
            logger.info(String.format("OpenGl version: %d.%d", majorGlVersion, minorGlVersion));

        // at least gl 30
        boolean enableFbo = IgiConfig.ENABLE_FRAMEBUFFER && OpenGlHelper.framebufferSupported && majorGlVersion >= 3;
        // at least gl 33
        boolean enablePostProcessing = IgiConfig.ENABLE_POST_PROCESSING_SHADER && ((majorGlVersion == 3 && minorGlVersion >= 3) || majorGlVersion > 3);
        // at least gl 40
        boolean enableMsfbo = IgiConfig.ENABLE_MSFRAMEBUFFER && majorGlVersion >= 4;

        // framebuffer is the prerequisite
        enablePostProcessing = enableFbo && enablePostProcessing;
        enableMsfbo = enableFbo && enableMsfbo;

        logger.info("[Render Feature] Framebuffer is " + (enableFbo ? "ON" : "OFF") + " (requires GL30)");
        logger.info("[Render Feature] Post-Processing on framebuffer is " + (enablePostProcessing ? "ON" : "OFF") + " (requires GL33 and framebuffer)");
        logger.info("[Render Feature] Multisampling on framebuffer is " + (enableMsfbo ? "ON" : "OFF") + " (requires GL40 and framebuffer)");

        IgiGuiLifeCycle.setEnableFbo(enableFbo);
        IgiGuiLifeCycle.setEnableShader(enablePostProcessing);
        IgiGuiLifeCycle.setEnableMultisampleOnFbo(enableMsfbo);
        RenderHints.fboSampleNum(IgiConfig.FRAMEBUFFER_SAMPLE_NUM);

        IgiGuiLifeCycle.setMaxFps_FixedUpdate(IgiConfig.FIXED_UPDATE_LIMIT);
        IgiGuiLifeCycle.setMaxFps_RefreshFbo(IgiConfig.RENDER_UPDATE_LIMIT);

        if (IgiConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            SpotifyOAuthUtils.CLIENT_ID = IgiConfig.SPOTIFY_CLIENT_ID;
            SpotifyOAuthUtils.CLIENT_SECRET = IgiConfig.SPOTIFY_CLIENT_SECRET;
        }
    }

    @Override
    public void init(FMLInitializationEvent event, Logger logger)
    {
        super.init(event, logger);

        // shutdown hook
        ShutdownHooks.hooks.add(() ->
        {
            logger.info("Starts disposing OpenGL resources");
            GlResourceManager.disposeAll(logger);
            logger.info("OpenGL resources disposed");
        });

        // core
        MinecraftForge.EVENT_BUS.register(IgiGuiLifeCycle.class);
        MinecraftForge.EVENT_BUS.register(MvvmRegisterEventHandler.class);

        // app communication
        if (IgiConfig.ENABLE_SPOTIFY_INTEGRATION)
            MinecraftForge.EVENT_BUS.register(SpotifyCommandHandler.class);

        String myPackage = "com.tttsaurus.ingameinfo";
        ElementRegistry.register();

        logger.info("");
        logger.info("Registered serviceable elements: ");
        List<Class<? extends Element>> constructableElements = ElementRegistry.getConstructableElements();
        for (Class<? extends Element> clazz: constructableElements)
            logger.info("  - " + (TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName()));

        logger.info("");
        logger.info("Notice:");
        logger.info("1. Elements marked with * below are unserviceable in ixml.");
        logger.info("2. You can access style properties from parent elements.");
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
            if (!clazz.equals(Element.class))
                parentMsg =
                    " extends " +
                    (TypeUtils.isFromParentPackage(clazz.getSuperclass(), myPackage) ? clazz.getSuperclass().getSimpleName() : clazz.getSuperclass().getName()) +
                    (constructableElements.contains(clazz.getSuperclass()) ? "" : "*");
            logger.info("Element type: " +
                    (TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName()) +
                    (constructableElements.contains(clazz) ? "" : "*") +
                    parentMsg);

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
