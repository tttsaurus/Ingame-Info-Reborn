package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import java.util.Arrays;

public class AnimTextRenderer extends TextRenderer
{
    public static class CharInfo
    {
        public float x;
        public float y;
        public float scale;
        public int color;
        public boolean shadow;

        public CharInfo(float x, float y, float scale, int color, boolean shadow)
        {
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.color = color;
            this.shadow = shadow;
        }
    }

    protected CharInfo[] characterInfos = new CharInfo[0];

    //<editor-fold desc="getters & setters">
    @Override
    public void setText(String text)
    {
        int oldLength = characterInfos.length;
        int length = text.length();

        float width = 0;
        for (int i = 0; i < oldLength; i++)
        {
            char c = text.charAt(i);
            characterInfos[i].x = width;
            width += RenderUtils.fontRenderer.getCharWidth(c) * characterInfos[i].scale;
        }

        characterInfos = Arrays.copyOf(characterInfos, length);

        for (int i = oldLength; i < length; i++)
        {
            char c = text.charAt(i);
            characterInfos[i] = new CharInfo(width, 0f, scale, color, shadow);
            width += RenderUtils.fontRenderer.getCharWidth(c) * scale;
        }

        this.text = text;
    }

    @Override
    public void setScale(float scale)
    {
        super.setScale(scale);
        float width = 0;
        for (int i = 0; i < characterInfos.length; i++)
        {
            char c = text.charAt(i);
            characterInfos[i].x = width;
            characterInfos[i].scale = scale;
            width += RenderUtils.fontRenderer.getCharWidth(c) * characterInfos[i].scale;
        }
    }

    @Override
    public void setColor(int color)
    {
        super.setColor(color);
        for (CharInfo characterInfo : characterInfos)
            characterInfo.color = color;
    }

    @Override
    public void setShadow(boolean shadow)
    {
        super.setShadow(shadow);
        for (CharInfo characterInfo : characterInfos)
            characterInfo.shadow = shadow;
    }

    public CharInfo[] getCharacterInfos() { return characterInfos; }
    //</editor-fold>

    @Override
    public void render()
    {
        for (int i = 0; i < text.length(); i++)
        {
            String c = String.valueOf(text.charAt(i));
            CharInfo info = characterInfos[i];
            RenderUtils.renderText(c, x + info.x, y + info.y, info.scale, info.color, info.shadow);
        }
    }
}
