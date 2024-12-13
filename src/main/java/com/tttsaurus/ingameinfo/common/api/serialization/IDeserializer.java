package com.tttsaurus.ingameinfo.common.api.serialization;

public interface IDeserializer<T>
{
    T deserialize(String raw);
}
