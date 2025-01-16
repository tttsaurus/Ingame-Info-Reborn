package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderKeyTyped;
import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.lang3.time.StopWatch;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("all")
public final class IgiGuiLifeCycle
{
    //<editor-fold desc="fixed update variables">
    private static int maxFps_FixedUpdate = 125;
    private static double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public static void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }
    private static int estimatedFps_FixedUpdate = 0;
    private static double deltaTime_FixedUpdate = 0d;
    private static double excessTime_FixedUpdate = 0d;
    private static final StopWatch stopwatch_FixedUpdate = new StopWatch();
    //</editor-fold>

    //<editor-fold desc="render update variables">
    private static int maxFps_RenderUpdate = 240;
    private static double timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    public static void setMaxFps_RenderUpdate(int fps)
    {
        maxFps_RenderUpdate = fps;
        timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    }
    private static int estimatedFps_RenderUpdate = 0;
    private static double deltaTime_RenderUpdate = 0d;
    private static double excessTime_RenderUpdate = 0d;
    private static final StopWatch stopwatch_RenderUpdate = new StopWatch();
    private static int estimatedUnlimitedFps = 1;
    private static float estimatedFboRefreshRate = 0f;

    private static boolean enableFbo = true;
    public static void setEnableFbo(boolean flag) { enableFbo = flag; }
    private static boolean refreshFbo = true;
    private static Framebuffer fbo = null;
    private static int fboDisplayWidth;
    private static int fboDisplayHeight;
    //</editor-fold>

    private static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

    private static String lastBiomeRegistryName = "";
    private static void triggerIgiEvents()
    {
        // trigger builtin igi events
        EventCenter.igiGuiFpsEvent.trigger(estimatedFps_FixedUpdate, estimatedFps_RenderUpdate);
        EventCenter.igiGuiFboRefreshRateEvent.trigger(estimatedFboRefreshRate);
        EventCenter.gameFpsEvent.trigger(Minecraft.getDebugFPS());
        Runtime runtime = Runtime.getRuntime();
        EventCenter.gameMemoryEvent.trigger(runtime.totalMemory() - runtime.freeMemory(), runtime.totalMemory());
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
        {
            Biome biome = player.world.getBiome(player.getPosition());
            ResourceLocation rl = biome.getRegistryName();
            if (rl != null)
            {
                String biomeRegistryName = rl.toString();
                if (!biomeRegistryName.equals(lastBiomeRegistryName))
                {
                    lastBiomeRegistryName = biomeRegistryName;
                    EventCenter.enterBiomeEvent.trigger(biome.getBiomeName(), biomeRegistryName);
                }
            }
        }
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null && server.isServerRunning())
        {
            long[] tickTimes = server.tickTimeArray;
            double averageTickTime = 0d;

            for (long tickTime : tickTimes)
                averageTickTime += tickTime / 1.0E6d;
            averageTickTime /= tickTimes.length;

            int tps = (int)(Math.min(1000d / averageTickTime, 20d));
            EventCenter.gameTpsMtpsEvent.trigger(tps, averageTickTime);
        }
    }

    private static double timer = 0.5f;
    private static void onFixedUpdate()
    {
        //<editor-fold desc="gui container fixed update">
        for (IgiGuiContainer container: openedGuiMap.values())
            container.onFixedUpdate(deltaTime_FixedUpdate);
        //</editor-fold>

        timer += deltaTime_FixedUpdate;
        if (timer >= 0.5d)
        {
            timer -= 0.5d;
            triggerIgiEvents();
        }
    }
    private static void onRenderUpdate()
    {
        boolean useFbo = enableFbo && OpenGlHelper.framebufferSupported;
        if (useFbo)
        {
            // init fbo
            if (fbo == null)
            {
                Minecraft minecraft = Minecraft.getMinecraft();
                fboDisplayWidth = minecraft.displayWidth;
                fboDisplayHeight = minecraft.displayHeight;
                fbo = new Framebuffer(fboDisplayWidth, fboDisplayHeight, true);
                fbo.enableStencil();
                fbo.framebufferColor[0] = 0f;
                fbo.framebufferColor[1] = 0f;
                fbo.framebufferColor[2] = 0f;
            }
            if (!refreshFbo)
            {
                // render fbo
                RenderUtils.renderFbo(resolution, fbo);
                return;
            }
            refreshFbo = false;
            fboSetupStep1();
        }

        //<editor-fold desc="gui container render update">
        ItemStack heldItemMainhand = null;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
            heldItemMainhand = player.getHeldItemMainhand();

        List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiMap.entrySet());
        String firstFocused = "";
        for (int i = entryList.size() - 1; i >= 0; i--)
        {
            Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
            if (entry.getValue().getFocused())
            {
                firstFocused = entry.getKey();
                break;
            }
        }
        for (Map.Entry<String, IgiGuiContainer> entry: openedGuiMap.entrySet())
        {
            IgiGuiContainer container = entry.getValue();
            boolean display = true;

            if (heldItemMainhand != null)
            {
                if (container.getUseHeldItemWhitelist())
                {
                    display = false;
                    for (ItemStack itemStack: container.getHeldItemWhitelist())
                        if (itemStack.isItemEqual(heldItemMainhand))
                            display = true;
                }
                if (container.getUseHeldItemBlacklist())
                {
                    for (ItemStack itemStack: container.getHeldItemBlacklist())
                        if (itemStack.isItemEqual(heldItemMainhand))
                            display = false;
                }
            }

            if (display)
                container.onRenderUpdate(entry.getKey().equals(firstFocused));
        }
        //</editor-fold>

        if (useFbo)
        {
            fboSetupStep2();
            // update & render fbo
            RenderUtils.renderFbo(resolution, fbo);
        }
    }

    private static void fboSetupStep1()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (fboDisplayWidth != minecraft.displayWidth || fboDisplayHeight != minecraft.displayHeight)
        {
            fboDisplayWidth = minecraft.displayWidth;
            fboDisplayHeight = minecraft.displayHeight;
            fbo.createBindFramebuffer(fboDisplayWidth, fboDisplayHeight);
        }
        else
            fbo.framebufferClear();

        fbo.bindFramebuffer(true);
    }
    private static void fboSetupStep2()
    {
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
    }

    //<editor-fold desc="gl states">
    private static int textureID = 0;
    private static float r = 0, g = 0, b = 0, a = 0;
    private static boolean blend = false;
    private static boolean lighting = false;
    private static boolean texture2D = false;
    private static boolean alphaTest = false;
    private static int shadeModel = 0;
    private static boolean depthTest = false;
    private static boolean cullFace = false;

    private static final IntBuffer intBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
    private static final FloatBuffer floatBuffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    //</editor-fold>

    private static void storeCommonGlStates()
    {
        GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, intBuffer);
        textureID = intBuffer.get(0);
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, floatBuffer);
        r = floatBuffer.get(0);
        g = floatBuffer.get(1);
        b = floatBuffer.get(2);
        a = floatBuffer.get(3);
        blend = GL11.glIsEnabled(GL11.GL_BLEND);
        lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        alphaTest = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glGetInteger(GL11.GL_SHADE_MODEL, intBuffer);
        shadeModel = intBuffer.get(0);
        depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
    }
    private static void restoreCommonGlStates()
    {
        if (cullFace)
            GlStateManager.enableCull();
        else
            GlStateManager.disableCull();
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
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        //<editor-fold desc="gui container init">
        for (IgiGuiContainer container : openedGuiMap.values())
            if (!container.getInitFlag())
                container.onInit();
        //</editor-fold>

        //<editor-fold desc="gui container resize">
        if (resolution.getScaleFactor() != event.getResolution().getScaleFactor())
        {
            resolution = event.getResolution();
            for (IgiGuiContainer container: openedGuiMap.values())
                container.onScaledResolutionResize();
        }
        //</editor-fold>

        //<editor-fold desc="fixed update timing">
        if (!stopwatch_FixedUpdate.isStarted())
            stopwatch_FixedUpdate.start();

        // unit: s
        double currentTime = stopwatch_FixedUpdate.getTime(TimeUnit.NANOSECONDS) / 1.0E9d;
        if (currentTime + excessTime_FixedUpdate >= timePerFrame_FixedUpdate)
        {
            stopwatch_FixedUpdate.stop();
            stopwatch_FixedUpdate.reset();
            stopwatch_FixedUpdate.start();

            deltaTime_FixedUpdate = currentTime;
            estimatedFps_FixedUpdate = ((int)(1d / (currentTime + excessTime_FixedUpdate)) + estimatedFps_FixedUpdate) / 2;

            onFixedUpdate();

            excessTime_FixedUpdate = currentTime + excessTime_FixedUpdate - timePerFrame_FixedUpdate;
        }
        //</editor-fold>

        //<editor-fold desc="render update timing">
        if (!stopwatch_RenderUpdate.isStarted())
            stopwatch_RenderUpdate.start();

        // unit: s
        currentTime = stopwatch_RenderUpdate.getTime(TimeUnit.NANOSECONDS) / 1.0E9d;
        estimatedUnlimitedFps = ((int)(1d / currentTime) + estimatedUnlimitedFps) / 2;
        estimatedFboRefreshRate = (Math.min(((float)estimatedFps_RenderUpdate) / ((float)estimatedUnlimitedFps), 1f) + estimatedFboRefreshRate) / 2f;
        if (currentTime + excessTime_RenderUpdate >= timePerFrame_RenderUpdate)
        {
            stopwatch_RenderUpdate.stop();
            stopwatch_RenderUpdate.reset();
            stopwatch_RenderUpdate.start();

            deltaTime_RenderUpdate = currentTime;
            estimatedFps_RenderUpdate = ((int)(1d / (currentTime + excessTime_RenderUpdate)) + estimatedFps_RenderUpdate) / 2;

            refreshFbo = true;

            excessTime_RenderUpdate = currentTime + excessTime_RenderUpdate - timePerFrame_RenderUpdate;
        }
        //</editor-fold>

        //<editor-fold desc="placeholder gui">
        if (!isPlaceholderGuiOn)
        {
            storeCommonGlStates();
            onRenderUpdate();
            restoreCommonGlStates();
        }
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            // close placeholder
            if (isPlaceholderGuiOn)
            {
                if (openedGuiMap.isEmpty())
                {
                    isPlaceholderGuiOn = false;
                    placeholderGui = null;
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
                else
                {
                    AtomicBoolean focus = new AtomicBoolean(false);
                    openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                    if (!focus.get())
                    {
                        isPlaceholderGuiOn = false;
                        placeholderGui = null;
                        Minecraft.getMinecraft().displayGuiScreen(null);
                    }
                }
            }
            // open placeholder
            else if (!openedGuiMap.isEmpty() && Minecraft.getMinecraft().currentScreen == null)
            {
                AtomicBoolean focus = new AtomicBoolean(false);
                openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                if (focus.get())
                {
                    placeholderGui = new PlaceholderMcGui();
                    placeholderGui.setDrawAction(new IPlaceholderDrawScreen()
                    {
                        @Override
                        public void draw()
                        {
                            storeCommonGlStates();
                            onRenderUpdate();
                            restoreCommonGlStates();
                        }
                    });
                    placeholderGui.setTypeAction(new IPlaceholderKeyTyped()
                    {
                        @Override
                        public void type(int keycode)
                        {
                            List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiMap.entrySet());
                            String key = "";
                            IFunc<Boolean> exitCallback = null;
                            for (int i = entryList.size() - 1; i >= 0; i--)
                            {
                                Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
                                IgiGuiContainer container = entry.getValue();
                                if (container.getFocused())
                                    if (keycode == container.getExitKeyForFocusedGui())
                                    {
                                        key = entry.getKey();
                                        exitCallback = entry.getValue().getExitCallback();
                                        break;
                                    }
                            }
                            if (!key.isEmpty())
                            {
                                if (exitCallback.invoke())
                                    openedGuiMap.remove(key);
                            }
                        }
                    });
                    Minecraft.getMinecraft().displayGuiScreen(placeholderGui);
                    isPlaceholderGuiOn = true;
                }
            }
        }
        //</editor-fold>

        if (initFlag)
        {
            initFlag = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiInitEvent());
        }
    }

    private static boolean initFlag = true;

    // placeholder related
    private static boolean isPlaceholderGuiOn = false;
    private static PlaceholderMcGui placeholderGui;

    private static final Map<String, IgiGuiContainer> openedGuiMap = new LinkedHashMap<>();

    public static String openIgiGui(IgiGuiContainer guiContainer)
    {
        String uuid = UUID.randomUUID().toString();
        openedGuiMap.put(uuid, guiContainer);
        return uuid;
    }
    public static void closeIgiGui(String uuid)
    {
        openedGuiMap.remove(uuid);
    }
}

// todo: add button group to handle complex button setup
// todo: optimize reCalc logic