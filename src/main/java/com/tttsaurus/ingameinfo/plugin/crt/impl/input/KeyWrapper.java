package com.tttsaurus.ingameinfo.plugin.crt.impl.input;

import com.tttsaurus.ingameinfo.common.core.input.Key;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenRegister
@ZenClass("mods.ingameinfo.input.Key")
public class KeyWrapper
{
    public final Key key;
    private KeyWrapper(Key key)
    {
        this.key = key;
    }

    @ZenProperty
    public static final KeyWrapper KEY_ESCAPE = new KeyWrapper(Key.KEY_ESCAPE);

    @ZenProperty
    public static final KeyWrapper KEY_1 = new KeyWrapper(Key.KEY_1);

    @ZenProperty
    public static final KeyWrapper KEY_2 = new KeyWrapper(Key.KEY_2);

    @ZenProperty
    public static final KeyWrapper KEY_3 = new KeyWrapper(Key.KEY_3);

    @ZenProperty
    public static final KeyWrapper KEY_4 = new KeyWrapper(Key.KEY_4);

    @ZenProperty
    public static final KeyWrapper KEY_5 = new KeyWrapper(Key.KEY_5);

    @ZenProperty
    public static final KeyWrapper KEY_6 = new KeyWrapper(Key.KEY_6);

    @ZenProperty
    public static final KeyWrapper KEY_7 = new KeyWrapper(Key.KEY_7);

    @ZenProperty
    public static final KeyWrapper KEY_8 = new KeyWrapper(Key.KEY_8);

    @ZenProperty
    public static final KeyWrapper KEY_9 = new KeyWrapper(Key.KEY_9);

    @ZenProperty
    public static final KeyWrapper KEY_0 = new KeyWrapper(Key.KEY_0);

    @ZenProperty
    public static final KeyWrapper KEY_MINUS = new KeyWrapper(Key.KEY_MINUS);

    @ZenProperty
    public static final KeyWrapper KEY_EQUALS = new KeyWrapper(Key.KEY_EQUALS);

    @ZenProperty
    public static final KeyWrapper KEY_BACK = new KeyWrapper(Key.KEY_BACK);

    @ZenProperty
    public static final KeyWrapper KEY_TAB = new KeyWrapper(Key.KEY_TAB);

    @ZenProperty
    public static final KeyWrapper KEY_Q = new KeyWrapper(Key.KEY_Q);

    @ZenProperty
    public static final KeyWrapper KEY_W = new KeyWrapper(Key.KEY_W);

    @ZenProperty
    public static final KeyWrapper KEY_E = new KeyWrapper(Key.KEY_E);

    @ZenProperty
    public static final KeyWrapper KEY_R = new KeyWrapper(Key.KEY_R);

    @ZenProperty
    public static final KeyWrapper KEY_T = new KeyWrapper(Key.KEY_T);

    @ZenProperty
    public static final KeyWrapper KEY_Y = new KeyWrapper(Key.KEY_Y);

    @ZenProperty
    public static final KeyWrapper KEY_U = new KeyWrapper(Key.KEY_U);

    @ZenProperty
    public static final KeyWrapper KEY_I = new KeyWrapper(Key.KEY_I);

    @ZenProperty
    public static final KeyWrapper KEY_O = new KeyWrapper(Key.KEY_O);

    @ZenProperty
    public static final KeyWrapper KEY_P = new KeyWrapper(Key.KEY_P);

    @ZenProperty
    public static final KeyWrapper KEY_LBRACKET = new KeyWrapper(Key.KEY_LBRACKET);

    @ZenProperty
    public static final KeyWrapper KEY_RBRACKET = new KeyWrapper(Key.KEY_RBRACKET);

    @ZenProperty
    public static final KeyWrapper KEY_RETURN = new KeyWrapper(Key.KEY_RETURN);

    @ZenProperty
    public static final KeyWrapper KEY_LCONTROL = new KeyWrapper(Key.KEY_LCONTROL);

