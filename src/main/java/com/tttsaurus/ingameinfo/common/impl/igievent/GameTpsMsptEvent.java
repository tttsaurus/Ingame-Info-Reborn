package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class GameTpsMsptEvent extends EventBase<IAction_2Param<Integer, Double>>
{
    @Override
    public void addListener(IAction_2Param<Integer, Double> listener)
    {
        addListenerInternal(listener);
    }
}
