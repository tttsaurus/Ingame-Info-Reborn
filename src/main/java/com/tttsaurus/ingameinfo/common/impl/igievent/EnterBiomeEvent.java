package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.function.Action2Param;
import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;

public final class EnterBiomeEvent extends EventBase<Action2Param<String, String>>
{
    @Override
    public void addListener(Action2Param<String, String> listener)
    {
        addListenerInternal(listener);
    }
}