    @ZenProperty
    public static final KeyWrapper KEY_A = new KeyWrapper(Key.KEY_A);

    @ZenProperty
    public static final KeyWrapper KEY_S = new KeyWrapper(Key.KEY_S);

    @ZenProperty
    public static final KeyWrapper KEY_D = new KeyWrapper(Key.KEY_D);

    @ZenProperty
    public static final KeyWrapper KEY_F = new KeyWrapper(Key.KEY_F);

    @ZenProperty
    public static final KeyWrapper KEY_G = new KeyWrapper(Key.KEY_G);

    @ZenProperty
    public static final KeyWrapper KEY_H = new KeyWrapper(Key.KEY_H);

    @ZenProperty
    public static final KeyWrapper KEY_J = new KeyWrapper(Key.KEY_J);

    @ZenProperty
    public static final KeyWrapper KEY_K = new KeyWrapper(Key.KEY_K);

    @ZenProperty
    public static final KeyWrapper KEY_L = new KeyWrapper(Key.KEY_L);

    @ZenProperty
    public static final KeyWrapper KEY_SEMICOLON = new KeyWrapper(Key.KEY_SEMICOLON);

    @ZenProperty
    public static final KeyWrapper KEY_APOSTROPHE = new KeyWrapper(Key.KEY_APOSTROPHE);

    @ZenProperty
    public static final KeyWrapper KEY_GRAVE = new KeyWrapper(Key.KEY_GRAVE);

    @ZenProperty
    public static final KeyWrapper KEY_LSHIFT = new KeyWrapper(Key.KEY_LSHIFT);

    @ZenProperty
    public static final KeyWrapper KEY_BACKSLASH = new KeyWrapper(Key.KEY_BACKSLASH);

    @ZenProperty
    public static final KeyWrapper KEY_Z = new KeyWrapper(Key.KEY_Z);

    @ZenProperty
    public static final KeyWrapper KEY_X = new KeyWrapper(Key.KEY_X);

    @ZenProperty
    public static final KeyWrapper KEY_C = new KeyWrapper(Key.KEY_C);

    @ZenProperty
    public static final KeyWrapper KEY_V = new KeyWrapper(Key.KEY_V);

    @ZenProperty
    public static final KeyWrapper KEY_B = new KeyWrapper(Key.KEY_B);

    @ZenProperty
    public static final KeyWrapper KEY_N = new KeyWrapper(Key.KEY_N);

    @ZenProperty
    public static final KeyWrapper KEY_M = new KeyWrapper(Key.KEY_M);

    @ZenProperty
    public static final KeyWrapper KEY_COMMA = new KeyWrapper(Key.KEY_COMMA);

    @ZenProperty
    public static final KeyWrapper KEY_PERIOD = new KeyWrapper(Key.KEY_PERIOD);

    @ZenProperty
    public static final KeyWrapper KEY_SLASH = new KeyWrapper(Key.KEY_SLASH);

    @ZenProperty
    public static final KeyWrapper KEY_RSHIFT = new KeyWrapper(Key.KEY_RSHIFT);

    @ZenProperty
    public static final KeyWrapper KEY_MULTIPLY = new KeyWrapper(Key.KEY_MULTIPLY);

    @ZenProperty
    public static final KeyWrapper KEY_LMENU = new KeyWrapper(Key.KEY_LMENU);

    @ZenProperty
    public static final KeyWrapper KEY_SPACE = new KeyWrapper(Key.KEY_SPACE);

    @ZenProperty
    public static final KeyWrapper KEY_CAPITAL = new KeyWrapper(Key.KEY_CAPITAL);

    @ZenProperty
    public static final KeyWrapper KEY_F1 = new KeyWrapper(Key.KEY_F1);

    @ZenProperty
    public static final KeyWrapper KEY_F2 = new KeyWrapper(Key.KEY_F2);

    @ZenProperty
    public static final KeyWrapper KEY_F3 = new KeyWrapper(Key.KEY_F3);

    @ZenProperty
    public static final KeyWrapper KEY_F4 = new KeyWrapper(Key.KEY_F4);

