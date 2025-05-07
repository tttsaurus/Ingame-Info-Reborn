package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.serialization.Deserializer;
import com.tttsaurus.ingameinfo.common.impl.serialization.SkewnessDeserializer;

@Deserializer(SkewnessDeserializer.class)
public enum Skewness
{
    NULL,
    LEFT,
    RIGHT
}
