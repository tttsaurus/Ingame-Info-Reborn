package com.tttsaurus.ingameinfo.common.api.gui.theme;

import org.spongepowered.configurate.ConfigurationNode;

public final class ThemeConfigUpdater
{
    private final ConfigurationNode root;
    private final ThemeConfig config;
    private final int version;

    private int parseColor(String hex)
    {
        if (hex == null) return 0;
        if (hex.isEmpty()) return 0;
        if (hex.length() == 6)
            return 0xff000000 | Integer.parseInt(hex, 16);
        if (hex.length() == 8)
            return (int)Long.parseLong(hex, 16);
        return 0;
    }

    private void parse()
    {
        config.backgroundStyles.box.parsedColor = parseColor(config.backgroundStyles.box.color);

        config.backgroundStyles.boxWithOutline.parsedColor = parseColor(config.backgroundStyles.boxWithOutline.color);
        config.backgroundStyles.boxWithOutline.parsedOutlineColor = parseColor(config.backgroundStyles.boxWithOutline.outlineColor);

        config.backgroundStyles.roundedBox.parsedColor = parseColor(config.backgroundStyles.roundedBox.color);

        config.backgroundStyles.roundedBoxWithOutline.parsedColor = parseColor(config.backgroundStyles.roundedBoxWithOutline.color);
        config.backgroundStyles.roundedBoxWithOutline.parsedOutlineColor = parseColor(config.backgroundStyles.roundedBoxWithOutline.outlineColor);
    }

    public ThemeConfig getConfig()
    {
        parse();
        return config;
    }
    public int getVersion() { return version; }

    public ThemeConfigUpdater(ThemeConfig config, int version, ConfigurationNode root)
    {
        this.config = config;
        this.version = version;
        this.root = root;
    }

    public boolean update()
    {
        if (version < ThemeConfig.latestVersion)
        {
            // update

            return true;
        }
        else
            return false;
    }
}
