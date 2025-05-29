package com.tttsaurus.ingameinfo.common.core.animation.text;

import com.tttsaurus.ingameinfo.common.core.gui.property.wrapped.DoubleProperty;

public interface ITextAnimDef
{
    void calcAnim(CharInfo[] charInfos, DoubleProperty timer, double deltaTime);
}
