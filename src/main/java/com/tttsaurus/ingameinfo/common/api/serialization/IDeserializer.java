package com.tttsaurus.ingameinfo.common.api.serialization;

import javax.annotation.Nullable;

public interface IDeserializer<T>
{
    @Nullable
    T deserialize(String raw, String protocol);
}
