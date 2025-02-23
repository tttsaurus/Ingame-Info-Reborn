package com.tttsaurus.ingameinfo.common.impl.minecraft;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.render.Texture2D;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.nio.ByteBuffer;

public final class WorldLoadingScreenOverhaul
{
    private static boolean screenShotToggle = false;
    private static boolean drawOverlay = false;
    private static Texture2D texture = null;
    private static ByteBuffer textureBuffer = null;
    private static int skyColor;

    public static void prepareScreenShot() { screenShotToggle = true; }

    public static boolean getDrawOverlay() { return drawOverlay; }
    public static void setDrawOverlay(boolean flag) { drawOverlay = flag; }

    public static void drawOverlay()
    {
        RenderUtils.renderRectFullScreen(skyColor);
        RenderUtils.renderTexture2DFullScreen(texture.getGlTextureID());
    }

    public static boolean isTextureNull() { return texture == null; }
    public static boolean isTextureBufferNull() { return textureBuffer == null; }

    public static int getTextureWidth()
    {
        if (texture == null) return 0;
        return texture.getWidth();
    }
    public static int getTextureHeight()
    {
        if (texture == null) return 0;
        return texture.getHeight();
    }
    public static ByteBuffer getTextureBuffer() { return textureBuffer; }

    public static void updateTexture(Texture2D tex)
    {
        if (texture != null) texture.dispose();
        texture = tex;
    }

    public static void setSkyColor(int color) { skyColor = color; }
    public static int getSkyColor() { return skyColor; }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        if (screenShotToggle)
        {
            screenShotToggle = false;
            if (texture != null) texture.dispose();

            Minecraft minecraft = Minecraft.getMinecraft();
            textureBuffer = RenderUtils.getInGameScreenShotByteBufferFullScreen();
            texture = new Texture2D(minecraft.displayWidth, minecraft.displayHeight, textureBuffer);
        }
    }
}
