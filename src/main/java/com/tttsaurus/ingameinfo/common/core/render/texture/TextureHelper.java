package com.tttsaurus.ingameinfo.common.core.render.texture;

import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.FilterMode;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.WrapMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static com.tttsaurus.ingameinfo.common.core.render.CommonBuffers.INT_BUFFER_16;

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

    public static void setTextureParams(ITexture2D tex, FilterMode filterModeMin, FilterMode filterModeMag, WrapMode wrapModeS, WrapMode wrapModeT)
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, INT_BUFFER_16);
        int textureID = INT_BUFFER_16.get(0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureID());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filterModeMin.glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filterModeMag.glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrapModeS.glValue);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrapModeT.glValue);

        GlStateManager.bindTexture(textureID);
    }

    @Nullable
    public static ImagePrefab tryWrapToImagePrefab(ITexture2D tex)
    {
        ImagePrefab imagePrefab = null;
        if (tex instanceof Texture2D texture2D)
            imagePrefab = new ImagePrefab(texture2D);
        else if (tex instanceof TextureSliced2D textureSliced2D)
            imagePrefab = new ImagePrefab(textureSliced2D);
        return imagePrefab;
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
