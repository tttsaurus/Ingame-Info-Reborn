package com.tttsaurus.ingameinfo.common.core.render.texture;

import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.GlDisposable;
import org.lwjgl.opengl.GL11;

public final class Texture2DImpl implements Texture2D, GlDisposable
{
    private int glTextureID;
    private final int width;
    private final int height;
    private boolean isGlRegistered;

    @Override
    public int getGlTextureID() { return glTextureID; }
    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }
    @Override
    public boolean isGlRegistered() { return isGlRegistered; }

    public Texture2DImpl(int width, int height, int glTextureID)
    {
        this.width = width;
        this.height = height;
        this.glTextureID = glTextureID;
        isGlRegistered = true;
    }

    @Override
    public void dispose()
    {
        if (glTextureID != 0)
            if (GL11.glIsTexture(glTextureID))
                GL11.glDeleteTextures(glTextureID);
        glTextureID = 0;
        isGlRegistered = false;
        GlResourceManager.removeDisposable(this);
    }
}
