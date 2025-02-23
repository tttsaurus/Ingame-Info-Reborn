package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.impl.minecraft.WorldLoadingScreenOverhaul;
import net.minecraft.client.gui.GuiScreenWorking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreenWorking.class)
public class GuiScreenWorkingMixin
{
    @WrapOperation(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreenWorking;drawDefaultBackground()V"
            ))
    public void mixin_drawScreen_GuiScreenWorking$drawDefaultBackground(GuiScreenWorking instance, Operation<Void> original)
    {
        original.call(instance);

        if (WorldLoadingScreenOverhaul.getDrawOverlay() && !WorldLoadingScreenOverhaul.isTextureNull())
            WorldLoadingScreenOverhaul.drawOverlay();
    }
}
