package com.tttsaurus.ingameinfo.common.api.gui.theme;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class ThemeConfig
{
    public static final int latestVersion = 1;

    public BackgroundStyles backgroundStyles = new BackgroundStyles();

    @ConfigSerializable
    public static class BackgroundStyles
    {
        public Box box = new Box();
        public BoxWithOutline boxWithOutline = new BoxWithOutline();
        public RoundedBox roundedBox = new RoundedBox();
        public RoundedBoxWithOutline roundedBoxWithOutline = new RoundedBoxWithOutline();

        @ConfigSerializable
        public static class Box
        {
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String color = "383838";

            public transient int parsedColor;
        }

        @ConfigSerializable
        public static class BoxWithOutline
        {
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String color = "383838";
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String outlineColor = "232323";

            public transient int parsedColor;
            public transient int parsedOutlineColor;
        }

        @ConfigSerializable
        public static class RoundedBox
        {
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String color = "383838";
            public float cornerRadius = 3f;

            public transient int parsedColor;
        }

        @ConfigSerializable
        public static class RoundedBoxWithOutline
        {
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String color = "383838";
            @Comment("In the form of hex RRGGBB or AARRGGBB.")
            public String outlineColor = "232323";
            public float cornerRadius = 3f;

            public transient int parsedColor;
            public transient int parsedOutlineColor;
        }
    }

    public Element element = new Element();
    public Text text = new Text();

    @ConfigSerializable
    public static class Element
    {
        @Comment("This will only be applied to elements with style property 'backgroundStyle' left empty.\n" +
                "Valid values are 'box', 'box-with-outline', 'rounded-box', 'rounded-box-with-outline', 'mc-vanilla'")
        public String backgroundStyle = "";
    }

    @ConfigSerializable
    public static class Text
    {
        @Comment("In the form of hex RRGGBB or AARRGGBB.")
        public String color = "d2d2d2";
    }
}
