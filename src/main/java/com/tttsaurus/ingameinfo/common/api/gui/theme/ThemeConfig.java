package com.tttsaurus.ingameinfo.common.api.gui.theme;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ThemeConfig
{
    public static final int latestVersion = 1;

    @Setting("Element")
    public Element element = new Element();

    @ConfigSerializable
    public static class Element
    {
        @Setting("background-style")
        public String backgroundStyle = "";
    }
}
