package com.tttsaurus.ingameinfo.common.core.render;

import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.render.text.FormattedText;
import com.tttsaurus.ingameinfo.common.core.render.texture.ImagePrefab;
import com.tttsaurus.ingameinfo.common.core.render.texture.NinePatchBorder;
import com.tttsaurus.ingameinfo.common.core.render.texture.Texture2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.INT_BUFFER_16;

public final class RenderUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    public static float zLevel = 0;

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

    //<editor-fold desc="item">
    public static void renderItem(ItemStack item, float x, float y, float scaleX, float scaleY)
    {
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, Math.round(y), zLevel);
        GlStateManager.scale(scaleX, scaleY, 1f);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    //</editor-fold>

    //<editor-fold desc="text">
    public static FormattedText bakeFormattedText(String text)
    {
        return InternalMethods.instance.FormattedText$constructor.invoke(text);
    }

    public static void renderFormattedText(FormattedText text, float x, float y, float scale, int color, boolean shadow)
    {
        renderFormattedText(text, x, y, scale, color, shadow, AlphaBlendMode.ADDITIVE);
    }
    public static void renderFormattedText(FormattedText text, float x, float y, float scale, int color, boolean shadow, AlphaBlendMode mode)
    {
        for (FormattedText.BakedComponent component: text.bakedComponents)
        {
            if (component.type == FormattedText.BakedComponent.Type.STRING)
            {
                GlStateManager.disableCull();
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.disableDepth();

                switch (mode)
                {
                    case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
                    case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
                    case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
                    case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + component.x * scale, Math.round(y + component.y * scale), zLevel);
                GlStateManager.scale(scale, scale, 0);
                fontRenderer.drawString(component.text, 0, 0, color, shadow);
                GlStateManager.popMatrix();
            }
            else if (component.type == FormattedText.BakedComponent.Type.ITEM)
            {
                ItemStack itemStack = component.item.getItemStack();
                if (itemStack != null)
                {
                    float itemScale = simulateTextHeight(1f) / 16f;
                    renderItem(itemStack, x + component.x * scale, y + component.y * scale, itemScale * scale, itemScale * scale);
                }
            }
        }
    }

    public static void renderText(String text, float x, float y, float scale, int color, boolean shadow)
    {
        renderText(text, x, y, scale, color, shadow, AlphaBlendMode.ADDITIVE);
    }
    public static void renderText(String text, float x, float y, float scale, int color, boolean shadow, AlphaBlendMode mode)
    {
        GlStateManager.disableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, Math.round(y), zLevel);
        GlStateManager.scale(scale, scale, 0);
        fontRenderer.drawString(text, 0, 0, color, shadow);
        GlStateManager.popMatrix();
    }

    public static float simulateTextHeight(float scale)
    {
        return fontRenderer.FONT_HEIGHT * scale;
    }
    public static float simulateTextWidth(String text, float scale)
    {
        return fontRenderer.getStringWidth(text) * scale;
    }
    //</editor-fold>

    //<editor-fold desc="rect overlay">
    public static void renderRectBrightnessOverlay(float x, float y, float width, float height, float brightness)
    {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.color(brightness, brightness, brightness);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y + height, zLevel).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).endVertex();
        bufferbuilder.pos(x, y, zLevel).endVertex();
        tessellator.draw();
    }
    public static void renderRectBrightnessOverlay(float x, float y, float width, float height, float r, float g, float b)
    {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.color(r, g, b);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x, y + height, zLevel).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).endVertex();
        bufferbuilder.pos(x, y, zLevel).endVertex();
        tessellator.draw();
    }
    //</editor-fold>

    //<editor-fold desc="rect">
    public static void renderRect(float x, float y, float width, float height, int color)
    {
        renderRect(x, y, width, height, color, AlphaBlendMode.ADDITIVE);
    }
    public static void renderRect(float x, float y, float width, float height, int color, AlphaBlendMode mode)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x, y + height, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(x + width, y + height, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(x + width, y, zLevel).color(r, g, b, a).endVertex();
        buffer.pos(x, y, zLevel).color(r, g, b, a).endVertex();
        tessellator.draw();
    }

    public static void renderRectFullScreen(int color)
    {
        renderRectFullScreen(color, AlphaBlendMode.ADDITIVE);
    }
    public static void renderRectFullScreen(int color, AlphaBlendMode mode)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

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

    public static void renderGradientRect(float x, float y, float width, float height, int startColor, int endColor)
    {
        renderGradientRect(x, y, width, height, startColor, endColor, AlphaBlendMode.ADDITIVE);
    }
    // this method is modified from Minecraft's Gui.drawGradientRect
    public static void renderGradientRect(float x, float y, float width, float height, int startColor, int endColor, AlphaBlendMode mode)
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

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x + width, y, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, y, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(x, y + height, zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
    }

    public static void renderRectOutline(float x, float y, float width, float height, float thickness, int color)
    {
        renderRectOutline(x, y, width, height, thickness, color, AlphaBlendMode.ADDITIVE);
    }
    public static void renderRectOutline(float x, float y, float width, float height, float thickness, int color, AlphaBlendMode mode)
    {
        renderRect(x, y, width, thickness, color, mode);
        renderRect(x, y + height, width, thickness, color, mode);
        renderRect(x, y, thickness, height, color, mode);
        renderRect(x + width, y, thickness, height + thickness, color, mode);
    }
    //</editor-fold>

    //<editor-fold desc="rounded rect">
    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color, boolean smoothHint)
    {
        renderRoundedRect(x, y, width, height, radius, color, smoothHint, AlphaBlendMode.ADDITIVE);
    }
    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color, boolean smoothHint, AlphaBlendMode mode)
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

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

        GlStateManager.color(r, g, b, a);

        if (smoothHint)
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

        if (smoothHint) GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }

    public static void renderRoundedRectOutline(float x, float y, float width, float height, float radius, float thickness, int color, boolean smoothHint)
    {
        renderRoundedRectOutline(x, y, width, height, radius, thickness, color, smoothHint, AlphaBlendMode.ADDITIVE);
    }
    public static void renderRoundedRectOutline(float x, float y, float width, float height, float radius, float thickness, int color, boolean smoothHint, AlphaBlendMode mode)
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

        switch (mode)
        {
            case ADDITIVE -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE); }
            case FORCE_SRC -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO); }
            case FORCE_DEST -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE); }
            case ZERO -> { GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ZERO); }
        }

        GlStateManager.glLineWidth(thickness * (float)(new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor());
        GlStateManager.color(r, g, b, a);

        if (smoothHint)
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

        if (smoothHint) GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
    //</editor-fold>

    //<editor-fold desc="stencil">
    public static void prepareStencilToWrite(int stencilValue)
    {
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
    public static void renderImagePrefab(float x, float y, float width, float height, ImagePrefab imagePrefab)
    {
        switch (imagePrefab.type)
        {
            case TEXTURE_2D -> renderTexture2D(x, y, width, height, imagePrefab.texture2D.getGlTextureID(), -1);
            case TEXTURE_SLICED_2D -> renderTextureSliced2D(x, y, width, height, imagePrefab.textureSliced2D.getMinU(), imagePrefab.textureSliced2D.getMaxU(), imagePrefab.textureSliced2D.getMinV(), imagePrefab.textureSliced2D.getMaxV(), imagePrefab.textureSliced2D.getGlTextureID(), -1);
            case NINE_PATCH_BORDER -> renderNinePatchBorder(x, y, width, height, imagePrefab.ninePatchBorder, -1);
        }
    }
    public static void renderImagePrefab(float x, float y, float width, float height, ImagePrefab imagePrefab, int color)
    {
        switch (imagePrefab.type)
        {
            case TEXTURE_2D -> renderTexture2D(x, y, width, height, imagePrefab.texture2D.getGlTextureID(), color);
            case TEXTURE_SLICED_2D -> renderTextureSliced2D(x, y, width, height, imagePrefab.textureSliced2D.getMinU(), imagePrefab.textureSliced2D.getMaxU(), imagePrefab.textureSliced2D.getMinV(), imagePrefab.textureSliced2D.getMaxV(), imagePrefab.textureSliced2D.getGlTextureID(), color);
            case NINE_PATCH_BORDER -> renderNinePatchBorder(x, y, width, height, imagePrefab.ninePatchBorder, color);
        }
    }

    public static void renderTextureSliced2D(float x, float y, float width, float height, float minU, float maxU, float minV, float maxV, int textureId)
    {
        renderTextureSliced2D(x, y, width, height, minU, maxU, minV, maxV, textureId, -1);
    }
    public static void renderTextureSliced2D(float x, float y, float width, float height, float minU, float maxU, float minV, float maxV, int textureId, int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.color(r, g, b, a);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(minU, maxV).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(maxU, minV).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(minU, minV).endVertex();
        tessellator.draw();
    }

    public static void renderNinePatchBorder(float x, float y, float width, float height, NinePatchBorder ninePatchBorder)
    {
        renderNinePatchBorder(x, y, width, height, ninePatchBorder, -1);
    }
    public static void renderNinePatchBorder(float x, float y, float width, float height, NinePatchBorder ninePatchBorder, int color)
    {
        float ppu = RenderHints.getHint_pixelPerUnit();

        float width1, height1;
        if (ninePatchBorder.topLeft.sizeDeductionByPixels)
        {
            width1 = ninePatchBorder.topLeft.tex.getWidth() / ppu;
            height1 = ninePatchBorder.topLeft.tex.getHeight() / ppu;
        }
        else
        {
            width1 = ninePatchBorder.topLeft.width;
            height1 = ninePatchBorder.topLeft.height;
        }
        renderTexture2D(x, y, width1, height1, 1f, 1f, ninePatchBorder.topLeft.tex.getGlTextureID(), color);

        float width2, height2;
        if (ninePatchBorder.topRight.sizeDeductionByPixels)
        {
            width2 = ninePatchBorder.topRight.tex.getWidth() / ppu;
            height2 = ninePatchBorder.topRight.tex.getHeight() / ppu;
        }
        else
        {
            width2 = ninePatchBorder.topRight.width;
            height2 = ninePatchBorder.topRight.height;
        }
        renderTexture2D(x + width - width2, y, width2, height2, 1f, 1f, ninePatchBorder.topRight.tex.getGlTextureID(), color);

        if (width - width1 - width2 > 0)
        {
            float width3, height3;
            if (ninePatchBorder.topCenter.sizeDeductionByPixels)
            {
                width3 = ninePatchBorder.topCenter.tex.getWidth() / ppu;
                height3 = ninePatchBorder.topCenter.tex.getHeight() / ppu;
            }
            else
            {
                width3 = ninePatchBorder.topCenter.width;
                height3 = ninePatchBorder.topCenter.height;
            }
            if (ninePatchBorder.topCenter.tiling)
                renderTexture2D(x + width1, y, width - width1 - width2, height3, (width - width1 - width2) / width3, 1f, ninePatchBorder.topCenter.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width1, y, width - width1 - width2, height3, 1f, 1f, ninePatchBorder.topCenter.tex.getGlTextureID(), color);
        }

        float width4, height4;
        if (ninePatchBorder.bottomLeft.sizeDeductionByPixels)
        {
            width4 = ninePatchBorder.bottomLeft.tex.getWidth() / ppu;
            height4 = ninePatchBorder.bottomLeft.tex.getHeight() / ppu;
        }
        else
        {
            width4 = ninePatchBorder.bottomLeft.width;
            height4 = ninePatchBorder.bottomLeft.height;
        }

        float width5, height5;
        if (ninePatchBorder.bottomRight.sizeDeductionByPixels)
        {
            width5 = ninePatchBorder.bottomRight.tex.getWidth() / ppu;
            height5 = ninePatchBorder.bottomRight.tex.getHeight() / ppu;
        }
        else
        {
            width5 = ninePatchBorder.bottomRight.width;
            height5 = ninePatchBorder.bottomRight.height;
        }

        if (height - height1 - height4 > 0)
        {
            float width7, height7;
            if (ninePatchBorder.centerLeft.sizeDeductionByPixels)
            {
                width7 = ninePatchBorder.centerLeft.tex.getWidth() / ppu;
                height7 = ninePatchBorder.centerLeft.tex.getHeight() / ppu;
            }
            else
            {
                width7 = ninePatchBorder.centerLeft.width;
                height7 = ninePatchBorder.centerLeft.height;
            }
            if (ninePatchBorder.centerLeft.tiling)
                renderTexture2D(x, y + height1, width7, height - height1 - height4, 1f, (height - height1 - height4) / height7, ninePatchBorder.centerLeft.tex.getGlTextureID(), color);
            else
                renderTexture2D(x, y + height1, width7, height - height1 - height4, 1f, 1f, ninePatchBorder.centerLeft.tex.getGlTextureID(), color);
        }

        if (height - height2 - height5 > 0)
        {
            float width8, height8;
            if (ninePatchBorder.centerRight.sizeDeductionByPixels)
            {
                width8 = ninePatchBorder.centerRight.tex.getWidth() / ppu;
                height8 = ninePatchBorder.centerRight.tex.getHeight() / ppu;
            }
            else
            {
                width8 = ninePatchBorder.centerRight.width;
                height8 = ninePatchBorder.centerRight.height;
            }
            if (ninePatchBorder.centerRight.tiling)
                renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, 1f, (height - height2 - height5) / height8, ninePatchBorder.centerRight.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width - width8, y + height2, width8, height - height2 - height5, 1f, 1f, ninePatchBorder.centerRight.tex.getGlTextureID(), color);
        }

        if (width - width1 - width2 > 0 && height - height1 - height4 > 0)
        {
            float width9, height9;
            if (ninePatchBorder.center.sizeDeductionByPixels)
            {
                width9 = ninePatchBorder.center.tex.getWidth() / ppu;
                height9 = ninePatchBorder.center.tex.getHeight() / ppu;
            }
            else
            {
                width9 = ninePatchBorder.center.width;
                height9 = ninePatchBorder.center.height;
            }
            if (ninePatchBorder.center.tiling)
                renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, (width - width1 - width2) / width9, (height - height1 - height4) / height9, ninePatchBorder.center.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width1, y + height1, width - width1 - width2, height - height1 - height4, 1f, 1f, ninePatchBorder.center.tex.getGlTextureID(), color);
        }

        renderTexture2D(x, y + height - height4, width4, height4, 1f, 1f, ninePatchBorder.bottomLeft.tex.getGlTextureID(), color);
        renderTexture2D(x + width - width5, y + height - height5, width5, height5, 1f, 1f, ninePatchBorder.bottomRight.tex.getGlTextureID(), color);

        if (width - width4 - width5 > 0)
        {
            float width6, height6;
            if (ninePatchBorder.bottomCenter.sizeDeductionByPixels)
            {
                width6 = ninePatchBorder.bottomCenter.tex.getWidth() / ppu;
                height6 = ninePatchBorder.bottomCenter.tex.getHeight() / ppu;
            }
            else
            {
                width6 = ninePatchBorder.bottomCenter.width;
                height6 = ninePatchBorder.bottomCenter.height;
            }
            if (ninePatchBorder.bottomCenter.tiling)
                renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, (width - width4 - width5) / width6, 1f, ninePatchBorder.bottomCenter.tex.getGlTextureID(), color);
            else
                renderTexture2D(x + width4, y + height - height6, width - width4 - width5, height6, 1f, 1f, ninePatchBorder.bottomCenter.tex.getGlTextureID(), color);
        }
    }

    public static void renderTexture2D(float x, float y, float width, float height, int textureId)
    {
        renderTexture2D(x, y, width, height, 1f, 1f, textureId);
    }
    public static void renderTexture2D(float x, float y, float width, float height, int textureId, int color)
    {
        renderTexture2D(x, y, width, height, 1f, 1f, textureId, color);
    }
    public static void renderTexture2D(float x, float y, float width, float height, float widthTiling, float heightTiling, int textureId)
    {
        renderTexture2D(x, y, width, height, widthTiling, heightTiling, textureId, -1);
    }
    public static void renderTexture2D(float x, float y, float width, float height, float widthTiling, float heightTiling, int textureId, int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.color(r, g, b, a);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, heightTiling).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(widthTiling, heightTiling).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(widthTiling, 0).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    public static void renderTexture2DFullScreen(int textureId)
    {
        renderTexture2DFullScreen(textureId, 1f, 1f);
    }
    public static void renderTexture2DFullScreen(int textureId, int color)
    {
        renderTexture2DFullScreen(textureId, 1f, 1f, color);
    }
    public static void renderTexture2DFullScreen(int textureId, float widthTiling, float heightTiling)
    {
        renderTexture2DFullScreen(textureId, widthTiling, heightTiling, -1);
    }
    public static void renderTexture2DFullScreen(int textureId, float widthTiling, float heightTiling, int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double width = resolution.getScaledWidth_double();
        double height = resolution.getScaledHeight_double();

        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0f);
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.color(r, g, b, a);

        GlStateManager.bindTexture(textureId);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, height, zLevel).tex(0, heightTiling).endVertex();
        buffer.pos(width, height, zLevel).tex(widthTiling, heightTiling).endVertex();
        buffer.pos(width, 0, zLevel).tex(widthTiling, 0).endVertex();
        buffer.pos(0, 0, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

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

        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        int glTextureID = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, RenderHints.getHint_Texture2D$FilterModeMin().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, RenderHints.getHint_Texture2D$FilterModeMag().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, RenderHints.getHint_Texture2D$WrapModeS().glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, RenderHints.getHint_Texture2D$WrapModeT().glValue);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GlStateManager.bindTexture(textureID);

        Texture2D texture2D = new Texture2D(width, height, glTextureID);
        GlResourceManager.addDisposable(texture2D);

        return texture2D;
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
}
