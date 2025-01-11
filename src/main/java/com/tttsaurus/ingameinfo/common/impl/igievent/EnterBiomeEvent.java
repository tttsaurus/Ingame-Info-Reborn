package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;

public class EnterBiomeEvent extends EventBase<IAction_2Param<String, String>>
{
    @Override
    public void addListener(IAction_2Param<String, String> listener)
    {
        addListenerInternal(listener);
    }
}
