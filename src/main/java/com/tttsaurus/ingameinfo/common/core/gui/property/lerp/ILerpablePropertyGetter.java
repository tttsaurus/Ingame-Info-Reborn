package com.tttsaurus.ingameinfo.common.core.gui.property.lerp;

import com.tttsaurus.ingameinfo.common.core.gui.Element;

public interface ILerpablePropertyGetter
{
    LerpableProperty<?> get(Element target);
}
