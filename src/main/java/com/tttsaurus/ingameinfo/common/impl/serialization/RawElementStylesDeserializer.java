package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.api.serialization.ixml.RawIxmlUtils;
import com.tttsaurus.ingameinfo.common.api.serialization.json.RawJsonUtils;
import net.minecraft.util.Tuple;
import java.util.ArrayList;
import java.util.List;

public class RawElementStylesDeserializer implements IDeserializer<List<Tuple<String, String>>>
{
    @Override
    public List<Tuple<String, String>> deserialize(String raw, String protocol)
    {
        if (protocol.equals("ixml"))
        {
            return RawIxmlUtils.splitParams(raw);
        }
        else if (protocol.equals("json"))
        {
            List<Tuple<String, String>> list = new ArrayList<>();
            List<String> args = RawJsonUtils.extractKeys(raw);

            for (String arg: args)
                list.add(new Tuple<>(arg, RawJsonUtils.extractValue(raw, arg)));
            return list;
        }
        return new ArrayList<>();
    }
}
