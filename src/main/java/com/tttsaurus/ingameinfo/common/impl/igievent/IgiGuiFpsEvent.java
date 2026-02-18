package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class IgiGuiFpsEvent extends EventBase<Action2Param<Integer, Integer>>
{
    public void addListener(Action2Param<Integer, Integer> listener)
    {
        addListenerInternal(listener);
    }
}
