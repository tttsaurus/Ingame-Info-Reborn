package com.tttsaurus.ingameinfo.common.impl.gui.render;

import com.tttsaurus.ingameinfo.common.core.gui.GuiResources;
import com.tttsaurus.ingameinfo.common.core.gui.layout.Rect;
import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import java.awt.*;

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

    private static final float ORIGINAL_R;
    private static final float ORIGINAL_G;
    private static final float ORIGINAL_B;

    static
    {
        String originalColorString = "aaaaaa";
        int originalColor = 0xff000000 | Integer.parseInt(originalColorString, 16);
        ORIGINAL_R = (float)(originalColor >> 16 & 255) / 255f;
        ORIGINAL_G = (float)(originalColor >> 8 & 255) / 255f;
        ORIGINAL_B = (float)(originalColor & 255) / 255f;
    }

    @Override
    public void execute(RenderContext context)
    {
        float a = (float)(buttonColor >> 24 & 255) / 255f;
        float r = (float)(buttonColor >> 16 & 255) / 255f;
        float g = (float)(buttonColor >> 8 & 255) / 255f;
        float b = (float)(buttonColor & 255) / 255f;

        float _r = r / ORIGINAL_R;
        float _g = g / ORIGINAL_G;
        float _b = b / ORIGINAL_B;

        int tempColor = (new Color(Math.min(_r, 1f), Math.min(_g, 1f), Math.min(_b, 1f), a)).getRGB();

        RenderUtils.renderImagePrefab(rect.x, rect.y, rect.width, rect.height, GuiResources.mcVanillaButton, tempColor);

        RenderUtils.renderRectBrightnessOverlay(rect.x, rect.y, rect.width, rect.height,
                _r > 1f ? r - ORIGINAL_R : 0f,
                _g > 1f ? g - ORIGINAL_G : 0f,
                _b > 1f ? b - ORIGINAL_B : 0f);

        RenderUtils.renderText(text, x, y, scale, textColor, shadow);
    }
}
