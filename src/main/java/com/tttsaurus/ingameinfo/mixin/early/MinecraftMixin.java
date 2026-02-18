package com.tttsaurus.ingameinfo.mixin.early;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.common.core.function.Action;
import com.tttsaurus.ingameinfo.common.core.render.RenderHints;
import com.tttsaurus.ingameinfo.common.core.shutdown.ShutdownHooks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;unloadSounds()V", shift = At.Shift.AFTER))
    public void shutdown(CallbackInfo ci)
    {
        for (Action action: ShutdownHooks.hooks)
            action.invoke();
    }

    // just created GL context
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createDisplay()V", shift = At.Shift.AFTER))
    private void afterCreateDisplay(CallbackInfo info)
    {
        //<editor-fold desc="rendering setup">
        int majorGlVersion = RenderHints.getMajorGlVersion();
        int minorGlVersion = RenderHints.getMinorGlVersion();

        InGameInfoReborn.LOGGER.info("Raw OpenGL version: " + RenderHints.getRawGlVersion());
        InGameInfoReborn.LOGGER.info(String.format("OpenGL version: %d.%d", majorGlVersion, minorGlVersion));

        // init getters
        RenderHints.getModelViewMatrix();
        InGameInfoReborn.LOGGER.info("The getters of ActiveRenderInfo private fields are ready.");
        RenderHints.getPartialTick();
        InGameInfoReborn.LOGGER.info("The getter of private partial tick field is ready.");
        //</editor-fold>
    }
}
