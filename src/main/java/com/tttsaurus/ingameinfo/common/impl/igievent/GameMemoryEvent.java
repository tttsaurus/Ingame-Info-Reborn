package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class GameMemoryEvent extends EventBase<Action2Param<Long, Long>>
{
    public void addListener(Action2Param<Long, Long> listener)
    {
        addListenerInternal(listener);
    }
}
