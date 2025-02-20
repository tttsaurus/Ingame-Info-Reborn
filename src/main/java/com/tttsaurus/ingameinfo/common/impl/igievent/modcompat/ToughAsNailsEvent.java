package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public final class ToughAsNailsEvent extends EventBase<IAction_2Param<Integer, Integer>>
{
    @Override
    public void addListener(IAction_2Param<Integer, Integer> listener)
    {
        addListenerInternal(listener);
    }
}
