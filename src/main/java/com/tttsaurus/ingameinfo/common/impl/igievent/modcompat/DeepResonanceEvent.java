package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.api.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public final class DeepResonanceEvent extends EventBase<IAction_1Param<Float>>
{
    @Override
    public void addListener(IAction_1Param<Float> listener)
    {
        addListenerInternal(listener);
    }
}
