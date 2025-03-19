package com.tttsaurus.ingameinfo.common.api.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class ScaledRes2NdcUtils
{
    private static final Minecraft minecraft = Minecraft.getMinecraft();

    public static float toNdcX(float x)
    {
        ScaledResolution resolution = new ScaledResolution(minecraft);
        float resWidth = (float)resolution.getScaledWidth_double();
        return ((x - resWidth / 2f) / resWidth) * 2f;
    }
    public static float toNdcY(float y)
    {
        ScaledResolution resolution = new ScaledResolution(minecraft);
        float resHeight = (float)resolution.getScaledHeight_double();
        return (((resHeight - y) - resHeight / 2f) / resHeight) * 2f;
    }
    public static float toNdcWidth(float width)
    {
        ScaledResolution resolution = new ScaledResolution(minecraft);
        float resWidth = (float)resolution.getScaledWidth_double();
        return (width / resWidth) * 2f;
    }
    public static float toNdcHeight(float height)
    {
        ScaledResolution resolution = new ScaledResolution(minecraft);
        float resHeight = (float)resolution.getScaledHeight_double();
        return -(height / resHeight) * 2f;
    }
}
