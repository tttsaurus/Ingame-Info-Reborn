package com.tttsaurus.ingameinfo.common.core.render.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import java.io.IOException;

public class McTextureWrapper extends AbstractTexture
{
    private final ITexture2D internal;

    public McTextureWrapper(ITexture2D internal)
    {
        this.internal = internal;
        if (internal.isGlRegistered()) glTextureId = internal.getGlTextureID();
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException { }
}
