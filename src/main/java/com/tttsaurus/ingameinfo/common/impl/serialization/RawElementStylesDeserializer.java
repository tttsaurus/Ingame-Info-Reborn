package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.core.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.core.serialization.ixml.RawIxmlUtils;
import net.minecraft.util.Tuple;
import java.util.List;

public class RawElementStylesDeserializer implements IDeserializer<List<Tuple<String, String>>>
{
    @Override
    public List<Tuple<String, String>> deserialize(String raw)
    {
        return RawIxmlUtils.splitParams(raw);
    }
}
