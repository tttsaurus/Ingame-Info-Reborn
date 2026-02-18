package com.tttsaurus.ingameinfo.plugin.crt.impl.event;

import com.tttsaurus.ingameinfo.common.core.IgiRuntime;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IgiRuntimeEntryPointEvent;

public class McIgiRuntimeEntryPointEvent implements IgiRuntimeEntryPointEvent
{
    private final com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent event;

    public McIgiRuntimeEntryPointEvent(com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent event)
    {
        this.event = event;
    }

    @Override
    public boolean isCanceled()
    {
        return false;
    }

    @Override
    public void setCanceled(boolean canceled) { }

    @Override
    public IgiRuntime.InitPhaseEntry getInitPhase()
    {
        return event.runtime.initPhase;
    }

    @Override
    public IgiRuntime.LivePhaseEntry getLivePhase()
    {
        return event.runtime.livePhase;
    }
}
