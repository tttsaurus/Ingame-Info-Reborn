package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class GameMemoryEvent extends EventBase<IAction_2Param<Long, Long>>
{
    public void addListener(IAction_2Param<Long, Long> listener)
    {
        addListenerInternal(listener);
    }
}
