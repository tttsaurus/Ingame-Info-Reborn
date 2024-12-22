package com.tttsaurus.ingameinfo.common.impl.gui.control;

import com.tttsaurus.ingameinfo.common.api.animation.text.ITextAnimDef;
import com.tttsaurus.ingameinfo.common.api.gui.Element;
import com.tttsaurus.ingameinfo.common.api.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.api.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.api.gui.style.StyleProperty;
import com.tttsaurus.ingameinfo.common.api.gui.style.StylePropertyCallback;
import com.tttsaurus.ingameinfo.common.impl.render.renderer.AnimTextRenderer;

@RegisterElement
public class AnimText extends Element
{
    private final AnimTextRenderer animTextRenderer = new AnimTextRenderer();

    @StyleProperty
    public ITextAnimDef animDef;

    @StylePropertyCallback
    public void setTextCallback()
    {
        animTextRenderer.setText(text);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setTextCallback")
    public String text;

    @StylePropertyCallback
    public void setScaleCallback()
    {
        animTextRenderer.setScale(scale);
        requestReCalc();
    }
    @StyleProperty(setterCallbackPost = "setScaleCallback")
    public float scale;

    @StylePropertyCallback
    public void setColorCallback()
    {
        animTextRenderer.setColor(color);
    }
    @StyleProperty(setterCallbackPost = "setColorCallback")
    public int color;

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
