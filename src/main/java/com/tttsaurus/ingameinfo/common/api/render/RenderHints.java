package com.tttsaurus.ingameinfo.common.api.render;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;

public final class RenderHints
{
    public static class GlStateManager
    {
        public enum BindTextureHint
        {
            TEXTURE_2D,
            TEXTURE_2D_MULTISAMPLE
        }
    }
    public static class Framebuffer
    {
        public enum CreateFramebufferHint
        {
            TEXTURE_2D,
            TEXTURE_2D_MULTISAMPLE
        }
    }

    private static GlStateManager.BindTextureHint glStateManagerBindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    private static Framebuffer.CreateFramebufferHint framebufferCreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    private static int framebufferSampleNum = 2;

    public static void multisampleTexBind()
    {
        glStateManagerBindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void normalTexBind()
    {
        glStateManagerBindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    }
    public static void multisampleFbo()
    {
        framebufferCreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void normalFbo()
    {
        framebufferCreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    }
    public static void fboSampleNum(int num)
    {
        if (num < 1) num = 1;
        if (num > 4) num = 4;
        framebufferSampleNum = num;
    }

    public static GlStateManager.BindTextureHint getGlStateManagerBindTextureHint() { return glStateManagerBindTextureHint; }
    public static Framebuffer.CreateFramebufferHint getFramebufferCreateFramebufferHint() { return framebufferCreateFramebufferHint; }
    public static int getFramebufferSampleNum() { return framebufferSampleNum; }

    public static boolean getLineSmoothHint() { return !IgiGuiLifeCycle.getIsFboRunning() || IgiGuiLifeCycle.getEnableMultisampleOnFbo(); }
    public static boolean getPolygonSmoothHint() { return !IgiGuiLifeCycle.getIsFboRunning() || IgiGuiLifeCycle.getEnableMultisampleOnFbo(); }
}
