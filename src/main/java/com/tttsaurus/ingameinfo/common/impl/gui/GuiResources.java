package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.render.RenderHints;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.Texture2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public final class GuiResources
{
    public static Texture2D mcVanillaBgTopLeft;
    public static Texture2D mcVanillaBgTopCenter;
    public static Texture2D mcVanillaBgTopRight;
    public static Texture2D mcVanillaBgCenterLeft;
    public static Texture2D mcVanillaBgCenter;
    public static Texture2D mcVanillaBgCenterRight;
    public static Texture2D mcVanillaBgBottomLeft;
    public static Texture2D mcVanillaBgBottomCenter;
    public static Texture2D mcVanillaBgBottomRight;

    @Nullable
    private static BufferedImage getBufferedImageFromRl(ResourceLocation rl)
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

    public static void init()
    {
        Texture2D.FilterMode hint = RenderHints.getHint_Texture2D$FilterMode();
        RenderHints.texture2dNearestFilter();

        BufferedImage image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_left.png"));
        if (image != null)
            mcVanillaBgTopLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_center.png"));
        if (image != null)
            mcVanillaBgTopCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_right.png"));
        if (image != null)
            mcVanillaBgTopRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center_left.png"));
        if (image != null)
            mcVanillaBgCenterLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center.png"));
        if (image != null)
            mcVanillaBgCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center_right.png"));
        if (image != null)
            mcVanillaBgCenterRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_left.png"));
        if (image != null)
            mcVanillaBgBottomLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_center.png"));
        if (image != null)
            mcVanillaBgBottomCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_right.png"));
        if (image != null)
            mcVanillaBgBottomRight = RenderUtils.createTexture2D(image);

        RenderHints.setHint_Texture2D$FilterMode(hint);
    }
}
