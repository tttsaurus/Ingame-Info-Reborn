package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Skewness;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

public class SkewnessDeserializer implements IDeserializer<Skewness>
{
    @Override
    public Skewness deserialize(String raw, String protocol)
    {
        if (protocol.equals("json"))
        {
            try { return Skewness.valueOf(raw); }
            catch (Exception ignored) { return null; }
        }
        return null;
    }
}
