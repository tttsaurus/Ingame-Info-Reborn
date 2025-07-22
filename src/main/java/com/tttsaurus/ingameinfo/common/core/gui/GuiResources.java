package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.render.*;
import com.tttsaurus.ingameinfo.common.core.render.texture.*;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.FilterMode;
import com.tttsaurus.ingameinfo.common.core.render.texture.param.WrapMode;
import net.minecraft.util.ResourceLocation;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class GuiResources
{
    private static final Map<String, ImagePrefab> imagePrefabs = new HashMap<>();

    public static void register(String name, ImagePrefab imagePrefab)
    {
        imagePrefabs.put(name, imagePrefab);
    }

    public static ImagePrefab tryRegisterTexture(ResourceLocation rl)
    {
        ITexture2D tex = TextureHelper.loadTextureFromRl(rl);
        if (tex == null) return missingTexture;
        ImagePrefab imagePrefab = TextureHelper.tryWrapToImagePrefab(tex);
        if (imagePrefab == null) return missingTexture;
        register(rl.toString(), imagePrefab);
        return imagePrefab;
    }
    public static ImagePrefab tryRegisterTexture(ResourceLocation rl, FilterMode filterModeMin, FilterMode filterModeMag, WrapMode wrapModeS, WrapMode wrapModeT)
    {
        ITexture2D tex = TextureHelper.loadTextureFromRl(rl);
        if (tex == null) return missingTexture;
        ImagePrefab imagePrefab = TextureHelper.tryWrapToImagePrefab(tex);
        if (imagePrefab == null) return missingTexture;
        TextureHelper.setTextureParams(tex, filterModeMin, filterModeMag, wrapModeS, wrapModeT);
        register(rl.toString(), imagePrefab);
        return imagePrefab;
    }

    public static boolean exists(ResourceLocation rl)
    {
        return exists(rl.toString());
    }
    public static boolean exists(String name)
    {
        return imagePrefabs.containsKey(name);
    }

    public static ImagePrefab get(ResourceLocation rl)
    {
        return get(rl.toString());
    }
    public static ImagePrefab get(String name)
    {
        ImagePrefab imagePrefab = imagePrefabs.get(name);
        if (imagePrefab == null)
            return missingTexture;
        else
            return imagePrefab;
    }

    private static ImagePrefab missingTexture;

    private static boolean init = false;
    public static void init()
    {
        if (init) return;
        init = true;

        FilterMode filterModeMin = RenderHints.getHint_Texture2D$FilterModeMin();
        FilterMode filterModeMag = RenderHints.getHint_Texture2D$FilterModeMag();
        WrapMode wrapModeS = RenderHints.getHint_Texture2D$WrapModeS();
        WrapMode wrapModeT = RenderHints.getHint_Texture2D$WrapModeT();

        RenderHints.texture2dNearestFilterMin();
        RenderHints.texture2dNearestFilterMag();
        RenderHints.texture2dRepeatWrapS();
        RenderHints.texture2dRepeatWrapT();

        Texture2D mcVanillaBgTopLeft = null;
        Texture2D mcVanillaBgTopCenter = null;
        Texture2D mcVanillaBgTopRight = null;
        Texture2D mcVanillaBgCenterLeft = null;
        Texture2D mcVanillaBgCenter = null;
        Texture2D mcVanillaBgCenterRight = null;
        Texture2D mcVanillaBgBottomLeft = null;
        Texture2D mcVanillaBgBottomCenter = null;
        Texture2D mcVanillaBgBottomRight = null;

        BufferedImage image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_left.png"));
        if (image != null)
            mcVanillaBgTopLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_center.png"));
        if (image != null)
            mcVanillaBgTopCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/top_right.png"));
        if (image != null)
            mcVanillaBgTopRight = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center_left.png"));
        if (image != null)
            mcVanillaBgCenterLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center.png"));
        if (image != null)
            mcVanillaBgCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/center_right.png"));
        if (image != null)
            mcVanillaBgCenterRight = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_left.png"));
        if (image != null)
            mcVanillaBgBottomLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_center.png"));
        if (image != null)
            mcVanillaBgBottomCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/background/vanilla/bottom_right.png"));
        if (image != null)
            mcVanillaBgBottomRight = RenderUtils.createTexture2D(image);

        ImagePrefab mcVanillaBg = new ImagePrefab(new NinePatchBorder(
                mcVanillaBgTopLeft,
                mcVanillaBgTopCenter,
                mcVanillaBgTopRight,
                mcVanillaBgCenterLeft,
                mcVanillaBgCenter,
                mcVanillaBgCenterRight,
                mcVanillaBgBottomLeft,
                mcVanillaBgBottomCenter,
                mcVanillaBgBottomRight));

        Texture2D mcVanillaButtonTopLeft = null;
        Texture2D mcVanillaButtonTopCenter = null;
        Texture2D mcVanillaButtonTopRight = null;
        Texture2D mcVanillaButtonCenterLeft = null;
        Texture2D mcVanillaButtonCenter = null;
        Texture2D mcVanillaButtonCenterRight = null;
        Texture2D mcVanillaButtonBottomLeft = null;
        Texture2D mcVanillaButtonBottomCenter = null;
        Texture2D mcVanillaButtonBottomRight = null;

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_left.png"));
        if (image != null)
            mcVanillaButtonTopLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_center.png"));
        if (image != null)
            mcVanillaButtonTopCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/top_right.png"));
        if (image != null)
            mcVanillaButtonTopRight = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center_left.png"));
        if (image != null)
            mcVanillaButtonCenterLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center.png"));
        if (image != null)
            mcVanillaButtonCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/center_right.png"));
        if (image != null)
            mcVanillaButtonCenterRight = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_left.png"));
        if (image != null)
            mcVanillaButtonBottomLeft = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_center.png"));
        if (image != null)
            mcVanillaButtonBottomCenter = RenderUtils.createTexture2D(image);

        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/button/vanilla/bottom_right.png"));
        if (image != null)
            mcVanillaButtonBottomRight = RenderUtils.createTexture2D(image);

        ImagePrefab mcVanillaButton = new ImagePrefab(new NinePatchBorder(
                mcVanillaButtonTopLeft,
                mcVanillaButtonTopCenter,
                mcVanillaButtonTopRight,
                mcVanillaButtonCenterLeft,
                mcVanillaButtonCenter,
                mcVanillaButtonCenterRight,
                mcVanillaButtonBottomLeft,
                mcVanillaButtonBottomCenter,
                mcVanillaButtonBottomRight));
        mcVanillaButton.ninePatchBorder.center.tiling = true;

        Texture2D missingTexture = null;
        image = TextureHelper.getBufferedImageFromRl(new ResourceLocation("ingameinfo:gui/missing_texture.png"));
        if (image != null)
            missingTexture = RenderUtils.createTexture2D(image);
        GuiResources.missingTexture = new ImagePrefab(new NinePatchBorder(
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture,
                missingTexture));
        GuiResources.missingTexture.ninePatchBorder.center.tiling = true;
        GuiResources.missingTexture.ninePatchBorder.topLeft.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.topRight.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.bottomLeft.sizeDeductionByPixels = false;
        GuiResources.missingTexture.ninePatchBorder.bottomRight.sizeDeductionByPixels = false;

        RenderHints.setHint_Texture2D$FilterModeMin(filterModeMin);
        RenderHints.setHint_Texture2D$FilterModeMag(filterModeMag);
        RenderHints.setHint_Texture2D$WrapModeS(wrapModeS);
        RenderHints.setHint_Texture2D$WrapModeT(wrapModeT);

        register("vanilla_background", mcVanillaBg);
        register("vanilla_button", mcVanillaButton);
    }
}
