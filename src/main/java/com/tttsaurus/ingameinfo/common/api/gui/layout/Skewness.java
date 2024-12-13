package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.SkewnessDeserializer;

@Deserializer(SkewnessDeserializer.class)
public enum Skewness
{
    NULL,
    LEFT,
    RIGHT
}
