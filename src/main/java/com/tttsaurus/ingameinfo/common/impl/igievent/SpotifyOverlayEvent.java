package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.api.internal.IAction_1Param;

public class SpotifyOverlayEvent extends EventBase<IAction_1Param<Boolean>>
{
    @Override
    public void addListener(IAction_1Param<Boolean> listener)
    {
        addListenerInternal(listener);
    }
}
