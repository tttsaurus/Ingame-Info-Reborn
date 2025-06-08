package com.tttsaurus.ingameinfo.nativeinterop.win.hwnd;

import org.lwjgl.opengl.Display;
import java.lang.reflect.Field;

public class HWNDGetterLwjgl2
{
    public static long getHWND()
    {
        try
        {
            Class<?> displayClass = Display.class;
            Field implementationField = displayClass.getDeclaredField("display_impl");
            implementationField.setAccessible(true);

            Object windowsDisplay = implementationField.get(null);

            Class<?> windowsDisplayClass = windowsDisplay.getClass();
            Field hwndField = windowsDisplayClass.getDeclaredField("hwnd");
            hwndField.setAccessible(true);

            return hwndField.getLong(windowsDisplay);
        }
        catch (Exception ignored) { return -1; }
    }
}
