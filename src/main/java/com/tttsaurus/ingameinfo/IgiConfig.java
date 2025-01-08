package com.tttsaurus.ingameinfo;

import net.minecraftforge.common.config.Configuration;

public class IgiConfig
{
    public static String SPOTIFY_CLIENT_ID;
    public static String SPOTIFY_CLIENT_SECRET;
    public static boolean SPOTIFY_AUTO_DISPLAY;

    public static Configuration CONFIG;

    public static void loadConfig()
    {
        try
        {
            CONFIG.load();

            SPOTIFY_CLIENT_ID = CONFIG.getString("Spotify Client Id", "spotify", "", "Input client id of your spotify app \nDeclaration: this mod doesn't record or share your client id \nand it's not recommended for you to share your client id \nGuide: you have to create a spotify app to get client id & secrete \nhttps://developer.spotify.com/documentation/web-api/concepts/apps \nRedirect URI should be set to http://localhost:8888 for this mod to listen");
            SPOTIFY_CLIENT_SECRET = CONFIG.getString("Spotify Client Secret", "spotify", "", "Input client secret of your spotify app \nDeclaration: this mod doesn't record or share your client secret \nand it's not recommended for you to share your client secret");
            SPOTIFY_AUTO_DISPLAY = CONFIG.getBoolean("Try Auto Display Overlay", "spotify", true, "Whether to try displaying the overlay when the game starts \nMay fail due to token/auth issues");
        }
        catch (Exception ignored) { }
        finally
        {
            if (CONFIG.hasChanged()) CONFIG.save();
        }
    }
}
