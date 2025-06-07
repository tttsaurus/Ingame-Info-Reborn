package com.tttsaurus.ingameinfo.experimental;

public class WindowsChecker
{
    public static boolean isWindows()
    {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
