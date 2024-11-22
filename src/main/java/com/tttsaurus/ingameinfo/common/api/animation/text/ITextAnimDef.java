package com.tttsaurus.ingameinfo.common.api.animation.text;

import com.tttsaurus.ingameinfo.common.api.render.renderer.AnimTextRenderer;

public interface ITextAnimDef
{
    void calcAnim(AnimTextRenderer.CharInfo[] charInfos, float time, float deltaTime);
}
