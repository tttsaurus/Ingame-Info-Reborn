package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

import java.lang.reflect.Array;

@SuppressWarnings("all")
public final class LerpTargetCopyUtils
{
    public static Object copy(Object in)
    {
        Class<?> component = in.getClass().getComponentType();
        if (component == null)
            return copy((ICopyableLerpTarget)in, in.getClass().asSubclass(ICopyableLerpTarget.class));
        else
            return copy((ICopyableLerpTarget[])in, component.asSubclass(ICopyableLerpTarget.class));
    }

    private static <T extends ICopyableLerpTarget> T copy(ICopyableLerpTarget in, Class<T> clazz)
    {
        return (T)in.copy();
    }

    private static <T extends ICopyableLerpTarget> T[] copy(ICopyableLerpTarget[] in, Class<T> component)
    {
        T[] out = (T[])Array.newInstance(component, in.length);
        for (int i = 0; i < out.length; i++)
            out[i] = (T)in[i].copy();
        return out;
    }
}
