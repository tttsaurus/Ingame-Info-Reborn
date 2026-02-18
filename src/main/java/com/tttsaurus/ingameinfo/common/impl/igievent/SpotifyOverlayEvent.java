package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.Action1Param;

public final class SpotifyOverlayEvent extends EventBase<Action1Param<Boolean>>
{
    @Override
    public void addListener(Action1Param<Boolean> listener)
    {
        addListenerInternal(listener);
    }
}
