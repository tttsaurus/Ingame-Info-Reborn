package com.tttsaurus.ingameinfo.common.core.render.texture;

import com.tttsaurus.ingameinfo.common.core.render.GlResourceManager;
import com.tttsaurus.ingameinfo.common.core.render.IGlDisposable;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.*;
import java.nio.ByteBuffer;

import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.INT_BUFFER_16;

public final class Texture2D implements ITexture2D, IGlDisposable
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

    public Texture2D(int width, int height, int glTextureID)
    {
        this.width = width;
        this.height = height;
        this.glTextureID = glTextureID;
        isGlRegistered = true;
    }

    public Texture2D(int width, int height, ByteBuffer byteBuffer)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        glTextureID = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, RenderHints.getHint_Texture2D$FilterModeMin().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, RenderHints.getHint_Texture2D$FilterModeMag().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, RenderHints.getHint_Texture2D$WrapModeS().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, RenderHints.getHint_Texture2D$WrapModeT().glValue);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        isGlRegistered = true;

        GlStateManager.bindTexture(textureID);

        GlResourceManager.addDisposable(this);
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
