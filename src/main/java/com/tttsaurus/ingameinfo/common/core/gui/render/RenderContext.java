package com.tttsaurus.ingameinfo.common.core.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;

public class RenderContext
{
    public final ThemeConfig theme;
    public final float lerpAlpha;
    public final boolean polygonSmoothHint;
    public final boolean lineSmoothHint;

    public RenderContext(ThemeConfig theme, float lerpAlpha, boolean polygonSmoothHint, boolean lineSmoothHint)
    {
        this.theme = theme;
        this.lerpAlpha = lerpAlpha;
        this.polygonSmoothHint = polygonSmoothHint;
        this.lineSmoothHint = lineSmoothHint;
    }
}
