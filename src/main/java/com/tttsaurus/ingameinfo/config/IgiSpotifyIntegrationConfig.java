package com.tttsaurus.ingameinfo.config;

import net.minecraftforge.common.config.Configuration;

public final class IgiSpotifyIntegrationConfig
{
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
        CONFIG_WRITER.replaceBoolean("default", "Use Extended Overlay Layout", flag);
    }

    public static void loadConfig()
    {
        try
        {
            ENABLE_SPOTIFY_INTEGRATION = CONFIG.getBoolean("Enable Spotify Integration", "default", false, "Whether to enable the whole integration module");
            SPOTIFY_CLIENT_ID = CONFIG.getString("Spotify Client Id", "default", "", "Input client id of your spotify app \nDeclaration: this mod doesn't record or share your client id \nand it's not recommended for you to share your client id \nGuide: you have to create a spotify app to get client id & secrete \nhttps://developer.spotify.com/documentation/web-api/concepts/apps \nRedirect URI must be set to http://127.0.0.1:8888 for this mod to listen");
            SPOTIFY_CLIENT_SECRET = CONFIG.getString("Spotify Client Secret", "default", "", "Input client secret of your spotify app \nDeclaration: this mod doesn't record or share your client secret \nand it's not recommended for you to share your client secret");
            SPOTIFY_AUTO_DISPLAY = CONFIG.getBoolean("Try Auto Display Overlay", "default", true, "Whether to try displaying the overlay when the game starts \nMay fail due to token/auth issues");
            SPOTIFY_EXTENDED_LAYOUT = CONFIG.getBoolean("Use Extended Overlay Layout", "default", false, "Whether to display more info on the overlay");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
