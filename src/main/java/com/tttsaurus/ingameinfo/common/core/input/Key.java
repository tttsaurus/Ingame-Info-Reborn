package com.tttsaurus.ingameinfo.common.core.input;

import org.lwjgl.input.Keyboard;

public enum Key
{
    KEY_ESCAPE(Keyboard.KEY_ESCAPE),
    KEY_1(Keyboard.KEY_1),
    KEY_2(Keyboard.KEY_2),
    KEY_3(Keyboard.KEY_3),
    KEY_4(Keyboard.KEY_4),
    KEY_5(Keyboard.KEY_5),
    KEY_6(Keyboard.KEY_6),
    KEY_7(Keyboard.KEY_7),
    KEY_8(Keyboard.KEY_8),
    KEY_9(Keyboard.KEY_9),
    KEY_0(Keyboard.KEY_0),
    KEY_MINUS(Keyboard.KEY_MINUS),
    KEY_EQUALS(Keyboard.KEY_EQUALS),
    KEY_BACK(Keyboard.KEY_BACK),
    KEY_TAB(Keyboard.KEY_TAB),
    KEY_Q(Keyboard.KEY_Q),
    KEY_W(Keyboard.KEY_W),
    KEY_E(Keyboard.KEY_E),
    KEY_R(Keyboard.KEY_R),
    KEY_T(Keyboard.KEY_T),
    KEY_Y(Keyboard.KEY_Y),
    KEY_U(Keyboard.KEY_U),
    KEY_I(Keyboard.KEY_I),
    KEY_O(Keyboard.KEY_O),
    KEY_P(Keyboard.KEY_P),
    KEY_LBRACKET(Keyboard.KEY_LBRACKET),
    KEY_RBRACKET(Keyboard.KEY_RBRACKET),
    KEY_RETURN(Keyboard.KEY_RETURN),
    KEY_LCONTROL(Keyboard.KEY_LCONTROL),
    KEY_A(Keyboard.KEY_A),
    KEY_S(Keyboard.KEY_S),
    KEY_D(Keyboard.KEY_D),
    KEY_F(Keyboard.KEY_F),
    KEY_G(Keyboard.KEY_G),
    KEY_H(Keyboard.KEY_H),
    KEY_J(Keyboard.KEY_J),
    KEY_K(Keyboard.KEY_K),
    KEY_L(Keyboard.KEY_L),
    KEY_SEMICOLON(Keyboard.KEY_SEMICOLON),
    KEY_APOSTROPHE(Keyboard.KEY_APOSTROPHE),
    KEY_GRAVE(Keyboard.KEY_GRAVE),
    KEY_LSHIFT(Keyboard.KEY_LSHIFT),
    KEY_BACKSLASH(Keyboard.KEY_BACKSLASH),
    KEY_Z(Keyboard.KEY_Z),
    KEY_X(Keyboard.KEY_X),
    KEY_C(Keyboard.KEY_C),
    KEY_V(Keyboard.KEY_V),
    KEY_B(Keyboard.KEY_B),
    KEY_N(Keyboard.KEY_N),
    KEY_M(Keyboard.KEY_M),
    KEY_COMMA(Keyboard.KEY_COMMA),
    KEY_PERIOD(Keyboard.KEY_PERIOD),
    KEY_SLASH(Keyboard.KEY_SLASH),
    KEY_RSHIFT(Keyboard.KEY_RSHIFT),
    KEY_MULTIPLY(Keyboard.KEY_MULTIPLY),
    KEY_LMENU(Keyboard.KEY_LMENU),
    KEY_SPACE(Keyboard.KEY_SPACE),
    KEY_CAPITAL(Keyboard.KEY_CAPITAL),
    KEY_F1(Keyboard.KEY_F1),
    KEY_F2(Keyboard.KEY_F2),
    KEY_F3(Keyboard.KEY_F3),
    KEY_F4(Keyboard.KEY_F4),
    KEY_F5(Keyboard.KEY_F5),
    KEY_F6(Keyboard.KEY_F6),
    KEY_F7(Keyboard.KEY_F7),
    KEY_F8(Keyboard.KEY_F8),
    KEY_F9(Keyboard.KEY_F9),
    KEY_F10(Keyboard.KEY_F10),
    KEY_NUMLOCK(Keyboard.KEY_NUMLOCK),
    KEY_SCROLL(Keyboard.KEY_SCROLL),
    KEY_NUMPAD7(Keyboard.KEY_NUMPAD7),
    KEY_NUMPAD8(Keyboard.KEY_NUMPAD8),
    KEY_NUMPAD9(Keyboard.KEY_NUMPAD9),
    KEY_SUBTRACT(Keyboard.KEY_SUBTRACT),
    KEY_NUMPAD4(Keyboard.KEY_NUMPAD4),
    KEY_NUMPAD5(Keyboard.KEY_NUMPAD5),
    KEY_NUMPAD6(Keyboard.KEY_NUMPAD6),
    KEY_ADD(Keyboard.KEY_ADD),
    KEY_NUMPAD1(Keyboard.KEY_NUMPAD1),
    KEY_NUMPAD2(Keyboard.KEY_NUMPAD2),
    KEY_NUMPAD3(Keyboard.KEY_NUMPAD3),
    KEY_NUMPAD0(Keyboard.KEY_NUMPAD0),
    KEY_DECIMAL(Keyboard.KEY_DECIMAL),
    KEY_F11(Keyboard.KEY_F11),
    KEY_F12(Keyboard.KEY_F12),
    KEY_F13(Keyboard.KEY_F13),
    KEY_F14(Keyboard.KEY_F14),
    KEY_F15(Keyboard.KEY_F15),
    KEY_F16(Keyboard.KEY_F16),
    KEY_F17(Keyboard.KEY_F17),
    KEY_F18(Keyboard.KEY_F18),
    KEY_KANA(Keyboard.KEY_KANA),
    KEY_F19(Keyboard.KEY_F19),
    KEY_CONVERT(Keyboard.KEY_CONVERT),
    KEY_NOCONVERT(Keyboard.KEY_NOCONVERT),
    KEY_YEN(Keyboard.KEY_YEN),
    KEY_NUMPADEQUALS(Keyboard.KEY_NUMPADEQUALS),
    KEY_CIRCUMFLEX(Keyboard.KEY_CIRCUMFLEX),
    KEY_AT(Keyboard.KEY_AT),
    KEY_COLON(Keyboard.KEY_COLON),
    KEY_UNDERLINE(Keyboard.KEY_UNDERLINE),
    KEY_KANJI(Keyboard.KEY_KANJI),
    KEY_STOP(Keyboard.KEY_STOP),
    KEY_AX(Keyboard.KEY_AX),
    KEY_UNLABELED(Keyboard.KEY_UNLABELED),
    KEY_NUMPADENTER(Keyboard.KEY_NUMPADENTER),
    KEY_RCONTROL(Keyboard.KEY_RCONTROL),
    KEY_SECTION(Keyboard.KEY_SECTION),
    KEY_NUMPADCOMMA(Keyboard.KEY_NUMPADCOMMA),
    KEY_DIVIDE(Keyboard.KEY_DIVIDE),
    KEY_SYSRQ(Keyboard.KEY_SYSRQ),
    KEY_RMENU(Keyboard.KEY_RMENU),
    KEY_FUNCTION(Keyboard.KEY_FUNCTION),
    KEY_PAUSE(Keyboard.KEY_PAUSE),
    KEY_HOME(Keyboard.KEY_HOME),
    KEY_UP(Keyboard.KEY_UP),
    KEY_PRIOR(Keyboard.KEY_PRIOR),
    KEY_LEFT(Keyboard.KEY_LEFT),
    KEY_RIGHT(Keyboard.KEY_RIGHT),
    KEY_END(Keyboard.KEY_END),
    KEY_NEXT(Keyboard.KEY_NEXT),
    KEY_INSERT(Keyboard.KEY_INSERT),
    KEY_DELETE(Keyboard.KEY_DELETE),
    KEY_CLEAR(Keyboard.KEY_CLEAR),
    KEY_LMETA(Keyboard.KEY_LMETA),
    KEY_RMETA(Keyboard.KEY_RMETA),
    KEY_APPS(Keyboard.KEY_APPS),
    KEY_POWER(Keyboard.KEY_POWER),
    KEY_SLEEP(Keyboard.KEY_SLEEP);

