package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class SimpleDifficultyEvent extends EventBase<Action1Param<Integer>>
{
    @Override
    public void addListener(Action1Param<Integer> listener)
    {
        addListenerInternal(listener);
    }
}
