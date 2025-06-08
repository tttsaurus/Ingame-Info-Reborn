package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.event.IgiGuiInitEvent;
import com.tttsaurus.ingameinfo.common.core.event.IgiGuiRegainScreenFocusEvent;
import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.dummy.IDummyDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.delegate.dummy.IDummyKeyTyped;
import com.tttsaurus.ingameinfo.common.core.gui.render.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.Display;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleProvider;

/**
 * Provides the lifecycle foundation for an IGI GUI system.
 *
 * <p>This abstract class is designed to be extended by clients who want fine-grained control
 * over GUI rendering and update timing.</p>
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *     <li>Tracking and managing opened GUI containers</li>
 *     <li>Handling GUI initialization and resolution resize detection</li>
 *     <li>Managing dummy screen activation and deactivation based on GUI focus state</li>
 *     <li>Calling fixed updates and render updates with customizable frame timing</li>
 *     <li>Posting events of regained focus and initial setup through event bus</li>
 * </ul>
 *
 * <p>Subclasses <b>must</b> implement:</p>
 * <ul>
 *     <li>{@link #updateInternal()} for frame-based logic</li>
 *     <li>{@link #getRenderLerpAlpha()} to supply interpolation alpha for rendering</li>
 *     <li>{@link #isUsingFramebuffer()} and {@link #isUsingMultisampleFramebuffer()} to guide render behavior</li>
 *     <li>{@link #getDummyGuiDrawScreen()} to provide dummy GUI draw behavior</li>
 * </ul>
 *
 * <p>Thread safety is not guaranteed; usage must occur on the Minecraft client thread.</p>
 *
 * @see DefaultLifecycleProvider
 */
public abstract class GuiLifecycleProvider
{
    protected static final Minecraft MC = Minecraft.getMinecraft();

    protected ScaledResolution resolution = new ScaledResolution(MC);
    protected final Map<String, IgiGuiContainer> openedGuiMap = new LinkedHashMap<>();

    public final void openIgiGui(String mvvmRegistryName, IgiGuiContainer guiContainer)
    {
        if (openedGuiMap.containsKey(mvvmRegistryName)) return;
        openedGuiMap.put(mvvmRegistryName, guiContainer);
    }
    public final void closeIgiGui(String mvvmRegistryName)
    {
        openedGuiMap.remove(mvvmRegistryName);
    }

    protected abstract void updateInternal();

    private boolean listenRegainScreenFocus = false;
    private boolean initFlag = true;

    private boolean isDummyGuiOn = false;
    protected boolean isDummyGuiOn() { return isDummyGuiOn; }

    public final void update()
    {
        if (initFlag)
        {
            initFlag = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiInitEvent());
        }

        if (!Display.isActive() && !listenRegainScreenFocus)
            listenRegainScreenFocus = true;
        if (Display.isActive() && listenRegainScreenFocus)
        {
            listenRegainScreenFocus = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiRegainScreenFocusEvent());
        }

        //<editor-fold desc="gui container init">
        for (IgiGuiContainer container : openedGuiMap.values())
            if (!container.getInitFlag())
                container.onInit();
        //</editor-fold>

        //<editor-fold desc="gui container resize">
        ScaledResolution newResolution = new ScaledResolution(MC);
        if (resolution.getScaleFactor() != newResolution.getScaleFactor() ||
                resolution.getScaledWidth() != newResolution.getScaledWidth() ||
                resolution.getScaledHeight() != newResolution.getScaledHeight())
        {
            resolution = newResolution;
            for (IgiGuiContainer container: openedGuiMap.values())
                container.onScaledResolutionResize();
        }
        //</editor-fold>

        //<editor-fold desc="dummy gui">
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            // close dummy
            if (isDummyGuiOn)
            {
                if (openedGuiMap.isEmpty())
                {
                    isDummyGuiOn = false;
                    MC.displayGuiScreen(null);
                }
                else
                {
                    AtomicBoolean focus = new AtomicBoolean(false);
                    openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                    if (!focus.get())
                    {
                        isDummyGuiOn = false;
                        MC.displayGuiScreen(null);
                    }
                }
            }
            // open dummy
            else if (!openedGuiMap.isEmpty() && MC.currentScreen == null)
            {
                AtomicBoolean focus = new AtomicBoolean(false);
                openedGuiMap.forEach((uuid, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));

                if (focus.get())
                {
                    MC.displayGuiScreen(getDummyGui());
                    isDummyGuiOn = true;
                }
            }
        }
        //</editor-fold>

        updateInternal();
    }

    //<editor-fold desc="fixed update variables">
    // units are all in second
    protected int maxFps_FixedUpdate = 125;
    protected double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public final void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }
    //</editor-fold>

    protected final void definedFixedUpdate(double deltaTime)
    {
        for (IgiGuiContainer container: openedGuiMap.values())
            container.onFixedUpdate(deltaTime);
    }

    //<editor-fold desc="render update variables">
    // units are all in second
    protected int maxFps_RenderUpdate = 240;
    protected double timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    public final void setMaxFps_RenderUpdate(int fps)
    {
        maxFps_RenderUpdate = fps;
        timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    }
    //</editor-fold>

    protected final void definedRenderUpdate()
    {
        ItemStack heldItemMainhand = null;
        EntityPlayerSP player = MC.player;
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
                    for (GhostableItem item: container.getHeldItemWhitelist())
                        if (item.getItemStack() != null)
                            if (item.getItemStack().isItemEqual(heldItemMainhand))
                                display = true;
                }
                if (container.getUseHeldItemBlacklist())
                {
                    for (GhostableItem item: container.getHeldItemBlacklist())
                        if (item.getItemStack() != null)
                            if (item.getItemStack().isItemEqual(heldItemMainhand))
                                display = false;
                }
            }

            if (display)
            {
                RenderMask.resetStencilCounter();

                RenderContext context = new RenderContext(
                        container.getThemeConfig(),
                        getRenderLerpAlpha(),
                        !isUsingFramebuffer() || isUsingMultisampleFramebuffer(),
                        !isUsingFramebuffer() || isUsingMultisampleFramebuffer());

                RenderOpQueue queue = container.onRenderUpdate(entry.getKey().equals(firstFocused));
                IRenderOp op;
                while ((op = queue.dequeue()) != null)
                    op.execute(context);
            }
        }
    }

    protected abstract boolean isUsingFramebuffer();
    protected abstract boolean isUsingMultisampleFramebuffer();

    protected abstract IDummyDrawScreen getDummyGuiDrawScreen();

    private DummyMcGui getDummyGui()
    {
        DummyMcGui dummyGui = new DummyMcGui();
        dummyGui.setDrawAction(getDummyGuiDrawScreen());
        dummyGui.setTypeAction(new IDummyKeyTyped()
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
                            exitCallback = container.getExitCallback();
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
        return dummyGui;
    }

    public abstract float getRenderLerpAlpha();
}
