package com.tttsaurus.ingameinfo.common.impl.gui;

import com.tttsaurus.ingameinfo.common.api.gui.GuiLayout;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGui;
import com.tttsaurus.ingameinfo.common.api.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderDrawScreen;
import com.tttsaurus.ingameinfo.common.api.gui.delegate.placeholder.IPlaceholderKeyTyped;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.impl.gui.control.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("all")
public final class IgiGuiLifeCycle
{
    private static final int maxFPS = 75;
    private static final double timePerFrame = 1d / maxFPS;
    private static final StopWatch stopwatch = new StopWatch();
    private static double deltaTime = 0d;
    private static double excessTime = 0d;
    private static int estimatedFPS = 0;
    private static ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

    public static int getEstimatedFPS() { return estimatedFPS; }

    // fixed update fps debug
    private static boolean displayDelayedFPS = false;
    private static int delayedFPS = 0;
    private static double timer = 0.5f;

    public static int getDelayedFPS() { return delayedFPS; }

    private static void onFixedUpdate()
    {
        //<editor-fold desc="gui container fixed update">
        for (IgiGuiContainer container: openedGuiList.values())
            container.onFixedUpdate(deltaTime);
        //</editor-fold>

        timer += deltaTime;
        if (timer >= 0.5d)
        {
            timer -= 0.5d;
            delayedFPS = estimatedFPS;
        }
    }
    private static void onRenderUpdate()
    {
        ItemStack heldItemMainhand = Minecraft.getMinecraft().player.getHeldItemMainhand();

        //<editor-fold desc="gui container render update">

        List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiList.entrySet());
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
        for (Map.Entry<String, IgiGuiContainer> entry: openedGuiList.entrySet())
        {
            IgiGuiContainer container = entry.getValue();
            boolean display = true;

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

            if (display)
                if (entry.getKey().equals(firstFocused))
                    container.onRenderUpdate(true);
                else
                    container.onRenderUpdate(false);
        }
        //</editor-fold>

        if (displayDelayedFPS)
        {
            String str = "FPS: " + delayedFPS;
            int width = RenderUtils.fontRenderer.getStringWidth(str) + 4;
            RenderUtils.renderRoundedRect(10, 10, width, 15, 5, Color.DARK_GRAY.getRGB());
            RenderUtils.renderRoundedRectOutline(10, 10, width, 15, 5, 1, Color.LIGHT_GRAY.getRGB());
            RenderUtils.renderText(str, 12, 13.5f, 1, Color.LIGHT_GRAY.getRGB(), true);
        }
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
        for (IgiGuiContainer container : openedGuiList.values())
            if (!container.getInitFlag())
                container.onInit();
        //</editor-fold>

        if (!isPlaceholderGuiOn) storeCommonGlStates();

        //<editor-fold desc="gui container resize">
        if (resolution.getScaleFactor() != event.getResolution().getScaleFactor())
        {
            resolution = event.getResolution();
            for (IgiGuiContainer container: openedGuiList.values())
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
                if (openedGuiList.isEmpty())
                {
                    isPlaceholderGuiOn = false;
                    placeholderGui = null;
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
                else
                {
                    AtomicBoolean focus = new AtomicBoolean(false);
                    openedGuiList.forEach((uuid, guiContainer) -> focus.set(focus.get() || guiContainer.getFocused()));

                    if (!focus.get())
                    {
                        isPlaceholderGuiOn = false;
                        placeholderGui = null;
                        Minecraft.getMinecraft().displayGuiScreen(null);
                    }
                }
            }
            // open placeholder
            else if (!isPlaceholderGuiOn && !openedGuiList.isEmpty() && Minecraft.getMinecraft().currentScreen == null)
            {
                AtomicBoolean focus = new AtomicBoolean(false);
                openedGuiList.forEach((uuis, guiContainer) -> focus.set(focus.get() || guiContainer.getFocused()));

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
                            List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(openedGuiList.entrySet());
                            String key = "";
                            for (int i = entryList.size() - 1; i >= 0; i--)
                            {
                                Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
                                IgiGuiContainer container = entry.getValue();
                                if (container.getFocused())
                                    if (keycode == container.getExitKeyForFocusedGui())
                                    {
                                        key = entry.getKey();
                                        break;
                                    }
                            }
                            if (!key.isEmpty()) openedGuiList.remove(key);
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
            GuiLayout builder = IgiGui.getBuilder();
            builder
                    .setDebug(false)
                    .setHeldItemWhitelist(true)
                    .addHeldItemWhitelist(new ItemStack(Items.APPLE))
                    .startHorizontalGroup()
                    .addElement(new Text("test1", 2f, Color.RED.getRGB()),
                                "\"alignment\" : BOTTOM_LEFT, \"pivot\" : BOTTOM_RIGHT, \"backgroundStyle\" : \"roundedBoxWithOutline\"")
                    .startVerticalGroup()
                    .addElement(new Text("test2", 2f, Color.GRAY.getRGB()))
                    .addElement(new Text("test3", 2f, Color.GRAY.getRGB()))
                    .endGroup()
                    .endGroup();
            IgiGui.openGui(builder);
        }
    }

    private static boolean flag = true;

    // placeholder related
    private static boolean isPlaceholderGuiOn = false;
    private static PlaceholderMcGui placeholderGui;

    private static Map<String, IgiGuiContainer> openedGuiList = new LinkedHashMap<>();

    public static String openIgiGui(IgiGuiContainer guiContainer)
    {
        String uuid = UUID.randomUUID().toString();
        openedGuiList.put(uuid, guiContainer);
        return uuid;
    }
    public static void closeIgiGui(String uuid)
    {
        openedGuiList.remove(uuid);
    }
}

// todo 4. inject system
// todo 5. event publisher
