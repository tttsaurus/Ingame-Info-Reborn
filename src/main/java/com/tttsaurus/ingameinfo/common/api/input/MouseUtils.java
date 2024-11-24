package com.tttsaurus.ingameinfo.common.api.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public final class MouseUtils
{
    // under Minecraft scaled resolution

    public static int getMouseX()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        int mouseX = Mouse.getX();
        return mouseX / resolution.getScaleFactor();
    }
    public static int getMouseY()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        int mouseY = mc.displayHeight - Mouse.getY() - 1;
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
