package com.tttsaurus.ingameinfo.common.core.commonutils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public final class MouseUtils
{
    // under Minecraft's scaled resolution coordinate system

    private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static int getMouseX()
    {
        ScaledResolution resolution = new ScaledResolution(MINECRAFT);
        int mouseX = Mouse.getX();
        return mouseX / resolution.getScaleFactor();
    }
    public static int getMouseY()
    {
        ScaledResolution resolution = new ScaledResolution(MINECRAFT);
        int mouseY = MINECRAFT.displayHeight - Mouse.getY() - 1;
        return mouseY / resolution.getScaleFactor();
    }
    public static boolean isMouseDownLeft()
    {
        return Mouse.isButtonDown(0);
    }
    public static boolean isMouseDownRight()
    {
        return Mouse.isButtonDown(1);
    }
}
