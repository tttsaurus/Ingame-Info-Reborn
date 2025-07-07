package com.tttsaurus.ingameinfo.common.impl.gui.render.op;

import com.tttsaurus.ingameinfo.common.core.animation.text.CharInfo;
import com.tttsaurus.ingameinfo.common.core.gui.property.lerp.LerpableProperty;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;

public class AnimTextOp implements IRenderOp
{
    public String text;
    public float x, y;
    public int color;
    public LerpableProperty<CharInfo[]> charInfos;

    public AnimTextOp(String text, float x, float y, int color, LerpableProperty<CharInfo[]> charInfos)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.charInfos = charInfos;
    }

    public boolean overrideColor;

    @Override
    public void readTheme(ThemeConfig theme)
    {
        if (color == 0)
        {
            color = theme.animText.parsedColor;
            overrideColor = true;
        }
    }

    @Override
    public void execute(RenderContext context)
    {
        CharInfo[] charInfos1 = charInfos.lerp(context.lerpAlpha);
        for (int i = 0; i < Math.min(charInfos1.length, text.length()); i++)
        {
            String c = String.valueOf(text.charAt(i));
            CharInfo info = charInfos1[i];
            RenderUtils.renderText(c, x + info.x, y + info.y, info.scale, overrideColor ? color : info.color, info.shadow);
        }
    }
}
