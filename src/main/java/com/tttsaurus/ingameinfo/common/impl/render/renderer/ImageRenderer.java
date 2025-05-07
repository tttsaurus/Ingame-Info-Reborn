package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.Texture2D;
import com.tttsaurus.ingameinfo.common.core.render.renderer.IRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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

            texture = RenderUtils.createTexture2D(bufferedImage);
        }
        catch (IOException ignored) { }
    }

    public void updateRl(ResourceLocation resourceLocation)
    {
        if (resourceLocation == null) return;
        if (texture != null) texture.dispose();
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
            InputStream inputStream = resource.getInputStream();

            BufferedImage bufferedImage = ImageIO.read(inputStream);

            texture = RenderUtils.createTexture2D(bufferedImage);
        }
        catch (IOException ignored) { }
    }

    public void render()
    {
        if (texture == null) return;
        if (!texture.getIsGlBounded()) return;

        RenderUtils.renderTexture2D(x, y, width, height, texture.getGlTextureID());
    }
}
