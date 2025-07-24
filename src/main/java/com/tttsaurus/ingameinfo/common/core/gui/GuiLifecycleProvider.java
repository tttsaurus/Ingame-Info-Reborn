package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleInitEvent;
import com.tttsaurus.ingameinfo.common.core.forgeevent.IgiGuiLifecycleRegainScreenFocusEvent;
import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenDrawScreen;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IGuiScreenKeyTyped;
import com.tttsaurus.ingameinfo.common.core.gui.screen.IgiDummyScreen;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderOpPhase;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.IVisualModifier;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.VisualBuilder;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.VisualBuilderAccessor;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.IArgsGenerator;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.visual.command.VisualCommand;
import com.tttsaurus.ingameinfo.common.core.gui.render.op.IRenderOp;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderContext;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.input.InputState;
import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.render.RenderMask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
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
 *     <li>{@link #timing()} for frame-based logic</li>
 *     <li>{@link #fixedUpdate()} that wraps {@link #definedFixedUpdate(double)}</li>
 *     <li>{@link #renderUpdate()} that wraps {@link #definedRenderUpdate()}</li>
 *     <li>{@link #getRenderLerpAlpha()} to supply interpolation alpha for rendering</li>
 *     <li>{@link #isUsingFramebuffer()} and {@link #isUsingMultisampleFramebuffer()} to guide render behavior</li>
 * </ul>
 *
 * <p>Thread safety is not guaranteed; usage must occur on the Minecraft client thread.</p>
 *
 * @see DefaultLifecycleProvider
 */
public abstract class GuiLifecycleProvider
{
    @SuppressWarnings("all")
    private String lifecycleHolderName = "";
    public String getLifecycleHolderName() { return lifecycleHolderName; }

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

    private boolean listenRegainScreenFocus = false;
    private boolean initFlag = true;
    private boolean finishFirstUpdate = false;

    private boolean isUsingDummyScreen = false;
    private boolean isUsingOtherScreen = false;
    public final boolean isUsingOtherScreen() { return isUsingOtherScreen; }

    public final void update(InputState inputState)
    {
        // in-game: underFocusedEnvironment -> false
        // otherwise: underFocusedEnvironment -> true
        // GuiScreen doesnt count
        update(inputState, false);
    }
    public final void update(InputState inputState, boolean underFocusedEnvironment)
    {
        if (!FMLCommonHandler.instance().getSide().isClient()) return;

        if (initFlag)
        {
            initFlag = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiLifecycleInitEvent(lifecycleHolderName));
        }

        if (!Display.isActive() && !listenRegainScreenFocus)
            listenRegainScreenFocus = true;
        if (Display.isActive() && listenRegainScreenFocus)
        {
            listenRegainScreenFocus = false;
            MinecraftForge.EVENT_BUS.post(new IgiGuiLifecycleRegainScreenFocusEvent(lifecycleHolderName));
        }

        //<editor-fold desc="gui container init">
        for (IgiGuiContainer container : openedGuiMap.values())
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

        //<editor-fold desc="dummy screen">
        isUsingDummyScreen = MC.currentScreen instanceof IgiDummyScreen;
        boolean isScreenOn = MC.currentScreen != null;
        boolean isOtherScreenOn = MC.currentScreen != null && !(MC.currentScreen instanceof IgiDummyScreen);
        // skip first update
        if (finishFirstUpdate && !underFocusedEnvironment)
        {
            AtomicBoolean focus = new AtomicBoolean(false);
            openedGuiMap.forEach((mvvm, guiContainer) -> focus.set(focus.get() || (guiContainer.getFocused() && guiContainer.getActive())));
            boolean requireFocus = focus.get();

            // close dummy screen
            if (isUsingDummyScreen && !requireFocus)
            {
                isUsingDummyScreen = false;
                MC.displayGuiScreen(null);
            }

            // open dummy
            if (!isScreenOn && requireFocus)
            {
                isUsingDummyScreen = true;
                MC.displayGuiScreen(newDummyScreen());
            }

            // stop using other screen
            if (isUsingOtherScreen && !requireFocus)
                isUsingOtherScreen = false;
            if (isUsingOtherScreen && !isOtherScreenOn)
                isUsingOtherScreen = false;

            // use other screen as dummy
            if (isOtherScreenOn && requireFocus)
                isUsingOtherScreen = true;
        }
        //</editor-fold>

        //<editor-fold desc="propagate input">
        // skip first update
        if (finishFirstUpdate)
            for (IgiGuiContainer container: openedGuiMap.values())
                container.onPropagateInput(inputState);
        //</editor-fold>

        timing();

        if (doFixedUpdate)
        {
            doFixedUpdate = false;
            fixedUpdate();
        }

        if (!isUsingDummyScreen && !isUsingOtherScreen)
            renderUpdate();

        if (!finishFirstUpdate) finishFirstUpdate = true;
    }

    protected boolean doFixedUpdate = false;
    protected final void reserveFixedUpdate() { doFixedUpdate = true; }

    protected abstract void timing();
    protected abstract void fixedUpdate();
    protected abstract void renderUpdate();

    //<editor-fold desc="fixed update">
    // units are all in second
    protected int maxFps_FixedUpdate = 125;
    protected double timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    public final void setMaxFps_FixedUpdate(int fps)
    {
        maxFps_FixedUpdate = fps;
        timePerFrame_FixedUpdate = 1d / maxFps_FixedUpdate;
    }

    protected final void definedFixedUpdate(double deltaTime)
    {
        for (IgiGuiContainer container: openedGuiMap.values())
            container.onFixedUpdate(deltaTime);
    }
    //</editor-fold>

    //<editor-fold desc="render update">
    // units are all in second
    protected int maxFps_RenderUpdate = 240;
    protected double timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    public final void setMaxFps_RenderUpdate(int fps)
    {
        maxFps_RenderUpdate = fps;
        timePerFrame_RenderUpdate = 1d / maxFps_RenderUpdate;
    }

    private final VisualBuilderAccessor visualBuilderAccessor = InternalMethods.instance.VisualBuilderAccessor$constructor.invoke();

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
                RenderMask.resetStencil();
                RenderMask baseMask = new RenderMask(RenderMask.MaskShape.RECT);
                baseMask.setRectMask(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight());
                baseMask.startMasking();

                RenderContext context = new RenderContext(
                        container.getThemeConfig(),
                        getRenderLerpAlpha(),
                        !isUsingFramebuffer() && !isUsingMultisampleFramebuffer(),
                        !isUsingFramebuffer() && !isUsingMultisampleFramebuffer());

                RenderDecorator decorator = container.getRenderDecorator();

                RenderOpQueue queue = container.onRenderUpdate(entry.getKey().equals(firstFocused));
                IRenderOp op;
                while ((op = queue.dequeue()) != null)
                {
                    op.readTheme(context.theme);
                    if (!decorator.isEmpty() && decorator.isModifying(op.getClass()))
                    {
                        boolean abort = false;
                        List<IVisualModifier> modBefore = decorator.getModifiers(op.getClass(), RenderOpPhase.BEFORE_EXE);
                        List<IVisualModifier> modAfter = decorator.getModifiers(op.getClass(), RenderOpPhase.AFTER_EXE);

                        if (!modBefore.isEmpty())
                        {
                            VisualBuilder builder = new VisualBuilder();
                            for (IVisualModifier mod: modBefore)
                                mod.apply(builder);

                            visualBuilderAccessor.setVisualBuilder(builder);
                            abort = visualBuilderAccessor.getAbortRenderOp();
                            for (Tuple<VisualCommand, IArgsGenerator> command: visualBuilderAccessor.getCommands())
                                command.getFirst().execute(command.getSecond().genCommandArgs(context, op));
                        }

                        if (!abort) op.execute(context);

                        if (!modAfter.isEmpty())
                        {
                            VisualBuilder builder = new VisualBuilder();
                            for (IVisualModifier mod: modAfter)
                                mod.apply(builder);

                            visualBuilderAccessor.setVisualBuilder(builder);
                            for (Tuple<VisualCommand, IArgsGenerator> command: visualBuilderAccessor.getCommands())
                                command.getFirst().execute(command.getSecond().genCommandArgs(context, op));
                        }
                    }
                    else
                        op.execute(context);
                }

                RenderMask.endMasking();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="delegates">
    private static class DelegateDrawScreen implements IGuiScreenDrawScreen
    {
        private final GuiLifecycleProvider lifecycleProvider;
        private DelegateDrawScreen(GuiLifecycleProvider lifecycleProvider)
        {
            this.lifecycleProvider = lifecycleProvider;
        }

        @Override
        public void draw()
        {
            lifecycleProvider.renderUpdate();
        }
    }

    private static class DelegateKeyTyped implements IGuiScreenKeyTyped
    {
        private final GuiLifecycleProvider lifecycleProvider;
        private DelegateKeyTyped(GuiLifecycleProvider lifecycleProvider)
        {
            this.lifecycleProvider = lifecycleProvider;
        }

        @Override
        public void type(int keycode)
        {
            List<Map.Entry<String, IgiGuiContainer>> entryList = new ArrayList<>(lifecycleProvider.openedGuiMap.entrySet());
            String key = "";
            IFunc<Boolean> exitCallback = null;
            for (int i = entryList.size() - 1; i >= 0; i--)
            {
                Map.Entry<String, IgiGuiContainer> entry = entryList.get(i);
                IgiGuiContainer container = entry.getValue();
                if (container.getFocused() && container.getActive())
                {
                    if (keycode == container.getExitKeyForFocusedGui())
                    {
                        key = entry.getKey();
                        exitCallback = container.getExitCallback();
                    }
                    break;
                }
            }
            if (!key.isEmpty())
            {
                if (exitCallback.invoke())
                    lifecycleProvider.openedGuiMap.remove(key);
            }
        }
    }
    //</editor-fold>

    public final IGuiScreenDrawScreen GUI_SCREEN_DRAW_SCREEN = new DelegateDrawScreen(this);
    public final IGuiScreenKeyTyped GUI_SCREEN_KEY_TYPED = new DelegateKeyTyped(this);

    public abstract float getRenderLerpAlpha();
    protected abstract boolean isUsingFramebuffer();
    protected abstract boolean isUsingMultisampleFramebuffer();

    private IgiDummyScreen newDummyScreen()
    {
        IgiDummyScreen dummyGui = new IgiDummyScreen();
        dummyGui.setDrawAction(GUI_SCREEN_DRAW_SCREEN);
        dummyGui.setTypeAction(GUI_SCREEN_KEY_TYPED);
        return dummyGui;
    }
}
