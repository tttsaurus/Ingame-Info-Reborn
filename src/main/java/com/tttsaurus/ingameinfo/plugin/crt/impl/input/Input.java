package com.tttsaurus.ingameinfo.plugin.crt.impl.input;

import crafttweaker.annotations.ZenRegister;
import org.lwjgl.input.Keyboard;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import java.lang.reflect.Field;

@ZenRegister
@ZenClass("mods.ingameinfo.input.Input")
public final class Input
{
    @ZenMethod
    public static int getKeyCode(String constant)
    {
        int keycode = -1;
        try
        {
            Field field = Keyboard.class.getDeclaredField(constant);
            field.setAccessible(true);
            keycode = (int)field.get(null);
        }
        catch (Exception ignored) { }
        return keycode;
    }

    @ZenMethod
    public static boolean isKeyDown(int keycode)
    {
        return Keyboard.isKeyDown(keycode);
    }
}
