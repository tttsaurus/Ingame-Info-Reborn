package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
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
    private static void mixin_bindTexture_GL11$glBindTexture(int target, int texture, Operation<Void> original)
    {
        if (RenderHints.getHint_GlStateManager$BindTextureHint() == RenderHints.GlStateManager.BindTextureHint.TEXTURE_2D)
            original.call(target, texture);
        else if (RenderHints.getHint_GlStateManager$BindTextureHint() == RenderHints.GlStateManager.BindTextureHint.TEXTURE_2D_MULTISAMPLE)
            GL11.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, texture);
    }
}
