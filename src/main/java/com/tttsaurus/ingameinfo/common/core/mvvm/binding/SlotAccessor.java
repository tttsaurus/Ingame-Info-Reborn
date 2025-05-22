package com.tttsaurus.ingameinfo.common.core.mvvm.binding;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.gui.layout.ElementGroup;

public class SlotAccessor
{
    // the slot group
    protected ElementGroup group;

    public void setComposeBlock()
    {
        if (group == null)
            throw new IllegalStateException("Internal object is null. The binding probably failed.");

        InGameInfoReborn.logger.info("set compose block");
    }
}
