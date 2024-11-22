package com.tttsaurus.ingameinfo.common.api.element;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import java.awt.*;

public class Element
{
    protected final Rect rect = new Rect(0, 0, 0, 0);

    public Rect getRect() { return rect; }
    public void renderDebugRect() { RenderUtils.renderRectOutline(rect.x, rect.y, rect.width, rect.height, 1.0f, Color.RED.getRGB()); }

    public void onFixedUpdate(float time, float deltaTime)
    {

    }
    public void onRenderUpdate()
    {

    }
}
