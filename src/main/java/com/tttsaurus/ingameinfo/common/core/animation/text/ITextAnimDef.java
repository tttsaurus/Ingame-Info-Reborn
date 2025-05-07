package com.tttsaurus.ingameinfo.common.core.animation.text;

import com.tttsaurus.ingameinfo.common.impl.gui.style.wrapped.DoubleProperty;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.AnimTextRenderer;

public interface ITextAnimDef
{
    void calcAnim(AnimTextRenderer.CharInfo[] charInfos, DoubleProperty timer, double deltaTime);
}
