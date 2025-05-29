package com.tttsaurus.ingameinfo.common.impl.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class ButtonOp implements IRenderOp
{
    public Rect rect;
    public int buttonColor;
    public String text;
    public float x, y, scale;
    public int textColor;
    public boolean shadow;

    public ButtonOp(Rect rect, int buttonColor, String text, float x, float y, float scale, int textColor, boolean shadow)
    {
        this.rect = rect;
        this.buttonColor = buttonColor;
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.textColor = textColor;
        this.shadow = shadow;
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderUtils.renderNinePatchBorderByPixel(rect.x, rect.y, rect.width, rect.height, GuiResources.mcVanillaButton, buttonColor);
        RenderUtils.renderText(text, x, y, scale, textColor, shadow);
    }
}
