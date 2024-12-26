package com.tttsaurus.ingameinfo.common.impl.event;

import com.tttsaurus.ingameinfo.common.api.event.EventBase;
import com.tttsaurus.ingameinfo.common.api.internal.IAction_1Param;

public final class IgiGuiFpsEvent extends EventBase
{
    public void addListener(IAction_1Param<Integer> action)
    {
        addListenerInternal(action);
    }
}
