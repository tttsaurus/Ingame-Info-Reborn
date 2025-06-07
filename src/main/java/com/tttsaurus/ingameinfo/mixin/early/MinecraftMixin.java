package com.tttsaurus.ingameinfo.mixin.early;

import com.tttsaurus.ingameinfo.InGameInfoReborn;
import com.tttsaurus.ingameinfo.experimental.HWNDGetterLwjgl2;
import com.tttsaurus.ingameinfo.common.core.function.IAction;
import com.tttsaurus.ingameinfo.common.core.shutdown.ShutdownHooks;
import com.tttsaurus.ingameinfo.experimental.WindowsChecker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void beforeShutdown(CallbackInfo ci)
    {
        for (IAction action: ShutdownHooks.hooks)
            action.invoke();
    }

    // just created GL context
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createDisplay()V", shift = At.Shift.AFTER))
    private void afterCreateDisplay(CallbackInfo info)
    {
        if (WindowsChecker.isWindows() && !InGameInfoReborn.isCleanroom())
        {
            InGameInfoReborn.LOGGER.info("HWND: " + HWNDGetterLwjgl2.getHWND());
        }
    }
}
