package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Skewness;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;

public class SkewnessDeserializer implements IDeserializer<Skewness>
{
    @Override
    public Skewness deserialize(String raw)
    {
        try { return Skewness.valueOf(raw); }
        catch (Exception ignored) { return null; }
    }
}
