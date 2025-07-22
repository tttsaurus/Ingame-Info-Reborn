package com.tttsaurus.ingameinfo.common.core.render.texture;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public final class TextureHelper
{
    @Nullable
    public static BufferedImage getBufferedImageFromRl(ResourceLocation rl)
    {
        try
        {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            InputStream inputStream = resource.getInputStream();
            return ImageIO.read(inputStream);
        }
        catch (Exception ignored) { }
        return null;
    }

    @Nullable
    public static ITexture2D loadTextureFromRl(ResourceLocation rl)
    {
        BufferedImage image = getBufferedImageFromRl(rl);
        if (image == null) return null;

        ITextureObject mcTex = Minecraft.getMinecraft().getTextureManager().getTexture(rl);
        if (mcTex != null)
            return new Texture2D(
                    image.getWidth(),
                    image.getHeight(),
                    mcTex.getGlTextureId());

        TextureAtlasSprite mcSprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(rl.toString());
        if (mcSprite != null)
            return new TextureSliced2D(
                    mcSprite.getOriginX(),
                    mcSprite.getOriginY(),
                    mcSprite.getIconWidth(),
                    mcSprite.getIconHeight(),
                    Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId(),
                    mcSprite.getMinU(),
                    mcSprite.getMaxU(),
                    mcSprite.getMinV(),
                    mcSprite.getMaxV());

        Texture2D texture2D = RenderUtils.createTexture2D(image);

        Minecraft.getMinecraft().getTextureManager().loadTexture(rl, new McTextureWrapper(texture2D));

        return texture2D;
    }
}
