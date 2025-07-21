package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;

public class ItemDeserializer implements IDeserializer<GhostableItem>
{
    @Override
    public GhostableItem deserialize(String raw)
    {
        raw = raw.trim();
        if (raw.startsWith("item(") && raw.endsWith(")"))
            return new GhostableItem(raw.substring(5, raw.length() - 1).trim());
        return null;
    }
}
