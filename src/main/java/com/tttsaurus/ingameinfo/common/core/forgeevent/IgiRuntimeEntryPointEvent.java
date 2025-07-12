package com.tttsaurus.ingameinfo.common.core.forgeevent;

import com.tttsaurus.ingameinfo.common.core.IgiRuntime;
import net.minecraftforge.fml.common.eventhandler.Event;

public class IgiRuntimeEntryPointEvent extends Event
{
    public final IgiRuntime runtime;

    public IgiRuntimeEntryPointEvent(IgiRuntime runtime)
    {
        this.runtime = runtime;
    }
}
