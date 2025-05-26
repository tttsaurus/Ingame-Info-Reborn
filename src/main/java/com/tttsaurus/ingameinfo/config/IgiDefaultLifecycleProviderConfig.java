package com.tttsaurus.ingameinfo.config;

import net.minecraftforge.common.config.Configuration;

public final class IgiDefaultLifecycleProviderConfig
{
    public static boolean ENABLE_FRAMEBUFFER;
    public static boolean ENABLE_MSFRAMEBUFFER;
    public static int FRAMEBUFFER_SAMPLE_NUM;

    public static boolean ENABLE_POST_PROCESSING_SHADER;
    public static boolean ENABLE_PP_ALPHA;
    public static float PP_ALPHA;

    public static Configuration CONFIG;

    public static void loadConfig()
    {
        try
        {
            ENABLE_FRAMEBUFFER = CONFIG.getBoolean("Enable Framebuffer", "default", true, "Reminder: framebuffer may fail on old devices \nEven if it failed, nothing will crash and everything still works fine");
            ENABLE_MSFRAMEBUFFER = CONFIG.getBoolean("Enable Multisample Framebuffer", "default.multisampling", false, "Whether to enable multisampling on framebuffer (requires GL40 support) \nand framebuffer is the prerequisite for it to work \nNotice: it's the prerequisite of multisample anti-aliasing");
            FRAMEBUFFER_SAMPLE_NUM = CONFIG.getInt("Multisample Framebuffer Sample Number", "default.multisampling", 4, 1, 4, "Number of samples a multisampled framebuffer has");

            ENABLE_POST_PROCESSING_SHADER = CONFIG.getBoolean("Enable Post Processing Shaders", "default.post_processing", false, "Whether to enable post-processing on framebuffer (requires GL33 support) \nand framebuffer is the prerequisite for it to work");
            ENABLE_PP_ALPHA = CONFIG.getBoolean("Enable Alpha Module", "default.post_processing.alpha", false, "Make the whole GUI overlay transparent");
            PP_ALPHA = CONFIG.getFloat("Alpha Value", "default.post_processing.alpha", 0.5f, 0f, 1f, "0.0 for full transparency; 1.0 for full opacity");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
