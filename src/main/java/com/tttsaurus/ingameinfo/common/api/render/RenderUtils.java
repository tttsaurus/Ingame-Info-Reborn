package com.tttsaurus.ingameinfo.common.api.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;

public final class RenderUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static float zLevel = 0;

    // for all render methods, pivot is in the top-left corner
    // all methods use minecraft scaled resolution's coordinate

    public static void renderText(String text, float x, float y, float scale, int color, boolean shadow)
    {
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(scale, scale, 0);
        fontRenderer.drawString(text, 0, 0, color, shadow);
        GlStateManager.popMatrix();
    }

    public static void renderRect(float x, float y, float width, float height, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        Gui.drawRect(0, 0, 1, 1, color);
        GlStateManager.popMatrix();
    }

    // this method is modified from Minecraft
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
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
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

    public static void renderTexture2D(float x, float y, float width, float height, int textureWidth, int textureHeight)
    {
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, zLevel);
        GlStateManager.scale(width, height, 0);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, textureWidth, textureHeight, 1, 1, textureWidth, textureHeight);
        GlStateManager.popMatrix();
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
