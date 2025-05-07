package com.tttsaurus.ingameinfo.plugin.crt.impl.event;

import com.tttsaurus.ingameinfo.common.core.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiGuiInitEvent;

public class McIgiGuiInitEvent implements IIgiGuiInitEvent
{
    private final IgiGuiInitEvent event;

    public McIgiGuiInitEvent(IgiGuiInitEvent event)
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
}
