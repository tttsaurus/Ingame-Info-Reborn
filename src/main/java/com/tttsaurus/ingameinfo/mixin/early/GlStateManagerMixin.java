package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.InGameInfoReborn;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlStateManager.class)
public class GlStateManagerMixin
{
    @WrapOperation(
            method = "bindTexture",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"
            ))
    private static void mixin_GL11$glBindTexture_in_bindTexture(int target, int texture, Operation<Void> original)
    {
        original.call(target, texture);
    }
}
