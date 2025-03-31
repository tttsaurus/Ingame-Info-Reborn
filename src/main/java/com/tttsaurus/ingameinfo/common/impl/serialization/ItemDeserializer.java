package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

public class ItemDeserializer implements IDeserializer<GhostableItem>
{
    @Override
    public GhostableItem deserialize(String raw)
    {
        raw = raw.trim();
        if (raw.startsWith("(") && raw.endsWith(")"))
            return new GhostableItem(raw.substring(1, raw.length() - 1).trim());
        return null;
    }
}
