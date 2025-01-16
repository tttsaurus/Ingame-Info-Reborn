package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public class IgiGuiFboRefreshRateEvent extends EventBase<IAction_1Param<Float>>
{
    @Override
    public void addListener(IAction_1Param<Float> listener)
    {
        addListenerInternal(listener);
    }
}
