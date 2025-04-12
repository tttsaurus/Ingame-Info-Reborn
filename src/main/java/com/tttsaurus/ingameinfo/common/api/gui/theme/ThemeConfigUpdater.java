package com.tttsaurus.ingameinfo.common.api.gui.theme;

public final class ThemeConfigUpdater
{
    private final ThemeConfig config;
    private final int version;

    public ThemeConfig getConfig() { return config; }
    public int getVersion() { return version; }

    public ThemeConfigUpdater(ThemeConfig config, int version)
    {
        this.config = config;
        this.version = version;
    }

    public void update()
    {

    }
}
