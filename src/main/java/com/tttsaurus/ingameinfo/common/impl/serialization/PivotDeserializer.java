package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

public class PivotDeserializer implements IDeserializer<Pivot>
{
    @Override
    public Pivot deserialize(String raw, String protocol)
    {
        if (protocol.equals("json"))
        {
            try { return Pivot.valueOf(raw); }
            catch (Exception ignored) { return null; }
        }
        return null;
    }
}
