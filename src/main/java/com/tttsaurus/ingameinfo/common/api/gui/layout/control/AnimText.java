package com.tttsaurus.ingameinfo.common.api.gui.layout.control;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.render.renderer.AnimTextRenderer;

public class AnimText extends Element
{
    private final AnimTextRenderer animTextRenderer = new AnimTextRenderer();
    private final ITextAnimDef animDef;

    public AnimText(String text, float scale, int color, ITextAnimDef animDef)
    {
        animTextRenderer.setText(text);
        animTextRenderer.setScale(scale);
        animTextRenderer.setColor(color);
        this.animDef = animDef;
    }

    @Override
    protected void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        animTextRenderer.setX(rect.x);
        animTextRenderer.setY(rect.y);
    }

    @Override
    protected void calcWidthHeight()
    {
        rect.width = animTextRenderer.simulateWidth();
        rect.height = animTextRenderer.simulateHeight();
    }

    @Override
    protected void onFixedUpdate(double deltaTime)
    {
        animDef.calcAnim(animTextRenderer.getCharacterInfos(), 0, deltaTime);
    }

    @Override
    protected void onRenderUpdate()
    {
        animTextRenderer.render();
    }
}
