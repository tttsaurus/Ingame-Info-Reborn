package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.Action;

public final class SpotifyOverlayEditEvent extends EventBase<Action>
{
    @Override
    public void addListener(Action listener)
    {
        addListenerInternal(listener);
    }
}
