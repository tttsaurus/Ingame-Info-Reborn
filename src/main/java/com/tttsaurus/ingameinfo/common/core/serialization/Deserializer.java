package com.tttsaurus.ingameinfo.common.core.serialization;

import javax.annotation.Nullable;

public interface Deserializer<T>
{
    @Nullable
    T deserialize(String raw);
}
