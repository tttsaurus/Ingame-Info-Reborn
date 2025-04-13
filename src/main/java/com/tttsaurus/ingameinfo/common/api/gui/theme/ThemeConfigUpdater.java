package com.tttsaurus.ingameinfo.common.api.gui.theme;

import org.spongepowered.configurate.ConfigurationNode;

public final class ThemeConfigUpdater
{
    private final ConfigurationNode root;
    private final ThemeConfig config;
    private final int version;

    public ThemeConfig getConfig() { return config; }
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
