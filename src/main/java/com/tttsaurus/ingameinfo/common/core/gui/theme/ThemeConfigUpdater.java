package com.tttsaurus.ingameinfo.common.core.gui.theme;

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
        if (hex.startsWith("#")) hex = hex.substring(1);
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

        config.backgroundStyles.mcVanilla.parsedColor = parseColor(config.backgroundStyles.mcVanilla.color);

        config.text.parsedColor = parseColor(config.text.color);
        config.slidingText.parsedColor = parseColor(config.slidingText.color);
        config.animText.parsedColor = parseColor(config.animText.color);

        config.progressBar.parsedFillerColor = parseColor(config.progressBar.fillerColor);
        config.progressBar.parsedBackgroundColor = parseColor(config.progressBar.backgroundColor);
        config.progressBar.parsedOutlineColor = parseColor(config.progressBar.outlineColor);

        config.button.simple.parsedDefaultColor = parseColor(config.button.simple.defaultColor);
        config.button.simple.parsedHoverColor = parseColor(config.button.simple.hoverColor);
        config.button.simple.parsedHoldColor = parseColor(config.button.simple.holdColor);
        config.button.simple.parsedDefaultTextColor = parseColor(config.button.simple.defaultTextColor);
        config.button.simple.parsedHoverTextColor = parseColor(config.button.simple.hoverTextColor);
        config.button.simple.parsedHoldTextColor = parseColor(config.button.simple.holdTextColor);

        config.button.mcVanilla.parsedDefaultColor = parseColor(config.button.mcVanilla.defaultColor);
        config.button.mcVanilla.parsedHoverColor = parseColor(config.button.mcVanilla.hoverColor);
        config.button.mcVanilla.parsedHoldColor = parseColor(config.button.mcVanilla.holdColor);
        config.button.mcVanilla.parsedDefaultTextColor = parseColor(config.button.mcVanilla.defaultTextColor);
        config.button.mcVanilla.parsedHoverTextColor = parseColor(config.button.mcVanilla.hoverTextColor);
        config.button.mcVanilla.parsedHoldTextColor = parseColor(config.button.mcVanilla.holdTextColor);
    }

    private void validate()
    {
        config.backgroundStyles.roundedBox.cornerRadius = Math.max(0f, config.backgroundStyles.roundedBox.cornerRadius);
        config.backgroundStyles.roundedBoxWithOutline.cornerRadius = Math.max(0f, config.backgroundStyles.roundedBoxWithOutline.cornerRadius);
        config.text.scale = Math.max(0f, config.text.scale);
        config.slidingText.scale = Math.max(0f, config.slidingText.scale);
        config.animText.scale = Math.max(0f, config.animText.scale);
        config.image.cornerRadius = Math.max(0f, config.image.cornerRadius);
        config.urlImage.cornerRadius = Math.max(0f, config.urlImage.cornerRadius);
    }

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
        {
            parse();
            validate();
            return false;
        }
    }
}
