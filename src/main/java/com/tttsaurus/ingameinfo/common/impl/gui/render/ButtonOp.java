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

    public boolean hover;
    public boolean hold;

    public ButtonOp(Rect rect, int buttonColor, String text, float x, float y, float scale, int textColor, boolean shadow, boolean hover, boolean hold)
    {
        this.rect = rect;
        this.buttonColor = buttonColor;
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.textColor = textColor;
        this.shadow = shadow;
        this.hover = hover;
        this.hold = hold;
    }

    @Override
    public void execute(RenderContext context)
    {
        RenderUtils.renderNinePatchBorder(rect.x, rect.y, rect.width, rect.height, GuiResources.mcVanillaButton, buttonColor);
        if (hover && !hold)
            RenderUtils.renderRectBrightnessOverlay(rect.x, rect.y, rect.width, rect.height, 0.1f);
        RenderUtils.renderText(text, x, y, scale, textColor, shadow);
    }
}
