package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.render.RenderHints;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.api.render.NinePatchBorder;
import com.tttsaurus.ingameinfo.common.api.render.Texture2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public final class GuiResources
{
    public static NinePatchBorder mcVanillaBg;

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

        Texture2D mcVanillaBgTopLeft = null;
        Texture2D mcVanillaBgTopCenter = null;
        Texture2D mcVanillaBgTopRight = null;
        Texture2D mcVanillaBgCenterLeft = null;
        Texture2D mcVanillaBgCenter = null;
        Texture2D mcVanillaBgCenterRight = null;
        Texture2D mcVanillaBgBottomLeft = null;
        Texture2D mcVanillaBgBottomCenter = null;
        Texture2D mcVanillaBgBottomRight = null;

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

        mcVanillaBg = new NinePatchBorder(
                mcVanillaBgTopLeft,
                mcVanillaBgTopCenter,
                mcVanillaBgTopRight,
                mcVanillaBgCenterLeft,
                mcVanillaBgCenter,
                mcVanillaBgCenterRight,
                mcVanillaBgBottomLeft,
                mcVanillaBgBottomCenter,
                mcVanillaBgBottomRight);
    }
}
