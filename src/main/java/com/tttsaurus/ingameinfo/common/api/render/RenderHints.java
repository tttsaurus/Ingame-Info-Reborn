package com.tttsaurus.ingameinfo.common.api.render;

import com.tttsaurus.ingameinfo.common.impl.gui.IgiGuiLifeCycle;
import org.lwjgl.opengl.GL11;

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
        public enum FramebufferClearHint
        {
            UNBIND_FBO,
            DONT_UNBIND_FBO
        }
    }

    // gl version
    private static boolean glVersionParsed = false;
    private static int majorGlVersion = -1;
    private static int minorGlVersion = -1;
    private static String rawGlVersion = "";

    // render hints
    private static GlStateManager.BindTextureHint hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    private static Framebuffer.CreateFramebufferHint hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    private static Framebuffer.FramebufferClearHint hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.UNBIND_FBO;
    private static int hint_Framebuffer$FramebufferSampleNum = 2;

    public static void multisampleTexBind()
    {
        hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void normalTexBind()
    {
        hint_GlStateManager$BindTextureHint = GlStateManager.BindTextureHint.TEXTURE_2D;
    }
    public static void multisampleFbo()
    {
        hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE;
    }
    public static void normalFbo()
    {
        hint_Framebuffer$CreateFramebufferHint = Framebuffer.CreateFramebufferHint.TEXTURE_2D;
    }
    public static void clearFboWithoutUnbind()
    {
        hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.DONT_UNBIND_FBO;
    }
    public static void clearFboWithUnbind()
    {
        hint_Framebuffer$FramebufferClearHint = Framebuffer.FramebufferClearHint.UNBIND_FBO;
    }
    public static void fboSampleNum(int num)
    {
        if (num < 1) num = 1;
        if (num > 4) num = 4;
        hint_Framebuffer$FramebufferSampleNum = num;
    }

    public static GlStateManager.BindTextureHint getHint_GlStateManager$BindTextureHint() { return hint_GlStateManager$BindTextureHint; }
    public static Framebuffer.CreateFramebufferHint getHint_Framebuffer$CreateFramebufferHint() { return hint_Framebuffer$CreateFramebufferHint; }
    public static Framebuffer.FramebufferClearHint getHint_Framebuffer$FramebufferClearHint() { return hint_Framebuffer$FramebufferClearHint; }
    public static int getHint_Framebuffer$FramebufferSampleNum() { return hint_Framebuffer$FramebufferSampleNum; }

    public static boolean getHint_LineSmoothHint() { return !IgiGuiLifeCycle.getEnableFbo() || IgiGuiLifeCycle.getEnableMultisampleOnFbo(); }
    public static boolean getHint_PolygonSmoothHint() { return !IgiGuiLifeCycle.getEnableFbo() || IgiGuiLifeCycle.getEnableMultisampleOnFbo(); }

    public static int getMajorGlVersion()
    {
        if (!glVersionParsed)
        {
            parseGlVersion();
            glVersionParsed = true;
        }
        return majorGlVersion;
    }
    public static int getMinorGlVersion()
    {
        if (!glVersionParsed)
        {
            parseGlVersion();
            glVersionParsed = true;
        }
        return minorGlVersion;
    }
    public static String getRawGlVersion()
    {
        if (!glVersionParsed)
        {
            parseGlVersion();
            glVersionParsed = true;
        }
        return rawGlVersion;
    }
    private static void parseGlVersion()
    {
        rawGlVersion = GL11.glGetString(GL11.GL_VERSION);

        if (rawGlVersion != null)
        {
            String[] parts = rawGlVersion.split("\\s+")[0].split("\\.");
            if (parts.length >= 2)
            {
                try
                {
                    majorGlVersion = Integer.parseInt(parts[0]);
                    minorGlVersion = Integer.parseInt(parts[1]);
                }
                catch (NumberFormatException e) { }
            }
        }
        else
            rawGlVersion = "";
    }
}
