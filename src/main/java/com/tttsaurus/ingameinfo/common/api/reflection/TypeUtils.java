package com.tttsaurus.ingameinfo.common.api.reflection;

public final class TypeUtils
{
    public static boolean looseTypeCheck(Class<?> clazz1, Class<?> clazz2)
    {
        if (isIntOrWrappedInt(clazz1) && isIntOrWrappedInt(clazz2))
            return true;
        else if (isLongOrWrappedLong(clazz1) && isLongOrWrappedLong(clazz2))
            return true;
        else if (isShortOrWrappedShort(clazz1) && isShortOrWrappedShort(clazz2))
            return true;
        else if (isByteOrWrappedByte(clazz1) && isByteOrWrappedByte(clazz2))
            return true;
        else if (isDoubleOrWrappedDouble(clazz1) && isDoubleOrWrappedDouble(clazz2))
            return true;
        else if (isFloatOrWrappedFloat(clazz1) && isFloatOrWrappedFloat(clazz2))
            return true;
        else if (isCharacterOrWrappedCharacter(clazz1) && isCharacterOrWrappedCharacter(clazz2))
            return true;
        else if (isBooleanOrWrappedBoolean(clazz1) && isBooleanOrWrappedBoolean(clazz2))
            return true;
        else return clazz1.getName().equals(clazz2.getName());
    }
    public static boolean isFromParentPackage(Class<?> clazz, String packageName)
    {
        return clazz.getName().startsWith(packageName);
    }
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
