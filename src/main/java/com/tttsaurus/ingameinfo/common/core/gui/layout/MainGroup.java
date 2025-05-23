package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MainGroup extends ElementGroup
{
    public MainGroup()
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        rect.width = (float)resolution.getScaledWidth_double();
        rect.height = (float)resolution.getScaledHeight_double();
    }

    @Override
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        if (elements.isEmpty()) return;
        for (Element element: elements)
        {
            element.rect.x = rect.x + rect.width * element.alignment.vertical;
            element.rect.y = rect.y + rect.height * element.alignment.horizontal;
            if (element.pivot.vertical == 0 || element.pivot.vertical == 0.5f) element.rect.x += element.padding.left;
            if (element.pivot.vertical == 1 || element.pivot.vertical == 0.5f) element.rect.x -= element.padding.right;
            if (element.pivot.horizontal == 0 || element.pivot.horizontal == 0.5f) element.rect.y += element.padding.top;
            if (element.pivot.horizontal == 1 || element.pivot.horizontal == 0.5f) element.rect.y -= element.padding.bottom;
            element.calcRenderPos(rect);
        }
    }

    @Override
    public void calcWidthHeight()
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        rect.width = (float)resolution.getScaledWidth_double();
        rect.height = (float)resolution.getScaledHeight_double();
        super.calcWidthHeight();
    }

    @Override
    public void renderDebugRect()
    {
        for (Element element: elements)
            element.renderDebugRect();
    }
}
