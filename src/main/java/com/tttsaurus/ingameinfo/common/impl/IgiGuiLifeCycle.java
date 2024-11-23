package com.tttsaurus.ingameinfo.common.impl;

import com.tttsaurus.ingameinfo.common.api.gui.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.api.gui.layout.*;
import com.tttsaurus.ingameinfo.common.api.gui.PlaceholderMcGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("all")
public final class IgiGuiLifeCycle
{
    private static final IntBuffer intBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
    private static final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();

    private static final int maxFPS = 60;
    private static final double timePerFrame = 1d / maxFPS;
    private static final StopWatch stopwatch = new StopWatch();
    private static double deltaTime = 0d;
    private static double excessTime = 0d;
    private static int estimatedFPS = 0;
    private static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

    public static int getEstimatedFPS() { return estimatedFPS; }

    private static void onFixedUpdate()
    {
        //<editor-fold desc="gui container fixed update">
        for (IgiGuiContainer container: openedGuiQueue)
            container.onFixedUpdate(deltaTime);
        //</editor-fold>
    }
    private static void onRenderUpdate()
    {
        //<editor-fold desc="gui container render update">
        for (IgiGuiContainer container: openedGuiQueue)
            container.onRenderUpdate();
        //</editor-fold>
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        //<editor-fold desc="gui container init">
        for (IgiGuiContainer container: openedGuiQueue)
            if (!container.getInitFlag())
                container.onInit();
        //</editor-fold>

        //<editor-fold desc="store gl states">
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        int textureID = intBuffer.get(0);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, floatBuffer);
        float r = floatBuffer.get(0);
        float g = floatBuffer.get(1);
        float b = floatBuffer.get(2);
        float a = floatBuffer.get(3);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        boolean alphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glGetInteger(GL11.GL_SHADE_MODEL, intBuffer);
        int shadeModel = intBuffer.get(0);
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        //</editor-fold>

        //<editor-fold desc="gui container resize">
        if (resolution.getScaleFactor() != event.getResolution().getScaleFactor())
        {
            resolution = event.getResolution();
            for (IgiGuiContainer container: openedGuiQueue)
                container.onScaledResolutionResize();
        }
        //</editor-fold>

        if (!stopwatch.isStarted())
            stopwatch.start();

        // unit: s
        double currentTime = stopwatch.getTime(TimeUnit.NANOSECONDS) / 1000000000d;
        if (currentTime + excessTime >= timePerFrame)
        {
            stopwatch.stop();
            stopwatch.reset();
            stopwatch.start();

            deltaTime = currentTime;
            estimatedFPS = ((int)(1d / (currentTime + excessTime)) + estimatedFPS) / 2;

            onFixedUpdate();

            excessTime = currentTime + excessTime - timePerFrame;
        }

        if (!isPlaceholderGuiOn)
            onRenderUpdate();

        //<editor-fold desc="release gl states">
        if (depthTest)
            GlStateManager.enableDepth();
        else
            GlStateManager.disableDepth();
        GlStateManager.shadeModel(shadeModel);
        if (alphaTest)
            GlStateManager.enableAlpha();
        else
            GlStateManager.disableAlpha();
        if (texture2D)
            GlStateManager.enableTexture2D();
        else
            GlStateManager.disableTexture2D();
        if (lighting)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
        if (blend)
            GlStateManager.enableBlend();
        else
            GlStateManager.disableBlend();
        GlStateManager.color(r, g, b, a);
        GlStateManager.bindTexture(textureID);
        //</editor-fold>

        //<editor-fold desc="placeholder gui">
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            // close placeholder
            if (isPlaceholderGuiOn)
            {
                if (openedGuiQueue.isEmpty())
                {
                    placeholderGui = null;
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    isPlaceholderGuiOn = false;
                }
                else
                {
                    AtomicBoolean focus = new AtomicBoolean(false);
                    openedGuiQueue.forEach(guiContainer -> focus.set(focus.get() || guiContainer.getFocused()));

                    if (!focus.get())
                    {
                        placeholderGui = null;
                        Minecraft.getMinecraft().displayGuiScreen(null);
                        isPlaceholderGuiOn = false;
                    }
                }
            }
            // open placeholder
            else if (!isPlaceholderGuiOn && !openedGuiQueue.isEmpty() && Minecraft.getMinecraft().currentScreen == null)
            {
                AtomicBoolean focus = new AtomicBoolean(false);
                openedGuiQueue.forEach(guiContainer -> focus.set(focus.get() || guiContainer.getFocused()));

                if (focus.get())
                {
                    placeholderGui = new PlaceholderMcGui();
                    placeholderGui.setDrawAction(new IPlaceholderDrawScreen()
                    {
                        @Override
                        public void draw()
                        {
                            onRenderUpdate();
                        }
                    });
                    Minecraft.getMinecraft().displayGuiScreen(placeholderGui);
                    isPlaceholderGuiOn = true;
                }
            }
        }
        //</editor-fold>

        // testing
        if (flag)
        {
            flag = false;
            IgiGuiContainer container = new IgiGuiContainer();
            container.getMainGroup()
            .add(
                    /*
                    (new HorizontalGroup())
                            .add(
                                    (new HorizontalGroup())
                                            .add(
                                                    (new TextElement("elem 1", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            )
                                            .add(
                                                    (new TextElement("elem 2", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            )
                            )
                    .add(
                            (new TextElement("elem 3", 1, Color.GRAY.getRGB())).setPadding(new Padding(10, 10, 5, 5))
                    )
                    */
                    (new HorizontalGroup())
                            .add(
                                    (new HorizontalGroup())
                                            .add(
                                                    (new TextElement("elem 1", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            )
                                            .add(
                                                    (new TextElement("elem 2", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            ).setPadding(new Padding(5, 5, 5, 5))
                            )
                            .add(
                                    (new VerticalGroup())
                                            .add(
                                                    (new TextElement("elem 3", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            )
                                            .add(
                                                    (new TextElement("elem 4", 1, Color.GRAY.getRGB())).setPadding(new Padding(3, 3, 3, 3))
                                            ).setPadding(new Padding(5, 5, 5, 5))
                            )
                    .setPadding(new Padding(0, 0, 0, 20))
                    .setAlignment(Alignment.MIDDLE)
                    .setPivot(Pivot.TOP_RIGHT)
            );
            openIgiGui(container);
        }
    }

    static boolean flag = true;

    private static boolean isPlaceholderGuiOn = false;
    private static PlaceholderMcGui placeholderGui;
    private static Queue<IgiGuiContainer> openedGuiQueue = new PriorityQueue<>();

    public static void openIgiGui(IgiGuiContainer guiContainer)
    {
        openedGuiQueue.add(guiContainer);
    }
}
