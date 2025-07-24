package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.core.IgiRuntimeLocator;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenKeyTyped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreen.class)
public class GuiScreenMixin
{
    @WrapOperation(
            method = "handleInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;handleMouseInput()V"
            ))
    public void handleMouseInput(GuiScreen instance, Operation<Void> original)
    {
        if (IgiRuntimeLocator.get().livePhase.isOccupyingScreen()) return;

        original.call(instance);
    }

    @WrapOperation(
            method = "handleInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;handleKeyboardInput()V"
            ))
    public void handleKeyboardInput(GuiScreen instance, Operation<Void> original)
    {
        if (IgiRuntimeLocator.get().livePhase.isOccupyingScreen())
        {
            char c0 = Keyboard.getEventCharacter();

            if (Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState())
                for (IGuiScreenKeyTyped delegate: IgiRuntimeLocator.get().livePhase.collectKeyTypedDelegatesIfScreenOccupied())
                    delegate.type(Keyboard.getEventKey());

            Minecraft.getMinecraft().dispatchKeypresses();
            return;
        }

        original.call(instance);
    }

    @WrapMethod(method = "drawScreen")
    public void drawScreen(int mouseX, int mouseY, float partialTicks, Operation<Void> original)
    {
        if (IgiRuntimeLocator.get().livePhase.isOccupyingScreen())
        {
            for (IGuiScreenDrawScreen delegate: IgiRuntimeLocator.get().livePhase.collectDrawScreenDelegatesIfScreenOccupied())
                delegate.draw();
            return;
        }

        original.call(mouseX, mouseY, partialTicks);
    }
}
