package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.gui.layout.Pivot;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;

public class PivotDeserializer implements IDeserializer<Pivot>
{
    @Override
    public Pivot deserialize(String raw)
    {
        try { return Pivot.valueOf(raw); }
        catch (Exception ignored) { return null; }
    }
}
