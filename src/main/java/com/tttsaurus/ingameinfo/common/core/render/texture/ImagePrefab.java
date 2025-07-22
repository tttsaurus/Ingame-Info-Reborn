package com.tttsaurus.ingameinfo.common.core.render.texture;

import javax.annotation.Nonnull;

public class ImagePrefab
{
    public enum InternalType
    {
        TEXTURE_2D,
        TEXTURE_SLICED_2D,
        NINE_PATCH_BORDER
    }

    public final InternalType type;
    public final Texture2D texture2D;
    public final TextureSliced2D textureSliced2D;
    public final NinePatchBorder ninePatchBorder;

    public ImagePrefab(@Nonnull Texture2D texture2D)
    {
        type = InternalType.TEXTURE_2D;
        this.texture2D = texture2D;
        textureSliced2D = null;
        ninePatchBorder = null;
    }

    public ImagePrefab(@Nonnull TextureSliced2D textureSliced2D)
    {
        type = InternalType.TEXTURE_SLICED_2D;
        texture2D = null;
        this.textureSliced2D = textureSliced2D;
        ninePatchBorder = null;
    }

    public ImagePrefab(@Nonnull NinePatchBorder ninePatchBorder)
    {
        type = InternalType.NINE_PATCH_BORDER;
        texture2D = null;
        textureSliced2D = null;
        this.ninePatchBorder = ninePatchBorder;
    }
}
