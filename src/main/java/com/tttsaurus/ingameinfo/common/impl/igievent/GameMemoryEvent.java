package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.api.internal.IAction_1Param;

public final class GameMemoryEvent extends EventBase<IAction_1Param<Long>>
{
    public void addListener(IAction_1Param<Long> listener)
    {
        addListenerInternal(listener);
    }
}
