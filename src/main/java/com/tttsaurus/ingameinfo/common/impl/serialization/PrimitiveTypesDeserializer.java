package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

@SuppressWarnings("all")
public class PrimitiveTypesDeserializer<T> implements IDeserializer<T>
{
    private final Class<T> clazz;
    public PrimitiveTypesDeserializer(Class<T> clazz) { this.clazz = clazz; }

    @Override
    public T deserialize(String raw, String protocol)
    {
        //Class<T> clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (protocol.equals("json"))
        {
            // int
            if (clazz.getName().equals("int"))
                try { return (T)(Object)Integer.parseInt(raw); }
                catch (Exception ignored) { return (T)(Object)0; }
            // String
            else if (clazz.equals(String.class))
                try { return (T)(Object)raw; }
                catch (Exception ignored) { return (T)(Object)""; }
        }
        return null;
    }
}