    public final int keycode;
    Key(int keycode)
    {
        this.keycode = keycode;
    }

    public Key parseKeycode(int keycode)
    {
        return switch (keycode)
        {
            case Keyboard.KEY_ESCAPE -> KEY_ESCAPE;
            case Keyboard.KEY_1 -> KEY_1;
            case Keyboard.KEY_2 -> KEY_2;
            case Keyboard.KEY_3 -> KEY_3;
            case Keyboard.KEY_4 -> KEY_4;
            case Keyboard.KEY_5 -> KEY_5;
            case Keyboard.KEY_6 -> KEY_6;
            case Keyboard.KEY_7 -> KEY_7;
            case Keyboard.KEY_8 -> KEY_8;
            case Keyboard.KEY_9 -> KEY_9;
            case Keyboard.KEY_0 -> KEY_0;
            case Keyboard.KEY_MINUS -> KEY_MINUS;
            case Keyboard.KEY_EQUALS -> KEY_EQUALS;
            case Keyboard.KEY_BACK -> KEY_BACK;
            case Keyboard.KEY_TAB -> KEY_TAB;
            case Keyboard.KEY_Q -> KEY_Q;
            case Keyboard.KEY_W -> KEY_W;
            case Keyboard.KEY_E -> KEY_E;
            case Keyboard.KEY_R -> KEY_R;
            case Keyboard.KEY_T -> KEY_T;
            case Keyboard.KEY_Y -> KEY_Y;
            case Keyboard.KEY_U -> KEY_U;
            case Keyboard.KEY_I -> KEY_I;
            case Keyboard.KEY_O -> KEY_O;
            case Keyboard.KEY_P -> KEY_P;
            case Keyboard.KEY_LBRACKET -> KEY_LBRACKET;
            case Keyboard.KEY_RBRACKET -> KEY_RBRACKET;
            case Keyboard.KEY_RETURN -> KEY_RETURN;
            case Keyboard.KEY_LCONTROL -> KEY_LCONTROL;
            case Keyboard.KEY_A -> KEY_A;
            case Keyboard.KEY_S -> KEY_S;
            case Keyboard.KEY_D -> KEY_D;
            case Keyboard.KEY_F -> KEY_F;
            case Keyboard.KEY_G -> KEY_G;
            case Keyboard.KEY_H -> KEY_H;
            case Keyboard.KEY_J -> KEY_J;
            case Keyboard.KEY_K -> KEY_K;
            case Keyboard.KEY_L -> KEY_L;
            case Keyboard.KEY_SEMICOLON -> KEY_SEMICOLON;
            case Keyboard.KEY_APOSTROPHE -> KEY_APOSTROPHE;
            case Keyboard.KEY_GRAVE -> KEY_GRAVE;
            case Keyboard.KEY_LSHIFT -> KEY_LSHIFT;
            case Keyboard.KEY_BACKSLASH -> KEY_BACKSLASH;
            case Keyboard.KEY_Z -> KEY_Z;
            case Keyboard.KEY_X -> KEY_X;
            case Keyboard.KEY_C -> KEY_C;
            case Keyboard.KEY_V -> KEY_V;
            case Keyboard.KEY_B -> KEY_B;
            case Keyboard.KEY_N -> KEY_N;
            case Keyboard.KEY_M -> KEY_M;
            case Keyboard.KEY_COMMA -> KEY_COMMA;
            case Keyboard.KEY_PERIOD -> KEY_PERIOD;
            case Keyboard.KEY_SLASH -> KEY_SLASH;
            case Keyboard.KEY_RSHIFT -> KEY_RSHIFT;
            case Keyboard.KEY_MULTIPLY -> KEY_MULTIPLY;
            case Keyboard.KEY_LMENU -> KEY_LMENU;
            case Keyboard.KEY_SPACE -> KEY_SPACE;
            case Keyboard.KEY_CAPITAL -> KEY_CAPITAL;
            case Keyboard.KEY_F1 -> KEY_F1;
            case Keyboard.KEY_F2 -> KEY_F2;
            case Keyboard.KEY_F3 -> KEY_F3;
            case Keyboard.KEY_F4 -> KEY_F4;
            case Keyboard.KEY_F5 -> KEY_F5;
            case Keyboard.KEY_F6 -> KEY_F6;
            case Keyboard.KEY_F7 -> KEY_F7;
            case Keyboard.KEY_F8 -> KEY_F8;
            case Keyboard.KEY_F9 -> KEY_F9;
            case Keyboard.KEY_F10 -> KEY_F10;
            case Keyboard.KEY_NUMLOCK -> KEY_NUMLOCK;
            case Keyboard.KEY_SCROLL -> KEY_SCROLL;
            case Keyboard.KEY_NUMPAD7 -> KEY_NUMPAD7;
            case Keyboard.KEY_NUMPAD8 -> KEY_NUMPAD8;
            case Keyboard.KEY_NUMPAD9 -> KEY_NUMPAD9;
            case Keyboard.KEY_SUBTRACT -> KEY_SUBTRACT;
            case Keyboard.KEY_NUMPAD4 -> KEY_NUMPAD4;
            case Keyboard.KEY_NUMPAD5 -> KEY_NUMPAD5;
            case Keyboard.KEY_NUMPAD6 -> KEY_NUMPAD6;
            case Keyboard.KEY_ADD -> KEY_ADD;
            case Keyboard.KEY_NUMPAD1 -> KEY_NUMPAD1;
            case Keyboard.KEY_NUMPAD2 -> KEY_NUMPAD2;
            case Keyboard.KEY_NUMPAD3 -> KEY_NUMPAD3;
            case Keyboard.KEY_NUMPAD0 -> KEY_NUMPAD0;
            case Keyboard.KEY_DECIMAL -> KEY_DECIMAL;
            case Keyboard.KEY_F11 -> KEY_F11;
            case Keyboard.KEY_F12 -> KEY_F12;
            case Keyboard.KEY_F13 -> KEY_F13;
            case Keyboard.KEY_F14 -> KEY_F14;
            case Keyboard.KEY_F15 -> KEY_F15;
            case Keyboard.KEY_F16 -> KEY_F16;
            case Keyboard.KEY_F17 -> KEY_F17;
            case Keyboard.KEY_F18 -> KEY_F18;
            case Keyboard.KEY_KANA -> KEY_KANA;
            case Keyboard.KEY_F19 -> KEY_F19;
            case Keyboard.KEY_CONVERT -> KEY_CONVERT;
            case Keyboard.KEY_NOCONVERT -> KEY_NOCONVERT;
            case Keyboard.KEY_YEN -> KEY_YEN;
            case Keyboard.KEY_NUMPADEQUALS -> KEY_NUMPADEQUALS;
            case Keyboard.KEY_CIRCUMFLEX -> KEY_CIRCUMFLEX;
            case Keyboard.KEY_AT -> KEY_AT;
            case Keyboard.KEY_COLON -> KEY_COLON;
            case Keyboard.KEY_UNDERLINE -> KEY_UNDERLINE;
            case Keyboard.KEY_KANJI -> KEY_KANJI;
            case Keyboard.KEY_STOP -> KEY_STOP;
            case Keyboard.KEY_AX -> KEY_AX;
            case Keyboard.KEY_UNLABELED -> KEY_UNLABELED;
            case Keyboard.KEY_NUMPADENTER -> KEY_NUMPADENTER;
            case Keyboard.KEY_RCONTROL -> KEY_RCONTROL;
            case Keyboard.KEY_SECTION -> KEY_SECTION;
            case Keyboard.KEY_NUMPADCOMMA -> KEY_NUMPADCOMMA;
            case Keyboard.KEY_DIVIDE -> KEY_DIVIDE;
            case Keyboard.KEY_SYSRQ -> KEY_SYSRQ;
            case Keyboard.KEY_RMENU -> KEY_RMENU;
            case Keyboard.KEY_FUNCTION -> KEY_FUNCTION;
            case Keyboard.KEY_PAUSE -> KEY_PAUSE;
            case Keyboard.KEY_HOME -> KEY_HOME;
            case Keyboard.KEY_UP -> KEY_UP;
            case Keyboard.KEY_PRIOR -> KEY_PRIOR;
            case Keyboard.KEY_LEFT -> KEY_LEFT;
            case Keyboard.KEY_RIGHT -> KEY_RIGHT;
            case Keyboard.KEY_END -> KEY_END;
            case Keyboard.KEY_NEXT -> KEY_NEXT;
            case Keyboard.KEY_INSERT -> KEY_INSERT;
            case Keyboard.KEY_DELETE -> KEY_DELETE;
            case Keyboard.KEY_CLEAR -> KEY_CLEAR;
            case Keyboard.KEY_LMETA -> KEY_LMETA;
            case Keyboard.KEY_RMETA -> KEY_RMETA;
            case Keyboard.KEY_APPS -> KEY_APPS;
            case Keyboard.KEY_POWER -> KEY_POWER;
            case Keyboard.KEY_SLEEP -> KEY_SLEEP;
            default -> throw new IllegalStateException("Unexpected value: " + keycode);
        };
    }
}
