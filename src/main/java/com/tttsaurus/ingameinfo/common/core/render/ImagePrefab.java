package com.tttsaurus.ingameinfo.common.core.render;

public class ImagePrefab
{
    public enum InternalType
    {
        TEXTURE_2D,
        NINE_PATCH_BORDER
    }

    public final InternalType type;
    public final Texture2D texture2D;
    public final NinePatchBorder ninePatchBorder;

    public ImagePrefab(Texture2D texture2D)
    {
        type = InternalType.TEXTURE_2D;
        this.texture2D = texture2D;
        ninePatchBorder = null;
    }

    public ImagePrefab(NinePatchBorder ninePatchBorder)
    {
        type = InternalType.TEXTURE_2D;
        texture2D = null;
        this.ninePatchBorder = ninePatchBorder;
    }
}
