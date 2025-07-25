package com.tttsaurus.ingameinfo.plugin.jei;

import com.tttsaurus.ingameinfo.common.core.IgiRuntimeLocator;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class JeiExclusionHandler implements IAdvancedGuiHandler<GuiContainer>
{
    @Override
    public @Nonnull Class<GuiContainer> getGuiContainerClass()
    {
        return GuiContainer.class;
    }

    @Override
    public @Nullable List<Rectangle> getGuiExtraAreas(@Nonnull GuiContainer guiContainer)
    {
        if (IgiRuntimeLocator.get().livePhase.isOccupyingScreen())
        {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            return Collections.singletonList(new Rectangle(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight()));
        }
        else
            return null;
    }
}
