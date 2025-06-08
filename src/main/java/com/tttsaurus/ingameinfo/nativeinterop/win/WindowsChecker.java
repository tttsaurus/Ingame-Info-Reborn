package com.tttsaurus.ingameinfo.nativeinterop.win;

public class WindowsChecker
{
    public static boolean isWindows()
    {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
