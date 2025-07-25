package com.tttsaurus.ingameinfo.plugin.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import javax.annotation.Nonnull;

@JEIPlugin
public class JeiCompatPlugin implements IModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.addAdvancedGuiHandlers(new JeiExclusionHandler());
    }
}
