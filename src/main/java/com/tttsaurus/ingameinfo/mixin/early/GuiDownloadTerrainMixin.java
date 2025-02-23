package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.impl.minecraft.WorldLoadingScreenOverhaul;
import net.minecraft.client.gui.GuiDownloadTerrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiDownloadTerrain.class)
public class GuiDownloadTerrainMixin
{
    @WrapOperation(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V"
            ))
    public void mixin_drawScreen_GuiScreen$drawScreen(GuiDownloadTerrain instance, int j, int i, float mouseX, Operation<Void> original)
    {
        original.call(instance, j, i, mouseX);

        if (WorldLoadingScreenOverhaul.getDrawOverlay() && !WorldLoadingScreenOverhaul.isTextureNull())
            WorldLoadingScreenOverhaul.drawOverlay();
    }
}
