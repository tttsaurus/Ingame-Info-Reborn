package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

import com.tttsaurus.ingameinfo.common.core.gui.Element;

public interface LerpablePropertyGetter
{
    LerpableProperty<?> get(Element target);
}
