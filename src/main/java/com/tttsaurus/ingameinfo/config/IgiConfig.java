package com.tttsaurus.ingameinfo.config;

import net.minecraftforge.common.config.Configuration;

public class IgiConfig
{
    public static int FIXED_UPDATE_LIMIT;
    public static boolean ENABLE_FRAMEBUFFER;
    public static int RENDER_UPDATE_LIMIT;

    public static String SPOTIFY_CLIENT_ID;
    public static String SPOTIFY_CLIENT_SECRET;
    public static boolean SPOTIFY_AUTO_DISPLAY;
    public static boolean SPOTIFY_EXTENDED_LAYOUT;

    public static Configuration CONFIG;
    public static ForgeConfigWriter CONFIG_WRITER;

    public static void useSpotifyExtendedLayout(boolean flag)
    {
        SPOTIFY_EXTENDED_LAYOUT = flag;
        CONFIG_WRITER.write("Use Extended Overlay Layout", flag ? "true" : "false");
    }

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            FIXED_UPDATE_LIMIT = CONFIG.getInt("Gui Fixed Update Limit", "igi", 125, 30, 300, "It represents how many updates will be called per second \nand this limit is for animation calculations & such \"fixed\" calculations \nNotice: this limit should be smaller than or equal to \"Gui Render Update Limit\" \nand becomes meaningless when it's greater than \"Gui Render Update Limit\" \nRecommended values: 60, 125, 240");
            ENABLE_FRAMEBUFFER = CONFIG.getBoolean("Enable Framebuffer", "igi", true, "Reminder: framebuffer may fail on old devices \nEven if it failed, nothing will crash and everything still works fine");
            RENDER_UPDATE_LIMIT = CONFIG.getInt("Gui Render Update Limit", "igi", 240, 60, 320, "It represents how many render updates (render to framebuffer) will be called per second \nThis option is only meaningful if you enabled framebuffer \nNotice: you don't want this limit to exceed your screen refresh rate because that's meaningless \nRecommended values: 60, 165, 240");

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
