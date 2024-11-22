package com.tttsaurus.ingameinfo.common.api.render.renderer;

import com.tttsaurus.ingameinfo.common.api.render.Texture2D;
import org.lwjgl.BufferUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class URLImageRenderer extends ImageRenderer
{
    public static final URLImageRenderer SHARED = new URLImageRenderer("https://media.forgecdn.net/avatars/thumbnails/1071/348/256/256/638606872011907048.png");

    public URLImageRenderer(String url)
    {
        BufferedImage image = downloadImage(url);
        if (image != null) texture = createTexture(image);
    }

    public void updateURL(String url)
    {
        BufferedImage image = downloadImage(url);
        if (image != null)
        {
            texture.dispose();
            texture = createTexture(image);
        }
    }

    private BufferedImage downloadImage(String url)
    {
        try
        {
            URL imageUrl = new URL(url);
            InputStream in = imageUrl.openStream();
            return ImageIO.read(in);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Texture2D createTexture(BufferedImage image)
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
}
