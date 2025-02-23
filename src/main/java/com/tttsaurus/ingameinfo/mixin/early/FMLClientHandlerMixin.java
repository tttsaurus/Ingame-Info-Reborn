package com.tttsaurus.ingameinfo.mixin.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.minecraft.WorldLoadingScreenOverhaul;
import com.tttsaurus.ingameinfo.common.impl.render.Texture2D;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.io.File;
import java.io.RandomAccessFile;

@Mixin(FMLClientHandler.class)
public class FMLClientHandlerMixin
{
    @WrapOperation(
            method = "tryLoadExistingWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/storage/WorldSummary;getFileName()Ljava/lang/String;",
                    ordinal = 0
            ))
    public String mixin_tryLoadExistingWorld_WorldSummary$getFileName(WorldSummary instance, Operation<String> original)
    {
        String folderName = original.call(instance);

        // join world
        WorldLoadingScreenOverhaul.setDrawOverlay(true);

        // try load screenshot
        File screenshot = new File("saves/" + folderName + "/last_screenshot.png");
        if (screenshot.exists())
        {
            Texture2D texture = RenderUtils.readPng(screenshot);
            if (texture != null) WorldLoadingScreenOverhaul.updateTexture(texture);
        }

        try
        {
            RandomAccessFile skyColorFile = new RandomAccessFile("saves/" + folderName + "/last_sky_color", "rw");
            WorldLoadingScreenOverhaul.setSkyColor(skyColorFile.readInt());
            skyColorFile.close();
        }
        catch (Exception ignored) { }

        return folderName;
    }
}
