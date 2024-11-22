package com.tttsaurus.ingameinfo.common.api.gui.element;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.render.renderer.AnimTextRenderer;

public class AnimTextElement extends Element
{
    private final AnimTextRenderer animTextRenderer = new AnimTextRenderer();
    private final ITextAnimDef animDef;

    public AnimTextElement(String text, float scale, int color, ITextAnimDef animDef)
    {
        animTextRenderer.setText(text);
        animTextRenderer.setScale(scale);
        animTextRenderer.setColor(color);
        rect.width = animTextRenderer.simulateWidth();
        rect.height = animTextRenderer.simulateHeight();
        this.animDef = animDef;
    }

    @Override
    public void onFixedUpdate(float time, float deltaTime)
    {
        animDef.calcAnim(animTextRenderer.getCharacterInfos(), time, deltaTime);
    }

    @Override
    public void onRenderUpdate()
    {
        animTextRenderer.setX(rect.x);
        animTextRenderer.setY(rect.y);
        animTextRenderer.render();
    }
}
