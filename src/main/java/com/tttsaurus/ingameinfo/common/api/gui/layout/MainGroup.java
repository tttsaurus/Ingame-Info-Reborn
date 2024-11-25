package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.gui.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MainGroup extends ElementGroup
{
    @Override
    public ElementGroup add(Element element)
    {
        element.outmost = true;
        elements.add(element);
        return this;
    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        for (Element element: elements)
            element.calcRenderPos(rect);
    }

    @Override
    public void calcWidthHeight()
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        rect.width = resolution.getScaledWidth();
        rect.height = resolution.getScaledHeight();
        super.calcWidthHeight();
    }

    @Override
    public void renderDebugRect()
    {
        for (Element element: elements)
            element.renderDebugRect();
    }
}