    @ZenProperty
    public static final KeyWrapper KEY_F5 = new KeyWrapper(Key.KEY_F5);

    @ZenProperty
    public static final KeyWrapper KEY_F6 = new KeyWrapper(Key.KEY_F6);

    @ZenProperty
    public static final KeyWrapper KEY_F7 = new KeyWrapper(Key.KEY_F7);

    @ZenProperty
    public static final KeyWrapper KEY_F8 = new KeyWrapper(Key.KEY_F8);

    @ZenProperty
    public static final KeyWrapper KEY_F9 = new KeyWrapper(Key.KEY_F9);

    @ZenProperty
    public static final KeyWrapper KEY_F10 = new KeyWrapper(Key.KEY_F10);

    @ZenProperty
    public static final KeyWrapper KEY_NUMLOCK = new KeyWrapper(Key.KEY_NUMLOCK);

    @ZenProperty
    public static final KeyWrapper KEY_SCROLL = new KeyWrapper(Key.KEY_SCROLL);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD7 = new KeyWrapper(Key.KEY_NUMPAD7);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD8 = new KeyWrapper(Key.KEY_NUMPAD8);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD9 = new KeyWrapper(Key.KEY_NUMPAD9);

    @ZenProperty
    public static final KeyWrapper KEY_SUBTRACT = new KeyWrapper(Key.KEY_SUBTRACT);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD4 = new KeyWrapper(Key.KEY_NUMPAD4);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD5 = new KeyWrapper(Key.KEY_NUMPAD5);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD6 = new KeyWrapper(Key.KEY_NUMPAD6);

    @ZenProperty
    public static final KeyWrapper KEY_ADD = new KeyWrapper(Key.KEY_ADD);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD1 = new KeyWrapper(Key.KEY_NUMPAD1);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD2 = new KeyWrapper(Key.KEY_NUMPAD2);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD3 = new KeyWrapper(Key.KEY_NUMPAD3);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPAD0 = new KeyWrapper(Key.KEY_NUMPAD0);

    @ZenProperty
    public static final KeyWrapper KEY_DECIMAL = new KeyWrapper(Key.KEY_DECIMAL);

    @ZenProperty
    public static final KeyWrapper KEY_F11 = new KeyWrapper(Key.KEY_F11);

    @ZenProperty
    public static final KeyWrapper KEY_F12 = new KeyWrapper(Key.KEY_F12);

    @ZenProperty
    public static final KeyWrapper KEY_F13 = new KeyWrapper(Key.KEY_F13);

    @ZenProperty
    public static final KeyWrapper KEY_F14 = new KeyWrapper(Key.KEY_F14);

    @ZenProperty
    public static final KeyWrapper KEY_F15 = new KeyWrapper(Key.KEY_F15);

    @ZenProperty
    public static final KeyWrapper KEY_F16 = new KeyWrapper(Key.KEY_F16);

    @ZenProperty
    public static final KeyWrapper KEY_F17 = new KeyWrapper(Key.KEY_F17);

    @ZenProperty
    public static final KeyWrapper KEY_F18 = new KeyWrapper(Key.KEY_F18);

    @ZenProperty
    public static final KeyWrapper KEY_KANA = new KeyWrapper(Key.KEY_KANA);

    @ZenProperty
    public static final KeyWrapper KEY_F19 = new KeyWrapper(Key.KEY_F19);

    @ZenProperty
    public static final KeyWrapper KEY_CONVERT = new KeyWrapper(Key.KEY_CONVERT);

    @ZenProperty
    public static final KeyWrapper KEY_NOCONVERT = new KeyWrapper(Key.KEY_NOCONVERT);

    @ZenProperty
    public static final KeyWrapper KEY_YEN = new KeyWrapper(Key.KEY_YEN);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPADEQUALS = new KeyWrapper(Key.KEY_NUMPADEQUALS);

    @ZenProperty
    public static final KeyWrapper KEY_CIRCUMFLEX = new KeyWrapper(Key.KEY_CIRCUMFLEX);

