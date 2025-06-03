package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.core.render.IGlDisposable;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.nio.IntBuffer;

@Mixin(Framebuffer.class)
public class FramebufferMixin implements IGlDisposable
{
    @SuppressWarnings("all")
    @Override
    public void dispose()
    {
        ((Framebuffer)(Object)this).deleteFramebuffer();
    }

    @WrapOperation(
            method = "createFramebuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;glTexImage2D(IIIIIIIILjava/nio/IntBuffer;)V"
            ))
    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, IntBuffer pixels, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D)
            original.call(target, level, internalFormat, width, height, border, format, type, pixels);
        else if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE)
            GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, RenderHints.getHint_Framebuffer$FramebufferSampleNum(), GL11.GL_RGBA8, width, height, true);
    }

    @WrapOperation(
            method = "createFramebuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OpenGlHelper;glFramebufferTexture2D(IIIII)V"
            ))
    public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D)
            original.call(target, attachment, textarget, texture, level);
        else if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE)
            OpenGlHelper.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL32.GL_TEXTURE_2D_MULTISAMPLE, texture, 0);
    }

    @WrapOperation(
            method = "createFramebuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/shader/Framebuffer;setFramebufferFilter(I)V"
            ))
    public void setFramebufferFilter(Framebuffer instance, int framebufferFilterIn, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D)
            original.call(instance, framebufferFilterIn);
        else if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE)
        {
            // no need to set param to multisample texture
        }
    }

    @WrapOperation(
            method = "createFramebuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OpenGlHelper;glRenderbufferStorage(IIII)V",
                    ordinal = 0
            ))
    public void glRenderbufferStorage_0(int target, int internalFormat, int width, int height, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D)
            original.call(target, internalFormat, width, height);
        else if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE)
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, RenderHints.getHint_Framebuffer$FramebufferSampleNum(), GL14.GL_DEPTH_COMPONENT24, width, height);
    }

    @WrapOperation(
            method = "createFramebuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OpenGlHelper;glRenderbufferStorage(IIII)V",
                    ordinal = 1
            ))
    public void glRenderbufferStorage_1(int target, int internalFormat, int width, int height, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D)
            original.call(target, internalFormat, width, height);
        else if (RenderHints.getHint_Framebuffer$CreateFramebufferHint() == RenderHints.Framebuffer.CreateFramebufferHint.TEXTURE_2D_MULTISAMPLE)
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, RenderHints.getHint_Framebuffer$FramebufferSampleNum(), EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT, width, height);
    }

    @WrapOperation(
            method = "framebufferClear",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/shader/Framebuffer;unbindFramebuffer()V"
            ))
    public void unbindFramebuffer(Framebuffer instance, Operation<Void> original)
    {
        if (RenderHints.getHint_Framebuffer$FramebufferClearHint() == RenderHints.Framebuffer.FramebufferClearHint.UNBIND_FBO)
            original.call(instance);
        else if (RenderHints.getHint_Framebuffer$FramebufferClearHint() == RenderHints.Framebuffer.FramebufferClearHint.DONT_UNBIND_FBO)
        {
            // don't unbind
        }
    }
}
