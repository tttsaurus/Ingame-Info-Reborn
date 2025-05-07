package com.tttsaurus.ingameinfo.common.core.serialization;

import javax.annotation.Nullable;

public interface IDeserializer<T>
{
    @Nullable
    T deserialize(String raw);
}
