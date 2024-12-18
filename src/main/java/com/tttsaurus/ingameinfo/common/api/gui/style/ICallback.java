package com.tttsaurus.ingameinfo.common.api.gui.style;

import com.tttsaurus.ingameinfo.common.api.gui.Element;

public interface ICallback
{
    void invoke(Element target);
    String name();
}
