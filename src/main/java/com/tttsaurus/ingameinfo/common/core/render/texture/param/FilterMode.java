package com.tttsaurus.ingameinfo.common.core.render.texture.param;

import org.lwjgl.opengl.GL11;

public enum FilterMode
{
    LINEAR(GL11.GL_LINEAR),
    NEAREST(GL11.GL_NEAREST);

    public final int glValue;
    FilterMode(int glValue)
    {
        this.glValue = glValue;
    }
}
