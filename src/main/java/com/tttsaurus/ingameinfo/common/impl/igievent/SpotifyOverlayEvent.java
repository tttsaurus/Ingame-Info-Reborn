package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;

public final class SpotifyOverlayEvent extends EventBase<IAction_1Param<Boolean>>
{
    @Override
    public void addListener(IAction_1Param<Boolean> listener)
    {
        addListenerInternal(listener);
    }
}
