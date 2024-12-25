package com.tttsaurus.ingameinfo.common.impl.serialization;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.serialization.IDeserializer;

public class GuiLayoutDeserializer implements IDeserializer<GuiLayout>
{
    @Override
    public GuiLayout deserialize(String raw, String protocol)
    {
        if (protocol.equals("ixml"))
        {

        }
        return null;
    }
}
