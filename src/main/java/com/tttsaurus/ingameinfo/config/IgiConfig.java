package com.tttsaurus.ingameinfo.config;

import net.minecraftforge.common.config.Configuration;

public class IgiConfig
{
    public static int FIXED_UPDATE_LIMIT;
    public static boolean ENABLE_FRAMEBUFFER;
    public static int RENDER_UPDATE_LIMIT;

    public static boolean ENABLE_MSFRAMEBUFFER;
    public static int FRAMEBUFFER_SAMPLE_NUM;

    public static boolean ENABLE_POST_PROCESSING_SHADER;
    public static boolean ENABLE_PP_ALPHA;
    public static float PP_ALPHA;

    public static boolean ENABLE_SPOTIFY_INTEGRATION;
    public static String SPOTIFY_CLIENT_ID;
    public static String SPOTIFY_CLIENT_SECRET;
    public static boolean SPOTIFY_AUTO_DISPLAY;
    public static boolean SPOTIFY_EXTENDED_LAYOUT;

    public static Configuration CONFIG;
    public static ForgeConfigWriter CONFIG_WRITER;

    public static void useSpotifyExtendedLayout(boolean flag)
    {
        SPOTIFY_EXTENDED_LAYOUT = flag;
        CONFIG_WRITER.replaceBoolean("spotify", "Use Extended Overlay Layout", flag);
    }

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            FIXED_UPDATE_LIMIT = CONFIG.getInt("Gui Fixed Update Limit", "igi", 30, 30, 300, "It represents how many updates will be called per second \nand this limit is for animation calculations & such \"fixed\" calculations \nNotice: this limit should be smaller than or equal to \"Gui Render Update Limit\" \nand becomes meaningless when it's greater than \"Gui Render Update Limit\" \nRecommended values: 30, 60, 125, 240");
            ENABLE_FRAMEBUFFER = CONFIG.getBoolean("Enable Framebuffer", "igi", true, "Reminder: framebuffer may fail on old devices \nEven if it failed, nothing will crash and everything still works fine");
            RENDER_UPDATE_LIMIT = CONFIG.getInt("Gui Render Update Limit", "igi", 30, 30, 320, "It represents how many render updates (render to framebuffer) will be called per second \nand framebuffer is the prerequisite for it to work \nNotice: you don't want this limit to exceed your screen refresh rate because that's meaningless \nRecommended values: 30, 60, 165, 240");

            ENABLE_MSFRAMEBUFFER = CONFIG.getBoolean("Enable Multisample Framebuffer", "igi.multisampling", false, "Whether to enable multisampling on framebuffer (requires GL40 support) \nand framebuffer is the prerequisite for it to work\nNotice: it's the prerequisite for multisample anti-aliasing");
            FRAMEBUFFER_SAMPLE_NUM = CONFIG.getInt("Multisample Framebuffer Sample Number", "igi.multisampling", 4, 1, 4, "Number of samples a multisampled framebuffer has");

            ENABLE_POST_PROCESSING_SHADER = CONFIG.getBoolean("Enable Post Processing Shaders", "igi.post_processing", false, "Whether to enable post-processing on framebuffer (requires GL33 support) \nand framebuffer is the prerequisite for it to work");
            ENABLE_PP_ALPHA = CONFIG.getBoolean("Enable Alpha Module", "igi.post_processing.alpha", false, "Make the whole gui overlay transparent");
            PP_ALPHA = CONFIG.getFloat("Alpha Value", "igi.post_processing.alpha", 0.5f, 0f, 1f, "0.0 for full transparency; 1.0 for full opacity");

            ENABLE_SPOTIFY_INTEGRATION = CONFIG.getBoolean("Enable Spotify Integration", "spotify", false, "Whether to enable the whole integration module");
            SPOTIFY_CLIENT_ID = CONFIG.getString("Spotify Client Id", "spotify", "", "Input client id of your spotify app \nDeclaration: this mod doesn't record or share your client id \nand it's not recommended for you to share your client id \nGuide: you have to create a spotify app to get client id & secrete \nhttps://developer.spotify.com/documentation/web-api/concepts/apps \nRedirect URI should be set to http://localhost:8888 for this mod to listen");
            SPOTIFY_CLIENT_SECRET = CONFIG.getString("Spotify Client Secret", "spotify", "", "Input client secret of your spotify app \nDeclaration: this mod doesn't record or share your client secret \nand it's not recommended for you to share your client secret");
            SPOTIFY_AUTO_DISPLAY = CONFIG.getBoolean("Try Auto Display Overlay", "spotify", true, "Whether to try displaying the overlay when the game starts \nMay fail due to token/auth issues");
            SPOTIFY_EXTENDED_LAYOUT = CONFIG.getBoolean("Use Extended Overlay Layout", "spotify", false, "Whether to display more info on the overlay");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
