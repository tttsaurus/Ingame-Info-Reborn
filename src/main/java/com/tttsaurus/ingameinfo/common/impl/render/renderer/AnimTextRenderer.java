package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;

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
        this.text = text;
        int length = text.length();
        characterInfos = new CharInfo[length];
        float width = 0;
        for (int i = 0; i < length; i++)
        {
            char c = text.charAt(i);
            characterInfos[i] = new CharInfo(width, 0f, scale, color, shadow);
            width += RenderUtils.fontRenderer.getCharWidth(c) * scale;
        }
    }
    public CharInfo[] getCharacterInfos() { return characterInfos; }
    //</editor-fold>

    @Override
    public void render()
    {
        for (int i = 0; i < characterInfos.length; i++)
        {
            String c = String.valueOf(text.charAt(i));
            CharInfo info = characterInfos[i];
            RenderUtils.renderText(c, x + info.x, y + info.y, info.scale, info.color, info.shadow);
        }
    }
}
