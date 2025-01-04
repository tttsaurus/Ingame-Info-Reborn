package com.tttsaurus.ingameinfo.plugin.crt;

public enum Types
{
    Int(int.class),
    Long(long.class),
    Short(short.class),
    Byte(byte.class),
    Double(double.class),
    Float(float.class),
    Char(char.class),
    Boolean(boolean.class),
    String(String.class);

    public final Class<?> clazz;
    Types(Class<?> clazz)
    {
        this.clazz = clazz;
    }
}
