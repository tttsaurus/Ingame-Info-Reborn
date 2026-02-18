package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.core.IgiRuntimeLocator;
import com.tttsaurus.ingameinfo.common.core.gui.screen.GuiScreenDrawScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin
{
    @WrapOperation(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V",
                    remap = true
            ),
            remap = false)
    private static void drawScreen(GuiScreen instance, int j, int i, float mouseX, Operation<Void> original)
    {
        original.call(instance, j, i, mouseX);

        if (IgiRuntimeLocator.get() == null) return;
        if (IgiRuntimeLocator.get().livePhase.isOccupyingScreen())
        {
            for (GuiScreenDrawScreen delegate: IgiRuntimeLocator.get().livePhase.collectDrawScreenDelegatesIfScreenOccupied())
                delegate.draw();
        }
    }
}
