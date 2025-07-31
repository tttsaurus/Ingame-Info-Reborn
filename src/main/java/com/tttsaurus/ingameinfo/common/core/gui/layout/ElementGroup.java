package com.tttsaurus.ingameinfo.common.core.gui.layout;

import com.tttsaurus.ingameinfo.common.core.gui.Element;
import com.tttsaurus.ingameinfo.common.core.gui.registry.RegisterElement;
import com.tttsaurus.ingameinfo.common.core.input.InputState;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.DebugRectOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import java.util.ArrayList;
import java.util.List;

@RegisterElement(constructable = false)
public abstract class ElementGroup extends Element
{
    public final List<Element> elements = new ArrayList<>();

    public void add(Element element)
    {
        InternalMethods.instance.Element$parent$setter.invoke(element, this);
        elements.add(element);
    }
    public void add(int index, Element element)
    {
        InternalMethods.instance.Element$parent$setter.invoke(element, this);
        elements.add(index, element);
    }

    @Override
    public void applyLogicTheme(ThemeConfig themeConfig)
    {
        super.applyLogicTheme(themeConfig);
        for (Element element: elements)
            element.applyLogicTheme(themeConfig);
    }

    @Override
    public void resetRenderInfo()
    {
        super.resetRenderInfo();
        for (Element element: elements)
            element.resetRenderInfo();
    }

    // how calcRenderPos() is implemented depends on the specific group types

    @Override
    public void calcWidthHeight()
    {
        for (Element element: elements)
        {
            element.calcWidthHeight();
            if (!element.enabled)
            {
                element.cachedWidth = element.rect.width;
                element.cachedHeight = element.rect.height;
                element.rect.width = 0f;
                element.rect.height = 0f;
            }
        }
    }

    @Override
    public void onFixedUpdate(double deltaTime)
    {
        if (!enabled) return;
        super.onFixedUpdate(deltaTime);
        for (Element element: elements)
            if (element.enabled)
                element.onFixedUpdate(deltaTime);
    }

    @Override
    public void onCollectLerpInfo()
    {
        super.onCollectLerpInfo();
        for (Element element: elements)
            element.onCollectLerpInfo();
    }

    @Override
    public void onRenderUpdate(RenderOpQueue queue, boolean focused)
    {
        if (!enabled) return;
        super.onRenderUpdate(queue, focused);
        for (Element element: elements)
            if (element.enabled)
                element.onRenderUpdate(queue, focused);
    }

    @Override
    public void onPropagateInput(InputState inputState)
    {
        if (!enabled) return;
        if (inputState.isConsumed()) return;
        super.onPropagateInput(inputState);
        for (Element element: elements)
        {
            if (element.enabled)
                element.onPropagateInput(inputState);
            if (inputState.isConsumed()) return;
        }
    }

    @Override
    public boolean getNeedReCalc()
    {
        if (super.getNeedReCalc()) return true;
        for (Element element: elements)
            if (element.getNeedReCalc()) return true;
        return false;
    }

    @Override
    public void finishReCalc()
    {
        super.finishReCalc();
        for (Element element: elements)
            element.finishReCalc();
    }

    @Override
    public void renderDebugRect(RenderOpQueue queue)
    {
        for (Element element: elements)
            element.renderDebugRect(queue);
        queue.enqueue(new DebugRectOp(true, rect, pivotPosX, pivotPosY));
    }
}
