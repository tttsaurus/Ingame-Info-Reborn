package com.tttsaurus.ingameinfo.common.impl.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MainGroup extends SizedGroup
{
    public MainGroup()
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        width = resolution.getScaledWidth();
        height = resolution.getScaledHeight();
    }

    @Override
    public void calcWidthHeight()
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        width = resolution.getScaledWidth();
        height = resolution.getScaledHeight();
        super.calcWidthHeight();
    }

    @Override
    public void renderDebugRect()
    {
        for (Element element: elements)
            element.renderDebugRect();
    }
}
