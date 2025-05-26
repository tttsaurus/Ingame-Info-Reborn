package com.tttsaurus.ingameinfo.common.core.gui.property;

import com.tttsaurus.ingameinfo.common.core.gui.Element;

public interface IStylePropertyCallbackPost
{
    void invoke(Element target, Object value);
    String name();
}
