package com.tttsaurus.ingameinfo.common.api.gui.style;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public interface IStylePropertyCallbackPre
{
    void invoke(Element target, Object value, CallbackInfo callbackInfo);
    String name();
}
