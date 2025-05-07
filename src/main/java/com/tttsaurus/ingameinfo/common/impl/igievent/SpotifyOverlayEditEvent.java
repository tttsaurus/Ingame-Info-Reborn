package com.tttsaurus.ingameinfo.common.impl.igievent;

import com.tttsaurus.ingameinfo.common.core.igievent.EventBase;
import com.tttsaurus.ingameinfo.common.core.function.IAction;

public final class SpotifyOverlayEditEvent extends EventBase<IAction>
{
    @Override
    public void addListener(IAction listener)
    {
        addListenerInternal(listener);
    }
}
