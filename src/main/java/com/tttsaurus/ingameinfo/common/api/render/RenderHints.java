package com.tttsaurus.ingameinfo.common.api.render;

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

    public static void multisampleFbo(boolean flag)
    {
        if (flag)
        {
            glStateManagerBindTextureHint = RenderHints.GlStateManager.BindTextureHint.TEXTURE_2D_MULTISAMPLE;
            framebufferCreateFramebufferHint = RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE;
        }
        else
        {
            glStateManagerBindTextureHint = RenderHints.GlStateManager.BindTextureHint.TEXTURE_2D;
            framebufferCreateFramebufferHint = RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D;
        }
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
}
