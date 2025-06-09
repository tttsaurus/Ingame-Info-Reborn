package com.tttsaurus.ingameinfo.common.core.mvvm.context;

public class ContextKey<T>
{
    public final String key;
    public Class<T> clazz;

    public ContextKey(String key, Class<T> clazz)
    {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof ContextKey<?> other)) return false;
        return key.equals(other.key) && clazz.equals(other.clazz);
    }

    @Override
    public int hashCode()
    {
        return 31 * key.hashCode() + clazz.hashCode();
    }

    @Override
    public String toString()
    {
        return "ContextKey[" + key + ", " + clazz.getSimpleName() + "]";
    }
}
