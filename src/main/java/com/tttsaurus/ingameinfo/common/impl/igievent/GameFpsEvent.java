package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;

public final class GameFpsEvent extends EventBase<IAction_1Param<Integer>>
{
    public void addListener(IAction_1Param<Integer> listener)
    {
        addListenerInternal(listener);
    }
}
