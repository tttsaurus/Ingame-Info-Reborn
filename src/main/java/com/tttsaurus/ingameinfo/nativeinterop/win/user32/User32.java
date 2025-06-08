package com.tttsaurus.ingameinfo.nativeinterop.win.user32;

import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary
{
    User32 INSTANCE = Native.loadLibrary("user32", User32.class);

    boolean SetWindowTextW(WinDef.HWND hWnd, WString lpString);
}