package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

import java.lang.reflect.Array;

@SuppressWarnings("all")
public final class LerpTargetCopyUtils
{
    public static Object copy(Object in)
    {
        Class<?> component = in.getClass().getComponentType();
        if (component == null)
            return copy((CopyableLerpTarget)in, in.getClass().asSubclass(CopyableLerpTarget.class));
        else
            return copy((CopyableLerpTarget[])in, component.asSubclass(CopyableLerpTarget.class));
    }

    private static <T extends CopyableLerpTarget> T copy(CopyableLerpTarget in, Class<T> clazz)
    {
        return (T)in.copy();
    }

    private static <T extends CopyableLerpTarget> T[] copy(CopyableLerpTarget[] in, Class<T> component)
    {
        T[] out = (T[])Array.newInstance(component, in.length);
        for (int i = 0; i < out.length; i++)
            out[i] = (T)in[i].copy();
        return out;
    }
}
