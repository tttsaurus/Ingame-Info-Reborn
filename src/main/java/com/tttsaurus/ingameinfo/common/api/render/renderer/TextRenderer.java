package com.tttsaurus.ingameinfo.common.api.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;

import java.awt.*;

public class TextRenderer implements IRenderer
{
    public static final TextRenderer SHARED = new TextRenderer();

    public static final float DEFAULT_SCALE = 1f;
    public static final int DEFAULT_COLOR = Color.WHITE.getRGB();
    public static final boolean DEFAULT_SHADOW = true;

    protected float x = 0;
    protected float y = 0;
    protected float scale = DEFAULT_SCALE;
    protected int color = DEFAULT_COLOR;
    protected boolean shadow = DEFAULT_SHADOW;
    protected String text;

    //<editor-fold desc="getters & setters">
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public boolean getShadow() { return shadow; }
    public void setShadow(boolean shadow) { this.shadow = shadow; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    //</editor-fold>

    public float simulateWidth()
    {
        return RenderUtils.fontRenderer.getStringWidth(text) * scale;
    }
    public float simulateHeight()
    {
        return RenderUtils.fontRenderer.FONT_HEIGHT * scale;
    }

    public void render()
    {
        RenderUtils.renderText(text, x, y, scale, color, shadow);
    }
}
