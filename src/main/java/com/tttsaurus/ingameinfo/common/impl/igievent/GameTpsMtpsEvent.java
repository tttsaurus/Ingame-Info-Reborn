package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public final class GameTpsMtpsEvent extends EventBase<IAction_2Param<Integer, Double>>
{
    @Override
    public void addListener(IAction_2Param<Integer, Double> listener)
    {
        addListenerInternal(listener);
    }
}
