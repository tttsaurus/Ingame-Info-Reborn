package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderKeyTyped;
import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.impl.igievent.EventCenter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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
    // fixed update limit
    private static int maxFps_FixedUpdate = 125;
    private static double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public static void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }
    private static int estimatedFps_FixedUpdate = 0;
    private static final StopWatch stopwatch = new StopWatch();
    private static double deltaTime = 0d;
    private static double excessTime = 0d;

    // todo: render update limit
    private static int maxFps_RenderUpdate = 240;
    private static double timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    public static void setMaxFps_RenderUpdate(int fps)
    {
        maxFps_RenderUpdate = fps;
        timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    }

    private static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

    private static String lastBiomeRegistryName = "";
    private static void triggerIgiEvents()
    {
        // trigger builtin igi events
        EventCenter.igiGuiFpsEvent.trigger(estimatedFps_FixedUpdate);
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
            container.onFixedUpdate(deltaTime);
        //</editor-fold>

        timer += deltaTime;
        if (timer >= 0.5d)
        {
            timer -= 0.5d;
            triggerIgiEvents();
        }
    }
    private static void onRenderUpdate()
    {
        ItemStack heldItemMainhand = null;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null)
            heldItemMainhand = player.getHeldItemMainhand();

        //<editor-fold desc="gui container render update">
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

        if (!isPlaceholderGuiOn) storeCommonGlStates();

        //<editor-fold desc="gui container resize">
        if (resolution.getScaleFactor() != event.getResolution().getScaleFactor())
        {
            resolution = event.getResolution();
            for (IgiGuiContainer container: openedGuiMap.values())
                container.onScaledResolutionResize();
        }
        //</editor-fold>

        if (!stopwatch.isStarted())
            stopwatch.start();

        // unit: s
        double currentTime = stopwatch.getTime(TimeUnit.NANOSECONDS) / 1.0E9d;
        if (currentTime + excessTime >= timePerFrame_FixedUpdate)
        {
            stopwatch.stop();
            stopwatch.reset();
            stopwatch.start();

            deltaTime = currentTime;
            estimatedFps_FixedUpdate = ((int)(1d / (currentTime + excessTime)) + estimatedFps_FixedUpdate) / 2;

            onFixedUpdate();

            excessTime = currentTime + excessTime - timePerFrame_FixedUpdate;
        }

        if (!isPlaceholderGuiOn)
        {
            onRenderUpdate();
            restoreCommonGlStates();
        }

        //<editor-fold desc="placeholder gui">
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
// todo: added fps limit to render update and apply framebuffer