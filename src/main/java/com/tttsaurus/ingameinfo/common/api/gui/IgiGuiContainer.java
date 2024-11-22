package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.*;

public class IgiGuiContainer
{
    protected boolean isFocused = false;
    protected boolean hasFocusBackground = true;
    protected int backgroundColor = (new Color(80, 80, 80, 160)).getRGB();

    public boolean getFocused() { return isFocused; }

    public void onRenderUpdate()
    {
        if (isFocused && hasFocusBackground)
        {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtils.renderGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), backgroundColor, backgroundColor);
        }
    }
}
