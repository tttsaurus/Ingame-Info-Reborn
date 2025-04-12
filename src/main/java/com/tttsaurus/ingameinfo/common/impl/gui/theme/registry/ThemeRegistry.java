package com.tttsaurus.ingameinfo.common.impl.gui.theme.registry;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.api.gui.theme.ThemeConfigSerDesUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class ThemeRegistry
{
    // key: theme name
    private static final Map<String, ThemeConfig> themeConfigs = new HashMap<>();

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

                String filePath = "config/ingameinfo/themes/default.itheme";
                File testFile = new File(filePath);
                boolean writeDefault = !testFile.exists();

                RandomAccessFile file = new RandomAccessFile(filePath, "rw");
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
                        builder.append(line + "\n");
                        line = file.readLine();
                    }
                }

                file.close();

                if (!writeDefault)
                    defaultTheme = ThemeConfigSerDesUtils.deserialize(builder.toString()).getConfig();
            }
            catch (Exception ignored) { }

            themeConfigs.put("default", defaultTheme);
        }
    }
}
