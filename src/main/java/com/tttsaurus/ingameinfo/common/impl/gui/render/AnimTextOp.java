package com.tttsaurus.ingameinfo.common.impl.gui.render;

import com.tttsaurus.ingameinfo.common.core.animation.text.CharInfo;
import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class AnimTextOp implements IRenderOp
{
    public String text;
    public float x, y, scale;
    public int color;
    public CharInfo[] charInfos;

    public AnimTextOp(String text, float x, float y, float scale, int color, CharInfo[] charInfos)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.color = color;
        this.charInfos = charInfos;
    }

    @Override
    public void execute(RenderContext context)
    {
        boolean overrideColor = false;

        if (color == 0)
        {
            color = context.theme.animText.parsedColor;
            overrideColor = true;
        }

        for (int i = 0; i < text.length(); i++)
        {
            String c = String.valueOf(text.charAt(i));
            CharInfo info = charInfos[i];
            RenderUtils.renderText(c, x + info.x, y + info.y, info.scale, overrideColor ? color : info.color, info.shadow);
        }
    }
}
