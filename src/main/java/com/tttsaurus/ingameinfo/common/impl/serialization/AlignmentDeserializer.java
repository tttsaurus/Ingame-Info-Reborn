package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Alignment;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

public class AlignmentDeserializer implements IDeserializer<Alignment>
{
    @Override
    public Alignment deserialize(String raw, String protocol)
    {
        if (protocol.equals("json"))
        {
            try { return Alignment.valueOf(raw); }
            catch (Exception ignored) { return null; }
        }
        return null;
    }
}
