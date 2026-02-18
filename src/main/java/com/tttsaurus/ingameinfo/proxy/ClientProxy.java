package com.tttsaurus.ingameinfo.proxy;

import com.google.common.collect.ImmutableMap;
import com.tttsaurus.ingameinfo.common.core.IgiRuntime;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.appcommunication.spotify.SpotifyOAuthUtils;
import com.tttsaurus.ingameinfo.common.core.commonutils.FileUtils;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpablePropertyGetter;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpTarget;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallbackPost;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertyCallbackPre;
import com.tttsaurus.ingameinfo.common.core.gui.property.style.StylePropertySetter;
import com.tttsaurus.ingameinfo.common.core.reflection.TypeUtils;
import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.core.shutdown.ShutdownHooks;
import com.tttsaurus.ingameinfo.common.impl.IgiRuntimeEntryPoint;
import com.tttsaurus.ingameinfo.common.impl.appcommunication.spotify.SpotifyCommandHandler;
import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.registry.ElementRegistry;
import com.tttsaurus.ingameinfo.common.core.gui.theme.registry.ThemeRegistry;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleProvider;
import com.tttsaurus.ingameinfo.common.impl.mvvm.command.RefreshVvmCommand;
import com.tttsaurus.ingameinfo.config.IgiCommonConfig;
import com.tttsaurus.ingameinfo.config.IgiDefaultLifecycleProviderConfig;
import com.tttsaurus.ingameinfo.config.IgiSpotifyIntegrationConfig;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event, Logger logger)
    {
        super.preInit(event, logger);

        //<editor-fold desc="lifecycle provider setup">
        if (IgiCommonConfig.GUI_LIFECYCLE_PROVIDER instanceof DefaultLifecycleProvider provider)
        {
            logger.info("Default GUI Lifecycle Provider is in use.");

            int majorGlVersion = RenderHints.getMajorGlVersion();
            int minorGlVersion = RenderHints.getMinorGlVersion();

            boolean enableFbo = IgiDefaultLifecycleProviderConfig.ENABLE_FRAMEBUFFER && OpenGlHelper.framebufferSupported;
            // at least gl 33
            boolean enablePostProcessing = IgiDefaultLifecycleProviderConfig.ENABLE_POST_PROCESSING_SHADER && ((majorGlVersion == 3 && minorGlVersion >= 3) || majorGlVersion > 3);
            // at least gl 40
            boolean enableMsfbo = IgiDefaultLifecycleProviderConfig.ENABLE_MSFRAMEBUFFER && majorGlVersion >= 4;

            // framebuffer is the prerequisite
            enablePostProcessing = enableFbo && enablePostProcessing;
            enableMsfbo = enableFbo && enableMsfbo;

            logger.info("Default GUI Lifecycle Provider: Framebuffer is " + (enableFbo ? "ON" : "OFF"));
            logger.info("Default GUI Lifecycle Provider: Post-Processing on framebuffer is " + (enablePostProcessing ? "ON" : "OFF") + " (requires GL33 and framebuffer)");
            logger.info("Default GUI Lifecycle Provider: Multisampling on framebuffer is " + (enableMsfbo ? "ON" : "OFF") + " (requires GL40 and framebuffer)");

            provider.setEnableFbo(enableFbo);
            provider.setEnableShader(enablePostProcessing);
            provider.setEnableMultisampleOnFbo(enableMsfbo);
            RenderHints.fboSampleNum(IgiDefaultLifecycleProviderConfig.FRAMEBUFFER_SAMPLE_NUM);
        }
        else
            logger.info("Default GUI Lifecycle Provider is not in use.");

        IgiCommonConfig.GUI_LIFECYCLE_PROVIDER.setMaxFps_FixedUpdate(IgiCommonConfig.FIXED_UPDATE_LIMIT);
        IgiCommonConfig.GUI_LIFECYCLE_PROVIDER.setMaxFps_RenderUpdate(IgiCommonConfig.RENDER_UPDATE_LIMIT);

        logger.info("GUI Lifecycle Provider is ready.");
        //</editor-fold>

        //<editor-fold desc="spotify setup">
        if (IgiSpotifyIntegrationConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            SpotifyOAuthUtils.CLIENT_ID = IgiSpotifyIntegrationConfig.SPOTIFY_CLIENT_ID;
            SpotifyOAuthUtils.CLIENT_SECRET = IgiSpotifyIntegrationConfig.SPOTIFY_CLIENT_SECRET;
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
        MinecraftForge.EVENT_BUS.register(IgiRuntimeEntryPoint.class);
        MinecraftForge.EVENT_BUS.register(ThemeRegistry.class);
        logger.info("Core event listeners registered.");
        //</editor-fold>

        //<editor-fold desc="app communication">
        if (IgiSpotifyIntegrationConfig.ENABLE_SPOTIFY_INTEGRATION)
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

        //<editor-fold desc="ui elements">
        String myPackage = "com.tttsaurus.ingameinfo";
        ElementRegistry.register();
        logger.info("UI elements registered. See config/ingameinfo/ui_element_dump.md");

        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        builder.append("## Notice:\n");
        builder.append("1. Elements marked with `*` below are abstract (unserviceable in ixml).\n");
        builder.append("2. You can access style properties from parent elements.\n");
        builder.append("\n");

        List<Class<? extends Element>> elementClasses = ElementRegistry.getRegisteredElements();
        List<Class<? extends Element>> constructableElements = ElementRegistry.getConstructableElements();

        builder.append("## Registered Elements:\n");
        for (Class<? extends Element> clazz: elementClasses)
        {
            builder.append("- `")
                    .append(TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName())
                    .append(constructableElements.contains(clazz) ? "" : "*")
                    .append("`\n");
        }
        builder.append("\n").append("## Details:\n\n");

        ImmutableMap<String, Map<String, StylePropertySetter>> setters = ElementRegistry.getStylePropertySetters();
        ImmutableMap<StylePropertySetter, Deserializer<?>> deserializers = ElementRegistry.getStylePropertyDeserializers();
        ImmutableMap<StylePropertySetter, StylePropertyCallbackPre> setterCallbacksPre = ElementRegistry.getStylePropertySetterCallbacksPre();
        ImmutableMap<StylePropertySetter, StylePropertyCallbackPost> setterCallbacksPost = ElementRegistry.getStylePropertySetterCallbacksPost();
        ImmutableMap<StylePropertySetter, Class<?>> classes = ElementRegistry.getStylePropertyClasses();
        ImmutableMap<String, Map<LerpablePropertyGetter, LerpTarget>> getters = ElementRegistry.getLerpablePropertyGetters();

        int count = elementClasses.size();
        int index = 0;
        for (Class<? extends Element> clazz: elementClasses)
        {
            String parentMsg = "";
            if (!clazz.equals(Element.class))
                parentMsg =
                    " extends `" +
                    (TypeUtils.isFromParentPackage(clazz.getSuperclass(), myPackage) ? clazz.getSuperclass().getSimpleName() : clazz.getSuperclass().getName()) +
                    (constructableElements.contains(clazz.getSuperclass()) ? "" : "*") + "`";

            builder.append("**Element Type**: `")
                    .append(TypeUtils.isFromParentPackage(clazz, myPackage) ? clazz.getSimpleName() : clazz.getName())
                    .append(constructableElements.contains(clazz) ? "" : "*")
                    .append("`")
                    .append(parentMsg).append("\n");

            Map<String, StylePropertySetter> map1 = setters.get(clazz.getName());
            if (!map1.isEmpty())
            {
                builder.append("- With Style Properties:\n");
                for (Map.Entry<String, StylePropertySetter> entry: map1.entrySet())
                {
                    StylePropertySetter primaryKey = entry.getValue();

                    String suffix = "";
                    if (deserializers.containsKey(primaryKey))
                        suffix = " (with deserializer: `" + (TypeUtils.isFromParentPackage(deserializers.get(primaryKey).getClass(), myPackage) ? deserializers.get(primaryKey).getClass().getSimpleName() : deserializers.get(primaryKey).getClass().getName()) + "`)";

                    builder.append("  - **[_").append(classes.get(primaryKey).getSimpleName()).append("_]** ")
                            .append("`").append(entry.getKey()).append("`")
                            .append(suffix).append("\n");

                    if (setterCallbacksPre.containsKey(primaryKey))
                        builder.append("    - Setter callback pre: `").append(setterCallbacksPre.get(primaryKey).name()).append("`\n");
                    if (setterCallbacksPost.containsKey(primaryKey))
                        builder.append("    - Setter callback post: `").append(setterCallbacksPost.get(primaryKey).name()).append("`\n");
                }
            }

            Map<LerpablePropertyGetter, LerpTarget> map2 = getters.get(clazz.getName());
            if (!map2.isEmpty())
            {
                builder.append("- With Lerpable Properties:\n");
                for (LerpTarget lerpTarget: map2.values())
                {
                    builder.append("  - ").append("`").append(lerpTarget.value());
                    if (!lerpTarget.inner0().isEmpty())
                        builder.append(".").append(lerpTarget.inner0());
                    if (!lerpTarget.inner1().isEmpty())
                        builder.append(".").append(lerpTarget.inner1());
                    builder.append("`").append("\n");
                }
            }

            builder.append("\n");

            if (index != count - 1) builder.append("***\n\n");
            index++;
        }

        try
        {
            RandomAccessFile raf = new RandomAccessFile(FileUtils.makeFile("ui_element_dump.md"), "rw");
            raf.setLength(0);
            raf.seek(0);
            raf.writeBytes(builder.toString());
            raf.close();
        }
        catch (IOException ignored) { }
        //</editor-fold>

        //<editor-fold desc="igi runtime">
        InternalMethods.IgiRuntime$init();
        IgiRuntime runtime = InternalMethods.IgiRuntime$instance$getter();
        MinecraftForge.EVENT_BUS.post(new IgiRuntimeEntryPointEvent(runtime));
        //</editor-fold>
    }
}
