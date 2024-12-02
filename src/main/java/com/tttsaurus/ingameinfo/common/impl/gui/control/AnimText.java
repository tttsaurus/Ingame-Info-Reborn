package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.AnimTextRenderer;

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
    public void calcRenderPos(Rect contextRect)
    {
        super.calcRenderPos(contextRect);
        animTextRenderer.setX(rect.x);
        animTextRenderer.setY(rect.y);
    }

    @Override
    public void calcWidthHeight()
    {
        rect.width = animTextRenderer.simulateWidth();
        rect.height = animTextRenderer.simulateHeight();
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        animDef.calcAnim(animTextRenderer.getCharacterInfos(), 0, deltaTime);
    }

    @Override
    public void onRenderUpdate(boolean focused)
    {
        animTextRenderer.render();
    }
}
