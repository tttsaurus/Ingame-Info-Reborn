package com.tttsaurus.ingameinfo.common.core.mvvm.context;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class SharedContext
{
    private final Map<ContextKey<?>, Object> data = new HashMap<>();

    public <T> void put(ContextKey<T> key, T value)
    {
        if (!key.clazz.isInstance(value)) return;
        data.put(key, value);
    }

    @Nullable
    public <T> T get(ContextKey<T> key)
    {
        Object obj = data.get(key);
        if (obj == null) return null;
        return (T)obj;
    }

    public int size()
    {
        return data.size();
    }
}
