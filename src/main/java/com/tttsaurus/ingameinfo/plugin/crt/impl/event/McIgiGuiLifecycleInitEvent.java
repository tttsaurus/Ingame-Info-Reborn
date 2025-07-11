package com.tttsaurus.ingameinfo.plugin.crt.impl.event;

import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiGuiLifecycleInitEvent;

public class McIgiGuiLifecycleInitEvent implements IIgiGuiLifecycleInitEvent
{
    private final IgiGuiLifecycleInitEvent event;

    public McIgiGuiLifecycleInitEvent(IgiGuiLifecycleInitEvent event)
    {
        this.event = event;
    }

    @Override
    public boolean isCanceled()
    {
        return false;
    }

    @Override
    public void setCanceled(boolean canceled)
    {

    }

    @Override
    public String getLifecycleOwner()
    {
        return event.lifecycleOwner;
    }
}
