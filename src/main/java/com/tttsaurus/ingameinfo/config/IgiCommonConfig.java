package com.tttsaurus.ingameinfo.config;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.gui.GuiLifecycleProvider;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleHolder;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Constructor;

public final class IgiCommonConfig
{
    public static GuiLifecycleProvider GUI_LIFECYCLE_PROVIDER = null;

    public static int FIXED_UPDATE_LIMIT;
    public static int RENDER_UPDATE_LIMIT;

    public static Configuration CONFIG;

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            String className = CONFIG.getString("GUI Lifecycle Provider Class", "default", "com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleProvider", "The lifecycle provider");
            try
            {
                Class<?> clazz = Class.forName(className);
                Class<? extends GuiLifecycleProvider> providerClass = clazz.asSubclass(GuiLifecycleProvider.class);
                Constructor<? extends GuiLifecycleProvider> constructor = providerClass.getConstructor();
                constructor.setAccessible(true);
                IgiCommonConfig.GUI_LIFECYCLE_PROVIDER = constructor.newInstance();
            }
            catch (Throwable throwable)
            {
                InGameInfoReborn.LOGGER.error("An invalid GUI Lifecycle Provider Class is provided.", throwable);
            }

            FIXED_UPDATE_LIMIT = CONFIG.getInt("GUI Fixed Update Limit", "default", 30, 30, 300, "It represents how many updates will be called per second \nand this limit is for animation calculations & other \"fixed\" calculations \nNotice: this limit should be smaller than or equal to \"GUI Render Update Limit\" \nand becomes meaningless when it's greater than \"GUI Render Update Limit\" \nRecommended values: 30, 60, 125, 240\n");
            RENDER_UPDATE_LIMIT = CONFIG.getInt("GUI Render Update Limit", "default", 30, 30, 320, "It represents how many render updates will be called per second \nNotice: you don't want this limit to exceed your screen refresh rate because that's meaningless \nRecommended values: 30, 60, 165, 240\n");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
