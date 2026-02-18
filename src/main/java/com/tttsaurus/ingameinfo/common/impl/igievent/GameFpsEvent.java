package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.Action1Param;

public final class GameFpsEvent extends EventBase<Action1Param<Integer>>
{
    public void addListener(Action1Param<Integer> listener)
    {
        addListenerInternal(listener);
    }
}
