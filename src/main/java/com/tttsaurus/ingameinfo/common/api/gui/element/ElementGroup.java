package com.tttsaurus.ingameinfo.common.api.gui.element;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementGroup extends Element
{
    protected final List<Element> elements = new ArrayList<>();

    public abstract void calcRect();

    @Override
    public void onFixedUpdate(float time, float deltaTime)
    {
        for (Element element: elements)
        {
            element.onFixedUpdate(time, deltaTime);
        }
    }
    @Override
    public void onRenderUpdate()
    {
        for (Element element: elements)
        {
            element.onRenderUpdate();
        }
    }
}
