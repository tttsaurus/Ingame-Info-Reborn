package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.serialization.DeserializerSignature;
import com.tttsaurus.ingameinfo.common.impl.serialization.SkewnessDeserializer;

@DeserializerSignature(SkewnessDeserializer.class)
public enum Skewness
{
    NULL,
    LEFT,
    RIGHT
}
