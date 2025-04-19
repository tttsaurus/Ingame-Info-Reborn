package com.tttsaurus.ingameinfo.common.api.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class RenderUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static float zLevel = 0;

    private static final IntBuffer intBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();

    private static void addArcVertices(BufferBuilder bufferbuilder, float cx, float cy, float radius, float startAngle, float endAngle, int segments)
    {
        startAngle -= 90;
        endAngle -= 90;
        float x = (float)(cx + Math.cos(Math.toRadians(startAngle)) * radius);
        float y = (float)(cy + Math.sin(Math.toRadians(startAngle)) * radius);
        for (int i = 1; i <= segments; i++)
        {
            float angle = (float)Math.toRadians(startAngle + (endAngle - startAngle) * i / segments);
            float dx = (float)(cx + Math.cos(angle) * radius);
            float dy = (float)(cy + Math.sin(angle) * radius);
            bufferbuilder.pos(x, y, 0).endVertex();
            bufferbuilder.pos(dx, dy, 0).endVertex();
            x = dx;
            y = dy;
        }
    }

    // for all render methods, pivot is in the top-left corner
    // all methods use Minecraft scaled resolution's coordinate

    //<editor-fold desc="text">
    public static void renderText(String text, float x, float y, float scale, int color, boolean shadow)
    {
        GlStateManager.disableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(scale, scale, 0);
        fontRenderer.drawString(text, 0, 0, color, shadow);
        GlStateManager.popMatrix();
    }
    //</editor-fold>

    //<editor-fold desc="rect">
    public static void renderRect(float x, float y, float width, float height, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();
    }
    public static void renderRectFullScreen(int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double width = resolution.getScaledWidth_double();
        double height = resolution.getScaledHeight_double();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(0, height, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(width, height, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(width, 0, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(0, 0, zLevel).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
    // this method is modified from Minecraft's Gui.drawGradientRect
    public static void renderGradientRect(float x, float y, float width, float height, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(1, 0, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(0, 0, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(0, 1, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(1, 1, 0).color(f5, f6, f7, f4).endVertex();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        tessellator.draw();
        GlStateManager.popMatrix();
    }
    public static void renderRectOutline(float x, float y, float width, float height, float thickness, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);

        GlStateManager.pushMatrix();
        GlStateManager.scale(width, thickness, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, height, 0);
        GlStateManager.scale(width, thickness, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.scale(thickness, height, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(width, 0, 0);
        GlStateManager.scale(thickness, height + thickness, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }
    //</editor-fold>

    //<editor-fold desc="rounded rect">
    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color)
    {
        int segments = Math.max(3, (int)(radius / 2f));
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);

        if (RenderHints.getHint_PolygonSmoothHint())
        {
            GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, zLevel);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);

        addArcVertices(bufferbuilder, x + width - radius, y + radius, radius, 0, 90, segments);
        bufferbuilder.pos(x + width, y + radius, 0).endVertex();
        bufferbuilder.pos(x + width, y + height - radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + width - radius, y + height - radius, radius, 90, 180, segments);
        bufferbuilder.pos(x + width - radius, y + height, 0).endVertex();
        bufferbuilder.pos(x + radius, y + height, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + height - radius, radius, 180, 270, segments);
        bufferbuilder.pos(x, y + height - radius, 0).endVertex();
        bufferbuilder.pos(x, y + radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + radius, radius, 270, 360, segments);
        bufferbuilder.pos(x + radius, y, 0).endVertex();
        bufferbuilder.pos(x + width - radius, y, 0).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();

        if (RenderHints.getHint_PolygonSmoothHint()) GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }
    public static void renderRoundedRectOutline(float x, float y, float width, float height, float radius, float thickness, int color)
    {
        int segments = Math.max(3, (int)(radius / 2f));
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(thickness * (float)(new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor());
        GlStateManager.color(r, g, b, a);

        if (RenderHints.getHint_LineSmoothHint())
        {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, zLevel);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        addArcVertices(bufferbuilder, x + width - radius, y + radius, radius, 0, 90, segments);
        bufferbuilder.pos(x + width, y + radius, 0).endVertex();
        bufferbuilder.pos(x + width, y + height - radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + width - radius, y + height - radius, radius, 90, 180, segments);
        bufferbuilder.pos(x + width - radius, y + height, 0).endVertex();
        bufferbuilder.pos(x + radius, y + height, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + height - radius, radius, 180, 270, segments);
        bufferbuilder.pos(x, y + height - radius, 0).endVertex();
        bufferbuilder.pos(x, y + radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + radius, radius, 270, 360, segments);
        bufferbuilder.pos(x + radius, y, 0).endVertex();
        bufferbuilder.pos(x + width - radius, y, 0).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();

        if (RenderHints.getHint_LineSmoothHint()) GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
    //</editor-fold>

    //<editor-fold desc="stencil">
    public static void prepareStencilToWrite(int stencilValue)
    {
//        if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled())
//            Minecraft.getMinecraft().getFramebuffer().enableStencil();

        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        GL11.glEnable(GL11.GL_STENCIL_TEST);

        GlStateManager.depthMask(false);
        GlStateManager.colorMask(false, false, false, false);
        GL11.glStencilFunc(GL11.GL_ALWAYS, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);

        // mask area
        GL11.glStencilMask(0xFF);
    }
    public static void prepareStencilToRender(int stencilValue)
    {
        GL11.glStencilMask(0x00);

        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
    }
    public static void prepareStencilToDecrease(int stencilValue)
    {
        GL11.glStencilMask(0xFF);

        GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_DECR);
    }
    public static void prepareStencilToIncrease(int stencilValue)
    {
        GL11.glStencilMask(0xFF);

        GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_INCR);
    }
    public static void prepareStencilToZero(int stencilValue)
    {
        GL11.glStencilMask(0xFF);

        GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_ZERO);
    }
    public static void endStencil()
    {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    public static void drawRoundedRectStencilArea(float x, float y, float width, float height, float radius)
    {
        int segments = Math.max(3, (int)(radius / 2f));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);

        addArcVertices(bufferbuilder, x + width - radius, y + radius, radius, 0, 90, segments);
        bufferbuilder.pos(x + width, y + radius, 0).endVertex();
        bufferbuilder.pos(x + width, y + height - radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + width - radius, y + height - radius, radius, 90, 180, segments);
        bufferbuilder.pos(x + width - radius, y + height, 0).endVertex();
        bufferbuilder.pos(x + radius, y + height, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + height - radius, radius, 180, 270, segments);
        bufferbuilder.pos(x, y + height - radius, 0).endVertex();
        bufferbuilder.pos(x, y + radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + radius, radius, 270, 360, segments);
        bufferbuilder.pos(x + radius, y, 0).endVertex();
        bufferbuilder.pos(x + width - radius, y, 0).endVertex();

        tessellator.draw();
    }
    public static void drawRectStencilArea(float x, float y, float width, float height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y, 0d).endVertex();
        bufferbuilder.pos(x + width, y, 0d).endVertex();
        bufferbuilder.pos(x + width, y + height, 0d).endVertex();
        bufferbuilder.pos(x, y + height, 0d).endVertex();
        tessellator.draw();
    }
    //</editor-fold>

    //<editor-fold desc="texture">
    public static void renderNinePatchBorder(float x, float y, float width, float height, NinePatchBorder ninePatchBorder)
    {
        float width1 = ninePatchBorder.topLeft.width;
        float height1 = ninePatchBorder.topLeft.height;
        renderTexture2D(x, y, width1, height1, ninePatchBorder.topLeft.tex);

        float width2 = ninePatchBorder.topRight.width;
        float height2 = ninePatchBorder.topRight.height;
        renderTexture2D(x + width - width2, y, width2, height2, ninePatchBorder.topRight.tex);

        if (width - width1 - width2 > 0)
        {
            float height3 = ninePatchBorder.topCenter.height;
            renderTexture2D(x + width1, y, width - width1 - width2, height3, ninePatchBorder.topCenter.tex);
        }

        float width4 = ninePatchBorder.bottomLeft.width;
        float height4 = ninePatchBorder.bottomLeft.height;
        renderTexture2D(x, y + height - height4, width4, height4, ninePatchBorder.bottomLeft.tex);

        float width5 = ninePatchBorder.bottomRight.width;
        float height5 = ninePatchBorder.bottomRight.height;
        renderTexture2D(x + width - width5, y + height - height5, width5, height5, ninePatchBorder.bottomRight.tex);

        if (width - width4 - width5 > 0)
        {
            float height6 = ninePatchBorder.bottomCenter.height;
            renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, ninePatchBorder.bottomCenter.tex);
        }

        if (height - height1 - height4 > 0)
        {
            float width7 = ninePatchBorder.centerLeft.width;
            renderTexture2D(x, y + height1, width7, height - height1 - height4, ninePatchBorder.centerLeft.tex);
        }

        if (height - height2 - height5 > 0)
        {
            float width8 = ninePatchBorder.centerRight.width;
            renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, ninePatchBorder.centerRight.tex);
        }

        if (width - width1 - width2 > 0 && height - height1 - height4 > 0)
        {
            renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, ninePatchBorder.center.tex);
        }
    }
    public static void renderNinePatchBorderByPixel(float x, float y, float width, float height, NinePatchBorder ninePatchBorder)
    {
        float ppu = RenderHints.getHint_pixelPerUnit();

        float width1 = ninePatchBorder.topLeft.tex.getWidth() / ppu;
        float height1 = ninePatchBorder.topLeft.tex.getHeight() / ppu;
        renderTexture2D(x, y, width1, height1, ninePatchBorder.topLeft.tex);

        float width2 = ninePatchBorder.topRight.tex.getWidth() / ppu;
        float height2 = ninePatchBorder.topRight.tex.getHeight() / ppu;
        renderTexture2D(x + width - width2, y, width2, height2, ninePatchBorder.topRight.tex);

        if (width - width1 - width2 > 0)
        {
            float height3 = ninePatchBorder.topCenter.tex.getHeight() / ppu;
            renderTexture2D(x + width1, y, width - width1 - width2, height3, ninePatchBorder.topCenter.tex);
        }

        float width4 = ninePatchBorder.bottomLeft.tex.getWidth() / ppu;
        float height4 = ninePatchBorder.bottomLeft.tex.getHeight() / ppu;
        renderTexture2D(x, y + height - height4, width4, height4, ninePatchBorder.bottomLeft.tex);

        float width5 = ninePatchBorder.bottomRight.tex.getWidth() / ppu;
        float height5 = ninePatchBorder.bottomRight.tex.getHeight() / ppu;
        renderTexture2D(x + width - width5, y + height - height5, width5, height5, ninePatchBorder.bottomRight.tex);

        if (width - width4 - width5 > 0)
        {
            float height6 = ninePatchBorder.bottomCenter.tex.getHeight() / ppu;
            renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, ninePatchBorder.bottomCenter.tex);
        }

        if (height - height1 - height4 > 0)
        {
            float width7 = ninePatchBorder.centerLeft.tex.getWidth() / ppu;
            renderTexture2D(x, y + height1, width7, height - height1 - height4, ninePatchBorder.centerLeft.tex);
        }

        if (height - height2 - height5 > 0)
        {
            float width8 = ninePatchBorder.centerRight.tex.getWidth() / ppu;
            renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, ninePatchBorder.centerRight.tex);
        }

        if (width - width1 - width2 > 0 && height - height1 - height4 > 0)
        {
            renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, ninePatchBorder.center.tex);
        }
    }
    public static void renderTexture2D(float x, float y, float width, float height, Texture2D texture2D)
    {
        renderTexture2D(x, y, width, height, texture2D.getWidth(), texture2D.getHeight(), texture2D.getGlTextureID());
    }
    public static void renderTexture2D(float x, float y, float width, float height, int textureWidth, int textureHeight, int textureId)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureID = intBuffer.get(0);

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        GlStateManager.bindTexture(textureId);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width/(float)((int)(width)), height/(float)((int)(height)), 0);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, textureWidth, textureHeight, (int)width, (int)height, textureWidth, textureHeight);
        GlStateManager.popMatrix();

        GlStateManager.bindTexture(textureID);
    }
    public static void renderTexture2DFullScreen(int textureId)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureID = intBuffer.get(0);

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double width = resolution.getScaledWidth_double();
        double height = resolution.getScaledHeight_double();

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, height, zLevel).tex(0, 1).endVertex();
        buffer.pos(width, height, zLevel).tex(1, 1).endVertex();
        buffer.pos(width, 0, zLevel).tex(1, 0).endVertex();
        buffer.pos(0, 0, zLevel).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.bindTexture(textureID);
    }
    //</editor-fold>

    //<editor-fold desc="fbo">
    public static void renderFbo(ScaledResolution resolution, Framebuffer fbo, boolean useTexture)
    {
        //GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        //int textureID = intBuffer.get(0);

        double width = resolution.getScaledWidth_double();
        double height = resolution.getScaledHeight_double();

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (useTexture)
            fbo.bindFramebufferTexture();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        if (useTexture)
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        else
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(0, height, 0.0).tex(0, 0).endVertex();
        buffer.pos(width, height, 0.0).tex(1, 0).endVertex();
        buffer.pos(width, 0, 0.0).tex(1, 1).endVertex();
        buffer.pos(0, 0, 0.0).tex(0, 1).endVertex();
        tessellator.draw();

        //GlStateManager.bindTexture(textureID);
    }
    //</editor-fold>

    //<editor-fold desc="screen shot">
    public static ByteBuffer getInGameScreenShotByteBufferFullScreen()
    {
        return getInGameScreenShotByteBufferInternal(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }
    public static ByteBuffer getInGameScreenShotByteBuffer(int x, int y, int width, int height)
    {
        int scaleFactor = (new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
        return getInGameScreenShotByteBufferInternal(x * scaleFactor, y * scaleFactor, width * scaleFactor, height * scaleFactor);

    }
    public static Texture2D getInGameScreenShotFullScreen()
    {
        return getInGameScreenShotInternal(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }
    public static Texture2D getInGameScreenShot(int x, int y, int width, int height)
    {
        int scaleFactor = (new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
        return getInGameScreenShotInternal(x * scaleFactor, y * scaleFactor, width * scaleFactor, height * scaleFactor);
    }
    private static Texture2D getInGameScreenShotInternal(int rawX, int rawY, int textureWidth, int textureHeight)
    {
        ByteBuffer flippedBuffer =getInGameScreenShotByteBufferInternal(rawX, rawY, textureWidth, textureHeight);
        return new Texture2D(textureWidth, textureHeight, flippedBuffer);
    }
    private static ByteBuffer getInGameScreenShotByteBufferInternal(int rawX, int rawY, int textureWidth, int textureHeight)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(textureWidth * textureHeight * 4);
        ByteBuffer flippedBuffer = BufferUtils.createByteBuffer(textureWidth * textureHeight * 4);
        GL11.glReadPixels(rawX, Minecraft.getMinecraft().displayHeight - rawY - textureHeight, textureWidth, textureHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        int bytesPerPixel = 4;
        for (int row = 0; row < textureHeight; row++)
        {
            int srcPos = (textureHeight - row - 1) * textureWidth * bytesPerPixel;
            int destPos = row * textureWidth * bytesPerPixel;

            for (int col = 0; col < textureWidth * bytesPerPixel; col++)
                flippedBuffer.put(destPos + col, buffer.get(srcPos + col));
        }
        return flippedBuffer;
    }
    //</editor-fold>

    //<editor-fold desc="png">
    @SuppressWarnings("all")
    public static void createPng(File directory, String fileName, ByteBuffer buffer, int width, int height)
    {
        if (!directory.exists())
            directory.mkdirs();

        File pngFile = new File(directory, fileName + ".png");

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int i = (y * width + x) * 4;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                int a = buffer.get(i + 3) & 0xFF;

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, argb);
            }
        }

        try
        {
            ImageIO.write(image, "PNG", pngFile);
        }
        catch (IOException ignored) { }
    }
    @Nullable
    public static Texture2D readPng(File png)
    {
        if (!png.exists()) return null;
        try
        {
            return createTexture2D(ImageIO.read(png));
        }
        catch (IOException ignored) { return null; }
    }
    //</editor-fold>

    //<editor-fold desc="texture">
    @Nullable
    public static Texture2D createTexture2D(BufferedImage bufferedImage)
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (width == 0 || height == 0) return null;

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        int[] pixels = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));  // r
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // g
                buffer.put((byte) (pixel & 0xFF));          // b
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // a
            }
        }
        buffer.flip();

        return new Texture2D(width, height, buffer);
    }
    //</editor-fold>
}
