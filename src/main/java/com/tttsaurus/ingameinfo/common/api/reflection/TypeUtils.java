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
    public static boolean isIntOrWrappedInt(Class<?> clazz)
    {
        return clazz.getName().equals("int") || clazz.equals(Integer.class);
    }
    public static boolean isLongOrWrappedLong(Class<?> clazz)
    {
        return clazz.getName().equals("long") || clazz.equals(Long.class);
    }
    public static boolean isShortOrWrappedShort(Class<?> clazz)
    {
        return clazz.getName().equals("short") || clazz.equals(Short.class);
    }
    public static boolean isByteOrWrappedByte(Class<?> clazz)
    {
        return clazz.getName().equals("byte") || clazz.equals(Byte.class);
    }
    public static boolean isDoubleOrWrappedDouble(Class<?> clazz)
    {
        return clazz.getName().equals("double") || clazz.equals(Double.class);
    }
    public static boolean isFloatOrWrappedFloat(Class<?> clazz)
    {
        return clazz.getName().equals("float") || clazz.equals(Float.class);
    }
    public static boolean isCharacterOrWrappedCharacter(Class<?> clazz)
    {
        return clazz.getName().equals("character") || clazz.equals(Character.class);
    }
    public static boolean isBooleanOrWrappedBoolean(Class<?> clazz)
    {
        return clazz.getName().equals("boolean") || clazz.equals(Boolean.class);
    }
}
