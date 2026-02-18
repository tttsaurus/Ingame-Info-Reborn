package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class GameTpsMsptEvent extends EventBase<Action2Param<Integer, Double>>
{
    @Override
    public void addListener(Action2Param<Integer, Double> listener)
    {
        addListenerInternal(listener);
    }
}
