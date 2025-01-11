package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.api.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.api.function.IAction;

public final class SpotifyOverlayEditEvent extends EventBase<IAction>
{
    @Override
    public void addListener(IAction listener)
    {
        addListenerInternal(listener);
    }
}
