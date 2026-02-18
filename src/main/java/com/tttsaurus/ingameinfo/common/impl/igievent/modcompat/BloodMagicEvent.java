package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class BloodMagicEvent extends EventBase<Action2Param<Integer, Integer>>
{
    @Override
    public void addListener(Action2Param<Integer, Integer> listener)
    {
        addListenerInternal(listener);
    }
}
