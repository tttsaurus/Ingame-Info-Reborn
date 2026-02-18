package com.tttsaurus.ingameinfo.common.impl.igievent.modcompat;

import com.tttsaurus.ingameinfo.common.core.function.Action1Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class DeepResonanceEvent extends EventBase<Action1Param<Float>>
{
    @Override
    public void addListener(Action1Param<Float> listener)
    {
        addListenerInternal(listener);
    }
}
