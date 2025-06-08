package com.tttsaurus.ingameinfo.nativeinterop.win.imm;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;

public interface Imm32 extends StdCallLibrary
{
    Imm32 INSTANCE = Native.loadLibrary("imm32", Imm32.class);

    Pointer ImmGetContext(HWND hWnd);

    boolean ImmAssociateContext(HWND hWnd, Pointer hIMC);

    boolean ImmSetOpenStatus(Pointer hIMC, boolean fOpen);

    boolean ImmGetOpenStatus(Pointer hIMC);

    int ImmGetCompositionStringW(Pointer hIMC, int dwIndex, char[] lpBuf, int dwBufLen);

    boolean ImmReleaseContext(HWND hWnd, Pointer hIMC);

    int NI_COMPOSITIONSTR = 0x0015;
    int CPS_COMPLETE = 0x0001;

    boolean ImmNotifyIME(Pointer hIMC, int dwAction, int dwIndex, int dwValue);
}
