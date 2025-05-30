package com.tttsaurus.ingameinfo.common.core.gui.theme;

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
        public McVanilla mcVanilla = new McVanilla();

        @ConfigSerializable
        public static class Box
        {
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String color = "#383838";

            public transient int parsedColor;
        }

        @ConfigSerializable
        public static class BoxWithOutline
        {
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String color = "#383838";
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String outlineColor = "#232323";

            public transient int parsedColor;
            public transient int parsedOutlineColor;
        }

        @ConfigSerializable
        public static class RoundedBox
        {
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String color = "#383838";
            public float cornerRadius = 3f;

            public transient int parsedColor;
        }

        @ConfigSerializable
        public static class RoundedBoxWithOutline
        {
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String color = "#383838";
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String outlineColor = "#232323";
            public float cornerRadius = 3f;

            public transient int parsedColor;
            public transient int parsedOutlineColor;
        }

        @ConfigSerializable
        public static class McVanilla
        {
            @Comment("In the form of hex #rrggbb or #aarrggbb.")
            public String color = "#ffffff";

            public transient int parsedColor;
        }
    }

    public Element element = new Element();
    public Text text = new Text();
    public SlidingText slidingText = new SlidingText();
    public AnimText animText = new AnimText();
    public Image image = new Image();
    public UrlImage urlImage = new UrlImage();
    public ProgressBar progressBar = new ProgressBar();
    public Button button = new Button();

    @ConfigSerializable
    public static class Element
    {
        @Comment("This will only be applied to Element with style property 'backgroundStyle' left empty.\n" +
                "Valid values are 'box', 'box-with-outline', 'rounded-box', 'rounded-box-with-outline', 'mc-vanilla'")
        public String backgroundStyle = "";
    }

    @ConfigSerializable
    public static class Text
    {
        @Comment("This will only be applied to Text with style property 'color' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String color = "#d2d2d2";
        @Comment("This will only be applied to Text with style property 'scale' left empty.")
        public float scale = 1f;

        public transient int parsedColor;
    }

    @ConfigSerializable
    public static class SlidingText
    {
        @Comment("This will only be applied to SlidingText with style property 'color' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String color = "#d2d2d2";
        @Comment("This will only be applied to SlidingText with style property 'scale' left empty.")
        public float scale = 1f;

        public transient int parsedColor;
    }

    @ConfigSerializable
    public static class AnimText
    {
        @Comment("This will only be applied to AnimText with style property 'color' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String color = "#d2d2d2";
        @Comment("This will only be applied to AnimText with style property 'scale' left empty.")
        public float scale = 1f;

        public transient int parsedColor;
    }

    @ConfigSerializable
    public static class Image
    {
        public float cornerRadius = 3f;
    }

    @ConfigSerializable
    public static class UrlImage
    {
        public float cornerRadius = 3f;
    }

    @ConfigSerializable
    public static class ProgressBar
    {
        @Comment("This will only be applied to ProgressBar with style property 'fillerColor' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String fillerColor = "#d2d2d2";
        @Comment("This will only be applied to ProgressBar with style property 'backgroundColor' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String backgroundColor = "#383838";
        @Comment("This will only be applied to ProgressBar with style property 'outlineColor' left empty.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String outlineColor = "#232323";

        public transient int parsedFillerColor;
        public transient int parsedBackgroundColor;
        public transient int parsedOutlineColor;
    }

    @ConfigSerializable
    public static class Button
    {
        @Comment("This will only be applied to Button with style property 'defaultColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String defaultColor = "#ffffff";
        @Comment("This will only be applied to Button with style property 'hoverColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String hoverColor = "#bec7ff";
        @Comment("This will only be applied to Button with style property 'holdColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String holdColor = "#232323";
        @Comment("This will only be applied to Button with style property 'defaultTextColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String defaultTextColor = "#ffffff";
        @Comment("This will only be applied to Button with style property 'hoverTextColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String hoverTextColor = "#ffffa0";
        @Comment("This will only be applied to Button with style property 'holdTextColor' left empty when using this style.\n" +
                "In the form of hex #rrggbb or #aarrggbb.")
        public String holdTextColor = "#383838";

        public transient int parsedDefaultColor;
        public transient int parsedHoverColor;
        public transient int parsedHoldColor;
        public transient int parsedDefaultTextColor;
        public transient int parsedHoverTextColor;
        public transient int parsedHoldTextColor;
    }
}
