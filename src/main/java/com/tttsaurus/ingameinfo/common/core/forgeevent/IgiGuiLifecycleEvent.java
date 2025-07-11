package com.tttsaurus.ingameinfo.common.core.forgeevent;

import net.minecraftforge.fml.common.eventhandler.Event;

public class IgiGuiLifecycleEvent extends Event
{
    public final String lifecycleOwner;

    public IgiGuiLifecycleEvent(String lifecycleOwner)
    {
        this.lifecycleOwner = lifecycleOwner;
    }
}
