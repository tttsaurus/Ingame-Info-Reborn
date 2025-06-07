package com.tttsaurus.ingameinfo.common.core.gui.theme.registry;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.event.IgiGuiRegainScreenFocusEvent;
import com.tttsaurus.ingameinfo.common.core.file.FileUtils;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfigSerDesUtils;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfigUpdater;
import com.tttsaurus.ingameinfo.config.IgiSpotifyIntegrationConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ThemeRegistry
{
    // key: theme name
    private static final Map<String, ThemeConfig> themeConfigs = new HashMap<>();
    private static ThemeConfig defaultTheme;

    // call after init
    public static ThemeConfig getDefaultTheme()
    {
        return defaultTheme;
    }
    public static ThemeConfig getTheme(String themeName)
    {
        ThemeConfig themeConfig = themeConfigs.get(themeName);
        if (themeConfig == null)
            return defaultTheme;
        return themeConfig;
    }
    public static Set<String> getThemeNames()
    {
        return themeConfigs.keySet();
    }

    @SuppressWarnings("all")
    public static void init()
    {
        if (!themeConfigs.containsKey("default"))
        {
            ThemeConfig defaultTheme = new ThemeConfig();

            try
            {
                File defaultFile = FileUtils.getFile("default.itheme", "themes");
                boolean writeDefault = !defaultFile.exists();

                RandomAccessFile file = new RandomAccessFile(defaultFile, "rw");
                StringBuilder builder = new StringBuilder();

                if (writeDefault)
                {
                    String config = ThemeConfigSerDesUtils.serialize(new ThemeConfig());
                    file.write(config.getBytes(StandardCharsets.UTF_8));
                    builder.append(config);
                }
                else
                {
                    String line = file.readLine();
                    while (line != null)
                    {
                        builder.append(line).append("\n");
                        line = file.readLine();
                    }
                }

                ThemeConfigUpdater updater = ThemeConfigSerDesUtils.deserialize(builder.toString());
                if (updater.update())
                {
                    String config = ThemeConfigSerDesUtils.serialize(updater.getConfig());
                    file.setLength(0);
                    file.seek(0);
                    file.write(config.getBytes(StandardCharsets.UTF_8));
                }
                defaultTheme = updater.getConfig();

                file.close();
            }
            catch (Exception exception)
            {
                InGameInfoReborn.LOGGER.error("Caught an exception when creating and loading the default theme.");
                InGameInfoReborn.LOGGER.throwing(exception);
            }

            themeConfigs.put("default", defaultTheme);
            ThemeRegistry.defaultTheme = defaultTheme;
        }

        if (IgiSpotifyIntegrationConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            try
            {
                File spotify = FileUtils.getFile("spotify.itheme", "themes");
                if (!spotify.exists())
                {
                    RandomAccessFile file = new RandomAccessFile(spotify, "rw");

                    ThemeConfig spotifyTheme = new ThemeConfig();
                    spotifyTheme.element.backgroundStyle = "mc-vanilla";
                    spotifyTheme.text.color = "#232323";
                    spotifyTheme.slidingText.color = "#232323";
                    spotifyTheme.animText.color = "#232323";

                    String config = ThemeConfigSerDesUtils.serialize(spotifyTheme);
                    file.write(config.getBytes(StandardCharsets.UTF_8));

                    file.close();
                }
            }
            catch (Exception exception)
            {
                InGameInfoReborn.LOGGER.error("Caught an exception when creating the spotify theme.");
                InGameInfoReborn.LOGGER.throwing(exception);
            }
        }

        File[] files = FileUtils.makeDir("themes").listFiles();
        if (files != null)
        {
            for (File file: files)
            {
                if (file.isFile() && !file.getName().equals("default.itheme"))
                {
                    String[] args = file.getName().split("\\.");

                    if (args.length != 2) continue;
                    if (!args[1].equals("itheme")) continue;
                    String themeName = args[0];

                    try
                    {
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        StringBuilder builder = new StringBuilder();

                        String line = raf.readLine();
                        while (line != null)
                        {
                            builder.append(line).append("\n");
                            line = raf.readLine();
                        }

                        ThemeConfigUpdater updater = ThemeConfigSerDesUtils.deserialize(builder.toString());
                        if (updater.update())
                        {
                            String config = ThemeConfigSerDesUtils.serialize(updater.getConfig());
                            raf.setLength(0);
                            raf.seek(0);
                            raf.write(config.getBytes(StandardCharsets.UTF_8));
                        }
                        themeConfigs.put(themeName, updater.getConfig());

                        raf.close();
                    }
                    catch (Exception exception)
                    {
                        InGameInfoReborn.LOGGER.error("Caught an exception when loading the theme config '" + themeName + "'.");
                        InGameInfoReborn.LOGGER.throwing(exception);
                    }
                }
            }
        }
    }

    // auto-reload
    @SubscribeEvent
    public static void onRegainScreenFocus(IgiGuiRegainScreenFocusEvent event)
    {
        InGameInfoReborn.LOGGER.info("Regain screen focus. Trying to reload theme configs...");

        File[] files = FileUtils.makeDir("themes").listFiles();
        if (files != null)
        {
            for (File file: files)
            {
                if (file.isFile())
                {
                    String[] args = file.getName().split("\\.");

                    if (args.length != 2) continue;
                    if (!args[1].equals("itheme")) continue;
                    String themeName = args[0];

                    long lastModified = file.lastModified();
                    long fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000);
                    if (lastModified > fiveMinutesAgo)
                    {
                        InGameInfoReborn.LOGGER.info("Theme config '" + themeName + "' was modified within 5 minutes. Start reloading.");
                        try
                        {
                            RandomAccessFile raf = new RandomAccessFile(file, "rw");
                            StringBuilder builder = new StringBuilder();

                            String line = raf.readLine();
                            while (line != null)
                            {
                                builder.append(line).append("\n");
                                line = raf.readLine();
                            }

                            ThemeConfigUpdater updater = ThemeConfigSerDesUtils.deserialize(builder.toString());
                            if (updater.update())
                            {
                                String config = ThemeConfigSerDesUtils.serialize(updater.getConfig());
                                raf.setLength(0);
                                raf.seek(0);
                                raf.write(config.getBytes(StandardCharsets.UTF_8));
                            }
                            themeConfigs.put(themeName, updater.getConfig());

                            raf.close();
                            InGameInfoReborn.LOGGER.info("Theme config '" + themeName + "' reloaded.");
                        }
                        catch (Exception exception)
                        {
                            InGameInfoReborn.LOGGER.error("Caught an exception when reloading the theme config '" + themeName + "'.");
                            InGameInfoReborn.LOGGER.throwing(exception);
                        }
                    }
                }
            }
        }
    }
}
