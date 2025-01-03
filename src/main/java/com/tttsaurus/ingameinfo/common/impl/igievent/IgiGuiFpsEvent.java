package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.api.internal.IAction_1Param;

public final class IgiGuiFpsEvent extends EventBase
{
    public void addListener(IAction_1Param<Integer> action)
    {
        addListenerInternal(action);
    }
}
