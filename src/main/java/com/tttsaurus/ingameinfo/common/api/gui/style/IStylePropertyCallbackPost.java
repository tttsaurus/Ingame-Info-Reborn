package com.tttsaurus.ingameinfo.common.api.gui.style;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public interface IStylePropertyCallbackPost
{
    void invoke(Element target, Object value);
    String name();
}
