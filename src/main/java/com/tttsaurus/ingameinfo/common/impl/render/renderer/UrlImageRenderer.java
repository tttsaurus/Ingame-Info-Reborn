package com.tttsaurus.ingameinfo.common.impl.render.renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UrlImageRenderer extends ImageRenderer
{
    public UrlImageRenderer() { }

    public UrlImageRenderer(String url)
    {
        BufferedImage image = downloadImage(url);
        if (image != null) texture = createTexture(image);
    }

    private BufferedImage asyncImage = null;

    public void updateUrlAsync(String url)
    {
        if (url.isEmpty()) return;
        CompletableFuture.supplyAsync(() ->
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
        }).thenAccept(image ->
        {
            if (image != null) asyncImage = image;
        });
    }

    @Override
    public void render()
    {
        if (asyncImage != null)
        {
            if (texture != null) texture.dispose();
            texture = createTexture(asyncImage);
            asyncImage = null;
        }
        super.render();
    }

    public void updateUrl(String url)
    {
        if (url.isEmpty()) return;
        BufferedImage image = downloadImage(url);
        if (image != null)
        {
            if (texture != null) texture.dispose();
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
}
