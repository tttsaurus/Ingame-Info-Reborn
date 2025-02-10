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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public final class RenderUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static float zLevel = 0;

    private static final IntBuffer intBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();

    // for all render methods, pivot is in the top-left corner
    // all methods use Minecraft scaled resolution's coordinate

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

        if (RenderHints.getPolygonSmoothHint())
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

        if (RenderHints.getPolygonSmoothHint()) GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
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

        if (RenderHints.getLineSmoothHint())
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

        if (RenderHints.getLineSmoothHint()) GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

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

    public static void initStencilStep1(int stencilValue)
    {
        if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled())
            Minecraft.getMinecraft().getFramebuffer().enableStencil();

        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        //GL11.glClearStencil(0);
        //GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

        GlStateManager.depthMask(false);
        GlStateManager.colorMask(false, false, false, false);
        GL11.glStencilFunc(GL11.GL_ALWAYS, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);

        // mask area
        GL11.glStencilMask(0xFF);
    }
    public static void initStencilStep2(int stencilValue, boolean exclude)
    {
        GL11.glStencilMask(0x00);

        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        if (exclude)
            GL11.glStencilFunc(GL11.GL_NOTEQUAL, stencilValue, 0xFF);
        else
            GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
    }

    public static void startRoundedRectStencil(float x, float y, float stencilWidth, float stencilHeight, int stencilValue, boolean exclude, float radius)
    {
        initStencilStep1(stencilValue);

        int segments = Math.max(3, (int)(radius / 2f));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);

        addArcVertices(bufferbuilder, x + stencilWidth - radius, y + radius, radius, 0, 90, segments);
        bufferbuilder.pos(x + stencilWidth, y + radius, 0).endVertex();
        bufferbuilder.pos(x + stencilWidth, y + stencilHeight - radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + stencilWidth - radius, y + stencilHeight - radius, radius, 90, 180, segments);
        bufferbuilder.pos(x + stencilWidth - radius, y + stencilHeight, 0).endVertex();
        bufferbuilder.pos(x + radius, y + stencilHeight, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + stencilHeight - radius, radius, 180, 270, segments);
        bufferbuilder.pos(x, y + stencilHeight - radius, 0).endVertex();
        bufferbuilder.pos(x, y + radius, 0).endVertex();
        addArcVertices(bufferbuilder, x + radius, y + radius, radius, 270, 360, segments);
        bufferbuilder.pos(x + radius, y, 0).endVertex();
        bufferbuilder.pos(x + stencilWidth - radius, y, 0).endVertex();

        tessellator.draw();

        initStencilStep2(stencilValue, exclude);
    }
    public static void startRectStencil(float x, float y, float stencilWidth, float stencilHeight, int stencilValue, boolean exclude)
    {
        initStencilStep1(stencilValue);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + stencilWidth, y);
        GL11.glVertex2f(x + stencilWidth, y + stencilHeight);
        GL11.glVertex2f(x, y + stencilHeight);
        GL11.glEnd();

        initStencilStep2(stencilValue, exclude);
    }
    public static void endStencil()
    {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    public static void renderRect(float x, float y, float width, float height, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();
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

    public static void renderTexture2D(float x, float y, float width, float height, int textureWidth, int textureHeight, int textureId)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureID = intBuffer.get(0);

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
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

    public static Texture2D getInGameScreenShot(int x, int y, int width, int height)
    {
        int scaleFactor = (new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
        return getInGameScreenShotInternal(x * scaleFactor, y * scaleFactor, width * scaleFactor, height * scaleFactor);
    }
    private static Texture2D getInGameScreenShotInternal(int rawX, int rawY, int textureWidth, int textureHeight)
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
        return new Texture2D(textureWidth, textureHeight, flippedBuffer);
    }
}
