package com.tttsaurus.ingameinfo.plugin.crt.impl.event;

import com.tttsaurus.ingameinfo.common.core.IgiRuntime;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiRuntimeEntryPointEvent;
import com.tttsaurus.ingameinfo.plugin.crt.api.event.IIgiRuntimeEntryPointEvent;

public class McIgiRuntimeEntryPointEvent implements IIgiRuntimeEntryPointEvent
{
    private final IgiRuntimeEntryPointEvent event;

    public McIgiRuntimeEntryPointEvent(IgiRuntimeEntryPointEvent event)
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
    public IgiRuntime.UnifiedEntry getUnifiedEntry()
    {
        return event.runtime.unifiedEntry;
    }
}
