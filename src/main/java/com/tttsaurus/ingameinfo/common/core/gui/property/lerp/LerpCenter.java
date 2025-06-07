package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

public final class LerpCenter
{
    public static Mode mode = Mode.LINEAR;

    public enum Mode
    {
        LINEAR
    }

    public static float lerp(float i, float j, float a)
    {
        return switch (mode)
        {
            case LINEAR -> i + a * (j - i);
        };
    }
}
