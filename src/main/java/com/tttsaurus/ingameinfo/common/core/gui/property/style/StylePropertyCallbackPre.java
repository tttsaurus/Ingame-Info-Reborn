package com.tttsaurus.ingameinfo.common.core.gui.property.style;

import com.tttsaurus.ingameinfo.common.core.gui.Element;

public interface StylePropertyCallbackPre
{
    void invoke(Element target, Object value, CallbackInfo callbackInfo);
    String name();
}
