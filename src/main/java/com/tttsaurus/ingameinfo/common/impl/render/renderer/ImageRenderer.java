package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.Texture2D;
import com.tttsaurus.ingameinfo.common.api.render.renderer.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ImageRenderer implements IRenderer
{
    protected Texture2D texture = null;
    protected float x = 0;
    protected float y = 0;
    protected float width = 0;
    protected float height = 0;

    //<editor-fold desc="getters & setters">
    public Texture2D getTexture() { return texture; }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    //</editor-fold>

    public ImageRenderer() { }

    public ImageRenderer(Texture2D texture)
    {
        this.texture = texture;
    }
    public ImageRenderer(ResourceLocation resourceLocation)
    {
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();

            BufferedImage bufferedImage = ImageIO.read(inputStream);

            texture = createTexture(bufferedImage);
        }
        catch (IOException ignored) { }
    }
    public void updateRl(ResourceLocation resourceLocation)
    {
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();

            BufferedImage bufferedImage = ImageIO.read(inputStream);

            texture = createTexture(bufferedImage);
        }
        catch (IOException ignored) { }
    }

    protected Texture2D createTexture(BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width == 0 || height == 0) return null;

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(width * height * 4);

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];
                byteBuffer.put((byte) ((pixel >> 16) & 0xFF));  // r
                byteBuffer.put((byte) ((pixel >> 8) & 0xFF));   // g
                byteBuffer.put((byte) (pixel & 0xFF));          // b
                byteBuffer.put((byte) ((pixel >> 24) & 0xFF));  // a
            }
        }
        byteBuffer.flip();

        return new Texture2D(width, height, byteBuffer);
    }

    public void render()
    {
        if (texture == null) return;
        if (!texture.getIsGlBounded()) return;

        RenderUtils.renderTexture2D(x, y, width, height, texture.getWidth(), texture.getHeight(), texture.getGlTextureID());
    }
}
