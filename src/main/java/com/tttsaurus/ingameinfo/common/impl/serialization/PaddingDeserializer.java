package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.layout.Padding;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;
import com.tttsaurus.ingameinfo.common.api.serialization.json.RawJsonUtils;

public class PaddingDeserializer implements IDeserializer<Padding>
{
    @Override
    public Padding deserialize(String raw)
    {
        String top = RawJsonUtils.extractValue(raw, "top");
        String bottom = RawJsonUtils.extractValue(raw, "bottom");
        String left = RawJsonUtils.extractValue(raw, "left");
        String right = RawJsonUtils.extractValue(raw, "right");
        Padding padding = new Padding(0, 0, 0, 0);

        if (!top.isEmpty())
            try { padding.top = Float.parseFloat(top); } catch (Exception ignored) { }
        if (!bottom.isEmpty())
            try { padding.bottom = Float.parseFloat(bottom); } catch (Exception ignored) { }
        if (!left.isEmpty())
            try { padding.left = Float.parseFloat(left); } catch (Exception ignored) { }
        if (!right.isEmpty())
            try { padding.right = Float.parseFloat(right); } catch (Exception ignored) { }
        return padding;
    }
}
