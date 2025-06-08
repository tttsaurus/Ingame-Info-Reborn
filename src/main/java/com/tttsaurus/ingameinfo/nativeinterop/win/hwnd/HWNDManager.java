package com.tttsaurus.ingameinfo.nativeinterop.win.hwnd;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

public class HWNDManager
{
    private static WinDef.HWND hwnd;

    public static WinDef.HWND getHWND() { return hwnd; }

    public static WinDef.HWND init(long hwndLong)
    {
        hwnd = new WinDef.HWND(Pointer.createConstant(hwndLong));
        return hwnd;
    }
}