    @ZenProperty
    public static final KeyWrapper KEY_AT = new KeyWrapper(Key.KEY_AT);

    @ZenProperty
    public static final KeyWrapper KEY_COLON = new KeyWrapper(Key.KEY_COLON);

    @ZenProperty
    public static final KeyWrapper KEY_UNDERLINE = new KeyWrapper(Key.KEY_UNDERLINE);

    @ZenProperty
    public static final KeyWrapper KEY_KANJI = new KeyWrapper(Key.KEY_KANJI);

    @ZenProperty
    public static final KeyWrapper KEY_STOP = new KeyWrapper(Key.KEY_STOP);

    @ZenProperty
    public static final KeyWrapper KEY_AX = new KeyWrapper(Key.KEY_AX);

    @ZenProperty
    public static final KeyWrapper KEY_UNLABELED = new KeyWrapper(Key.KEY_UNLABELED);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPADENTER = new KeyWrapper(Key.KEY_NUMPADENTER);

    @ZenProperty
    public static final KeyWrapper KEY_RCONTROL = new KeyWrapper(Key.KEY_RCONTROL);

    @ZenProperty
    public static final KeyWrapper KEY_SECTION = new KeyWrapper(Key.KEY_SECTION);

    @ZenProperty
    public static final KeyWrapper KEY_NUMPADCOMMA = new KeyWrapper(Key.KEY_NUMPADCOMMA);

    @ZenProperty
    public static final KeyWrapper KEY_DIVIDE = new KeyWrapper(Key.KEY_DIVIDE);

    @ZenProperty
    public static final KeyWrapper KEY_SYSRQ = new KeyWrapper(Key.KEY_SYSRQ);

    @ZenProperty
    public static final KeyWrapper KEY_RMENU = new KeyWrapper(Key.KEY_RMENU);

    @ZenProperty
    public static final KeyWrapper KEY_FUNCTION = new KeyWrapper(Key.KEY_FUNCTION);

    @ZenProperty
    public static final KeyWrapper KEY_PAUSE = new KeyWrapper(Key.KEY_PAUSE);

    @ZenProperty
    public static final KeyWrapper KEY_HOME = new KeyWrapper(Key.KEY_HOME);

    @ZenProperty
    public static final KeyWrapper KEY_UP = new KeyWrapper(Key.KEY_UP);

    @ZenProperty
    public static final KeyWrapper KEY_PRIOR = new KeyWrapper(Key.KEY_PRIOR);

    @ZenProperty
    public static final KeyWrapper KEY_LEFT = new KeyWrapper(Key.KEY_LEFT);

    @ZenProperty
    public static final KeyWrapper KEY_RIGHT = new KeyWrapper(Key.KEY_RIGHT);

    @ZenProperty
    public static final KeyWrapper KEY_END = new KeyWrapper(Key.KEY_END);

    @ZenProperty
    public static final KeyWrapper KEY_NEXT = new KeyWrapper(Key.KEY_NEXT);

    @ZenProperty
    public static final KeyWrapper KEY_INSERT = new KeyWrapper(Key.KEY_INSERT);

    @ZenProperty
    public static final KeyWrapper KEY_DELETE = new KeyWrapper(Key.KEY_DELETE);

    @ZenProperty
    public static final KeyWrapper KEY_CLEAR = new KeyWrapper(Key.KEY_CLEAR);

    @ZenProperty
    public static final KeyWrapper KEY_LMETA = new KeyWrapper(Key.KEY_LMETA);

    @ZenProperty
    public static final KeyWrapper KEY_RMETA = new KeyWrapper(Key.KEY_RMETA);

    @ZenProperty
    public static final KeyWrapper KEY_APPS = new KeyWrapper(Key.KEY_APPS);

    @ZenProperty
    public static final KeyWrapper KEY_POWER = new KeyWrapper(Key.KEY_POWER);

    @ZenProperty
    public static final KeyWrapper KEY_SLEEP = new KeyWrapper(Key.KEY_SLEEP);
}
