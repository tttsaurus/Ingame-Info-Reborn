package com.tttsaurus.ingameinfo.plugin.crt.impl;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("all")
@ZenRegister
@ZenClass("mods.ingameinfo.utils.CommonUtils")
public final class CommonUtils
{
    //<editor-fold desc="atomic utils">
    @ZenRegister
    @ZenClass("mods.ingameinfo.utils.AtomicBoolean")
    public static class AtomicBoolean
    {
        private final java.util.concurrent.atomic.AtomicBoolean atomicBoolean;

        public AtomicBoolean()
        {
            atomicBoolean = new java.util.concurrent.atomic.AtomicBoolean();
        }

        @ZenMethod
        public void set(boolean value)
        {
            atomicBoolean.set(value);
        }
        @ZenMethod
        public boolean get()
        {
            return atomicBoolean.get();
        }

        @ZenMethod("new")
        public static AtomicBoolean newAtomicBoolean(boolean value)
        {
            AtomicBoolean instance = new AtomicBoolean();
            instance.set(value);
            return instance;
        }
    }

    @ZenRegister
    @ZenClass("mods.ingameinfo.utils.AtomicInteger")
    public static class AtomicInteger
    {
        private final java.util.concurrent.atomic.AtomicInteger atomicInteger;

        public AtomicInteger()
        {
            atomicInteger = new java.util.concurrent.atomic.AtomicInteger();
        }

        @ZenMethod
        public void set(int value)
        {
            atomicInteger.set(value);
        }
        @ZenMethod
        public int get()
        {
            return atomicInteger.get();
        }

        @ZenMethod("new")
        public static AtomicInteger newAtomicInteger(int value)
        {
            AtomicInteger instance = new AtomicInteger();
            instance.set(value);
            return instance;
        }
    }
    //</editor-fold>

    //<editor-fold desc="align string">
    @ZenMethod
    public static String alignStringToRight(String value, int expectLength)
    {
        if (value.length() < expectLength)
        {
            StringBuilder builder = new StringBuilder();
            int compensate = expectLength - value.length();
            for (int i = 0; i < compensate; i++) builder.append(" ");
            builder.append(value);
            return builder.toString();
        }
        else
            return value;
    }

    @ZenMethod
    public static String alignStringToLeft(String value, int expectLength)
    {
        if (value.length() < expectLength)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(value);
            int compensate = expectLength - value.length();
            for (int i = 0; i < compensate; i++) builder.append(" ");
            return builder.toString();
        }
        else
            return value;
    }
    //</editor-fold>

    //<editor-fold desc="truncate">
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
    //</editor-fold>

    //<editor-fold desc="to int">
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
    //</editor-fold>

    //<editor-fold desc="to float">
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
    //</editor-fold>

    //<editor-fold desc="to string">
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
    //</editor-fold>

    //<editor-fold desc="to object">
    @ZenMethod
    public static Object toObject(Object value)
    {
        return value;
    }
    @ZenMethod
    public static Object toObject(String value)
    {
        return value;
    }
    @ZenMethod
    public static Object toObject(int value)
    {
        return (Integer)value;
    }
    @ZenMethod
    public static Object toObject(long value)
    {
        return (Long)value;
    }
    @ZenMethod
    public static Object toObject(short value)
    {
        return (Short)value;
    }
    @ZenMethod
    public static Object toObject(double value)
    {
        return (Double)value;
    }
    @ZenMethod
    public static Object toObject(float value)
    {
        return (Float)value;
    }
    @ZenMethod
    public static Object toObject(char value)
    {
        return (Character)value;
    }
    @ZenMethod
    public static Object toObject(boolean value)
    {
        return (Boolean)value;
    }
    //</editor-fold>
}
