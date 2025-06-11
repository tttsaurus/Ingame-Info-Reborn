package com.tttsaurus.ingameinfo.plugin.crt.impl;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("all")
@ZenRegister
@ZenClass("mods.ingameinfo.Utils")
public final class Utils
{
    @ZenMethod
    public static float truncateFloat(float value, int decimalPlaces)
    {
        float multiplier = (float)Math.pow(10, decimalPlaces);
        return (float)((int)(value * multiplier)) / multiplier;
    }

    @ZenMethod
    public static double truncateDouble(double value, int decimalPlaces)
    {
        double multiplier = Math.pow(10, decimalPlaces);
        return (long)(value * multiplier) / multiplier;
    }

    @ZenMethod
    public static int toInt(Object value)
    {
        Class<?> clazz = value.getClass();
        int output = 0;

        if (clazz == Integer.class)
            output = ((Integer)value).intValue();
        else if (clazz == Long.class)
            output = (int)((Long)value).longValue();
        else if (clazz == Short.class)
            output = (int)((Short)value).shortValue();
        else if (clazz == Double.class)
            output = (int)((Double)value).doubleValue();
        else if (clazz == Float.class)
            output = (int)((Float)value).floatValue();
        else if (clazz == Character.class)
            output = (int)((Character)value).charValue();
        else if (clazz == Boolean.class)
            output = ((Boolean)value).booleanValue() ? 1 : 0;
        else if (clazz == String.class)
            output = toInt((String)value);

        return output;
    }
    @ZenMethod
    public static int toInt(int value)
    {
        return value;
    }
    @ZenMethod
    public static int toInt(long value)
    {
        return (int)value;
    }
    @ZenMethod
    public static int toInt(short value)
    {
        return (int)value;
    }
    @ZenMethod
    public static int toInt(double value)
    {
        return (int)value;
    }
    @ZenMethod
    public static int toInt(float value)
    {
        return (int)value;
    }
    @ZenMethod
    public static int toInt(char value)
    {
        return (int)value;
    }
    @ZenMethod
    public static int toInt(boolean value)
    {
        return value ? 1 : 0;
    }
    @ZenMethod
    public static int toInt(String value)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ignored) { return 0; }
    }

    @ZenMethod
    public static float toFloat(Object value)
    {
        Class<?> clazz = value.getClass();
        float output = 0;

        if (clazz == Integer.class)
            output = (float)((Integer)value).intValue();
        else if (clazz == Long.class)
            output = (float)(int)((Long)value).longValue();
        else if (clazz == Short.class)
            output = (float)(int)((Short)value).shortValue();
        else if (clazz == Double.class)
            output = (float)((Double)value).doubleValue();
        else if (clazz == Float.class)
            output = ((Float)value).floatValue();
        else if (clazz == Character.class)
            output = (float)(int)((Character)value).charValue();
        else if (clazz == Boolean.class)
            output = ((Boolean)value).booleanValue() ? 1f : 0f;
        else if (clazz == String.class)
            output = toFloat((String)value);

        return output;
    }
    @ZenMethod
    public static float toFloat(int value)
    {
        return (float)value;
    }
    @ZenMethod
    public static float toFloat(long value)
    {
        return (float)(int)value;
    }
    @ZenMethod
    public static float toFloat(short value)
    {
        return (float)(int)value;
    }
    @ZenMethod
    public static float toFloat(double value)
    {
        return (float)value;
    }
    @ZenMethod
    public static float toFloat(float value)
    {
        return value;
    }
    @ZenMethod
    public static float toFloat(char value)
    {
        return (float)(int)value;
    }
    @ZenMethod
    public static float toFloat(boolean value)
    {
        return value ? 1f : 0f;
    }
    @ZenMethod
    public static float toFloat(String value)
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException ignored) { return 0f; }
    }

    @ZenMethod
    public static String toString(Object value)
    {
        return value.toString();
    }
    @ZenMethod
    public static String toString(int value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(long value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(short value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(double value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(float value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(char value)
    {
        return String.valueOf(value);
    }
    @ZenMethod
    public static String toString(boolean value)
    {
        return value ? "true" : "false";
    }
}
