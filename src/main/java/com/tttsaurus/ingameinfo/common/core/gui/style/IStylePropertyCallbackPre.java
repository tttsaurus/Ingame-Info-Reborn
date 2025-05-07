package com.tttsaurus.ingameinfo.common.core.gui.style;

import com.tttsaurus.ingameinfo.common.core.gui.Element;

public interface IStylePropertyCallbackPre
{
    void invoke(Element target, Object value, CallbackInfo callbackInfo);
    String name();
}
