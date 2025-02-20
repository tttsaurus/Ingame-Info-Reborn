package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public final class SimpleDifficultyEvent extends EventBase<IAction_1Param<Integer>>
{
    @Override
    public void addListener(IAction_1Param<Integer> listener)
    {
        addListenerInternal(listener);
    }
}
