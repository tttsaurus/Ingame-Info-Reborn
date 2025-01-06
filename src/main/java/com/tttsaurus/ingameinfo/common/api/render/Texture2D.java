package com.tttsaurus.ingameinfo.common.api.render;

import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;

public class Texture2D
{
    private int glTextureID = 0;
    private final int width;
    private final int height;
    private boolean isGlBound;

    public int getGlTextureID() { return glTextureID; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean getIsGlBound() { return isGlBound; }

    public Texture2D(int width, int height, ByteBuffer byteBuffer)
    {
        glTextureID = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        isGlBound = true;
    }

    public void dispose()
    {
        if (glTextureID != 0) GL11.glDeleteTextures(glTextureID);
        glTextureID = 0;
        isGlBound = false;
    }
}
