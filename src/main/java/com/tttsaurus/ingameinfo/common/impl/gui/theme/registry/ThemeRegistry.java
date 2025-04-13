package com.tttsaurus.ingameinfo.common.impl.gui.theme.registry;

import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfigSerDesUtils;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfigUpdater;
import com.tttsaurus.ingameinfo.config.IgiConfig;
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
                File directory = new File("config/ingameinfo/themes");
                if (!directory.exists()) directory.mkdirs();

                File defaultFile = new File("config/ingameinfo/themes/default.itheme");
                boolean writeDefault = !defaultFile.exists();

                RandomAccessFile file = new RandomAccessFile(defaultFile, "rw");
                StringBuilder builder = new StringBuilder();

                if (writeDefault)
                {
                    String config = ThemeConfigSerDesUtils.serialize(new ThemeConfig());
                    file.write(config.getBytes(StandardCharsets.UTF_8));
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

                if (!writeDefault)
                {
                    ThemeConfigUpdater updater = ThemeConfigSerDesUtils.deserialize(builder.toString());
                    if (updater.update())
                    {
                        String config = ThemeConfigSerDesUtils.serialize(updater.getConfig());
                        file.setLength(0);
                        file.seek(0);
                        file.write(config.getBytes(StandardCharsets.UTF_8));
                    }
                    defaultTheme = updater.getConfig();
                }

                file.close();
            }
            catch (Exception ignored) { }

            themeConfigs.put("default", defaultTheme);
            ThemeRegistry.defaultTheme = defaultTheme;
        }

        if (IgiConfig.ENABLE_SPOTIFY_INTEGRATION)
        {
            try
            {
                File directory = new File("config/ingameinfo/themes");
                if (!directory.exists()) directory.mkdirs();

                File spotify = new File("config/ingameinfo/themes/spotify.itheme");
                if (!spotify.exists())
                {
                    RandomAccessFile file = new RandomAccessFile(spotify, "rw");

                    ThemeConfig spotifyTheme = new ThemeConfig();
                    spotifyTheme.element.backgroundStyle = "rounded-box-with-outline";

                    String config = ThemeConfigSerDesUtils.serialize(spotifyTheme);
                    file.write(config.getBytes(StandardCharsets.UTF_8));

                    file.close();
                }
            }
            catch (Exception ignored) { }
        }

        File directory = new File("config/ingameinfo/themes");
        if (!directory.exists()) directory.mkdirs();

        File[] files = directory.listFiles();
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
                    }
                    catch (Exception ignored) { }
                }
            }
        }
    }
}
