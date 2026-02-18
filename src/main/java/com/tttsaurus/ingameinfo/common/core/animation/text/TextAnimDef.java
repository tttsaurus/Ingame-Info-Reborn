package com.tttsaurus.ingameinfo.common.core.animation.text;

import com.tttsaurus.ingameinfo.common.core.gui.property.style.wrapped.DoubleProperty;

public interface TextAnimDef
{
    void calcAnim(CharInfo[] charInfos, DoubleProperty timer, double deltaTime);
}
