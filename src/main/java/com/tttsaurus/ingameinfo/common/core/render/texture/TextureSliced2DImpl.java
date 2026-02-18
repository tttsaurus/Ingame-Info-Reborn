package com.tttsaurus.ingameinfo.common.core.render.texture;

import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.GlDisposable;
import org.lwjgl.opengl.GL11;

public final class TextureSliced2DImpl implements Texture2D, GlDisposable
{
    private final int x, y;
    private final float minU, maxU, minV, maxV;

    private int glTextureID = 0;
    private final int width;
    private final int height;
    private boolean isGlRegistered;

    public float getMinU() { return minU; }
    public float getMaxU() { return maxU; }
    public float getMinV() { return minV; }
    public float getMaxV() { return maxV; }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public int getGlTextureID() { return glTextureID; }
    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }
    @Override
    public boolean isGlRegistered() { return isGlRegistered; }

    public TextureSliced2DImpl(int x, int y, int width, int height, int glTextureID, float minU, float maxU, float minV, float maxV)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.glTextureID = glTextureID;
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
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
