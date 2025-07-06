package com.tttsaurus.ingameinfo.plugin.crt.impl.input;

import com.tttsaurus.ingameinfo.common.core.commonutils.MouseUtils;
import crafttweaker.annotations.ZenRegister;
import org.lwjgl.input.Keyboard;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ingameinfo.input.DirectInput")
public final class DirectInput
{
    @ZenMethod
    public static boolean isKeyDown(KeyWrapper keyWrapper)
    {
        return Keyboard.isKeyDown(keyWrapper.key.keycode);
    }

    @ZenMethod
    public static int getMouseX()
    {
        return MouseUtils.getMouseX();
    }

    @ZenMethod
    public static int getMouseY()
    {
        return MouseUtils.getMouseY();
    }

    @ZenMethod
    public static boolean isMouseDownLeft()
    {
        return MouseUtils.isMouseDownLeft();
    }

    @ZenMethod
    public static boolean isMouseDownRight()
    {
        return MouseUtils.isMouseDownRight();
    }
}
