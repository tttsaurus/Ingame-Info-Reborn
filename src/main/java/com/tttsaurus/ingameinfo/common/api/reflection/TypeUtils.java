package com.tttsaurus.ingameinfo.common.api.reflection;

public final class TypeUtils
{
    public static boolean isPrimitiveOrWrappedPrimitive(Class<?> clazz)
    {
        return clazz.isPrimitive() ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Short.class ||
                clazz == Byte.class ||
                clazz == Double.class ||
                clazz == Float.class ||
                clazz == Character.class ||
                clazz == Boolean.class;
    }
}
