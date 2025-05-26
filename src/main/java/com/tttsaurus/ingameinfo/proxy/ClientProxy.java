package com.tttsaurus.ingameinfo.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.core.event.MvvmRegisterEvent;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiManager;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertyCallbackPost;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertyCallbackPre;
import com.tttsaurus.ingameinfo.common.core.gui.style.IStylePropertySetter;
import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.core.shutdown.ShutdownHooks;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyCommandHandler;
import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.core.gui.theme.registry.ThemeRegistry;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleProvider;
import com.tttsaurus.ingameinfo.common.impl.mvvm.command.RefreshVvmCommand;
import com.tttsaurus.ingameinfo.common.impl.mvvm.registry.MvvmRegisterEventHandler;
import com.tttsaurus.ingameinfo.config.IgiConfig;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.ClientCommandHandler;
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

        //<editor-fold desc="gl setup">
        int majorGlVersion = RenderHints.getMajorGlVersion();
        int minorGlVersion = RenderHints.getMinorGlVersion();

        logger.info("Raw OpenGL version: " + RenderHints.getRawGlVersion());
        logger.info(String.format("OpenGL version: %d.%d", majorGlVersion, minorGlVersion));

        boolean enableFbo = IgiConfig.ENABLE_FRAMEBUFFER && OpenGlHelper.framebufferSupported;
        // at least gl 33
        boolean enablePostProcessing = IgiConfig.ENABLE_POST_PROCESSING_SHADER && ((majorGlVersion == 3 && minorGlVersion >= 3) || majorGlVersion > 3);
        // at least gl 40
        boolean enableMsfbo = IgiConfig.ENABLE_MSFRAMEBUFFER && majorGlVersion >= 4;

        // framebuffer is the prerequisite
        enablePostProcessing = enableFbo && enablePostProcessing;
        enableMsfbo = enableFbo && enableMsfbo;

        logger.info("[IGI Render Feature] Framebuffer is " + (enableFbo ? "ON" : "OFF"));
        logger.info("[IGI Render Feature] Post-Processing on framebuffer is " + (enablePostProcessing ? "ON" : "OFF") + " (requires GL33 and framebuffer)");
        logger.info("[IGI Render Feature] Multisampling on framebuffer is " + (enableMsfbo ? "ON" : "OFF") + " (requires GL40 and framebuffer)");

        // init getters
        RenderHints.getModelViewMatrix();
        logger.info("The getters of ActiveRenderInfo private fields are ready.");
        RenderHints.getPartialTick();
        logger.info("The getter of private partial tick field is ready.");
        //</editor-fold>

        //<editor-fold desc="lifecycle provider setup">
        DefaultLifecycleProvider lifecycleProvider = new DefaultLifecycleProvider();

        lifecycleProvider.setEnableFbo(enableFbo);
        lifecycleProvider.setEnableShader(enablePostProcessing);
        lifecycleProvider.setEnableMultisampleOnFbo(enableMsfbo);
        RenderHints.fboSampleNum(IgiConfig.FRAMEBUFFER_SAMPLE_NUM);

        lifecycleProvider.setMaxFps_FixedUpdate(IgiConfig.FIXED_UPDATE_LIMIT);
        lifecycleProvider.setMaxFps_RenderUpdate(IgiConfig.RENDER_UPDATE_LIMIT);

        IgiGuiManager.setLifecycleProvider(lifecycleProvider);
        logger.info("GUI Lifecycle Provider is ready.");
        //</editor-fold>

        //<editor-fold desc="spotify setup">
        if (IgiConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            SpotifyOAuthUtils.CLIENT_ID = IgiConfig.SPOTIFY_CLIENT_ID;
            SpotifyOAuthUtils.CLIENT_SECRET = IgiConfig.SPOTIFY_CLIENT_SECRET;
        }
        //</editor-fold>
    }

    @Override
    public void init(FMLInitializationEvent event, Logger logger)
    {
        super.init(event, logger);

        //<editor-fold desc="shutdown hooks">
        ShutdownHooks.hooks.add(() ->
        {
            logger.info("Start disposing OpenGL resources");
            GlResourceManager.disposeAll(logger);
            logger.info("OpenGL resources disposed");
        });
        logger.info("Shutdown hooks setup finished.");
        //</editor-fold>

        //<editor-fold desc="core events">
        MinecraftForge.EVENT_BUS.register(IgiGuiManager.class);
        MinecraftForge.EVENT_BUS.register(MvvmRegisterEventHandler.class);
        MinecraftForge.EVENT_BUS.register(ThemeRegistry.class);
        logger.info("Core event listeners registered.");
        //</editor-fold>

        //<editor-fold desc="app communication">
        if (IgiConfig.ENABLE_SPOTIFY_INTEGRATION)
            MinecraftForge.EVENT_BUS.register(SpotifyCommandHandler.class);
        //</editor-fold>

        //<editor-fold desc="commands">
        ClientCommandHandler.instance.registerCommand(new RefreshVvmCommand());
        logger.info("Client commands registered.");
        //</editor-fold>

        //<editor-fold desc="gui resources">
        GuiResources.init();
        logger.info("GUI resources loaded.");
        //</editor-fold>

        //<editor-fold desc="themes">
        ThemeRegistry.init();
        logger.info("Theme configs loaded. They are: " + String.join(", ", ThemeRegistry.getThemeNames()));
        //</editor-fold>

        //<editor-fold desc="gui elements">
        String myPackage = "com.tttsaurus.ingameinfo";
        ElementRegistry.register();
        logger.info("GUI elements registered.");

        logger.info("");
        logger.info("Registered serviceable elements: ");
        List<Class<? extends Element>> constructableElements = ElementRegistry.getConstructableElements();
        for (Class<? extends Element> clazz: constructableElements)
            logger.info("- " + (TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName()));

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
        //</editor-fold>

        //<editor-fold desc="mvvm">
        logger.info("Start registering mvvm.");
        MinecraftForge.EVENT_BUS.post(new MvvmRegisterEvent());
        //</editor-fold>
    }
}
