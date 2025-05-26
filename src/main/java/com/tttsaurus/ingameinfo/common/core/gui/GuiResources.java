package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.render.NinePatchBorder;
import com.tttsaurus.ingameinfo.common.core.render.Texture2D;
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
    public static NinePatchBorder mcVanillaButton;

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
        Texture2D.FilterMode filterMode = RenderHints.getHint_Texture2D$FilterMode();
        Texture2D.WrapMode wrapMode = RenderHints.getHint_Texture2D$WrapMode();

        RenderHints.texture2dNearestFilter();
        RenderHints.texture2dRepeatWrap();

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

        Texture2D mcVanillaButtonTopLeft = null;
        Texture2D mcVanillaButtonTopCenter = null;
        Texture2D mcVanillaButtonTopRight = null;
        Texture2D mcVanillaButtonCenterLeft = null;
        Texture2D mcVanillaButtonCenter = null;
        Texture2D mcVanillaButtonCenterRight = null;
        Texture2D mcVanillaButtonBottomLeft = null;
        Texture2D mcVanillaButtonBottomCenter = null;
        Texture2D mcVanillaButtonBottomRight = null;

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_left.png"));
        if (image != null)
            mcVanillaButtonTopLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_center.png"));
        if (image != null)
            mcVanillaButtonTopCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_right.png"));
        if (image != null)
            mcVanillaButtonTopRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center_left.png"));
        if (image != null)
            mcVanillaButtonCenterLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center.png"));
        if (image != null)
            mcVanillaButtonCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center_right.png"));
        if (image != null)
            mcVanillaButtonCenterRight = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_left.png"));
        if (image != null)
            mcVanillaButtonBottomLeft = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_center.png"));
        if (image != null)
            mcVanillaButtonBottomCenter = RenderUtils.createTexture2D(image);

        image = getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_right.png"));
        if (image != null)
            mcVanillaButtonBottomRight = RenderUtils.createTexture2D(image);

        mcVanillaButton = new NinePatchBorder(
                mcVanillaButtonTopLeft,
                mcVanillaButtonTopCenter,
                mcVanillaButtonTopRight,
                mcVanillaButtonCenterLeft,
                mcVanillaButtonCenter,
                mcVanillaButtonCenterRight,
                mcVanillaButtonBottomLeft,
                mcVanillaButtonBottomCenter,
                mcVanillaButtonBottomRight);
        mcVanillaButton.center.tiling = true;

        RenderHints.setHint_Texture2D$FilterMode(filterMode);
        RenderHints.setHint_Texture2D$WrapMode(wrapMode);
    }
}
