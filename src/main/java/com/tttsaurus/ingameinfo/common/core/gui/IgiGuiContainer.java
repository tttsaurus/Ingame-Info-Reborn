package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.Func;
import com.tttsaurus.ingameinfo.common.core.gui.render.decorator.RenderDecorator;
import com.tttsaurus.ingameinfo.common.core.input.InputState;
import com.tttsaurus.ingameinfo.common.core.gui.render.RenderOpQueue;
import com.tttsaurus.ingameinfo.common.core.gui.theme.ThemeConfig;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.commonutils.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.SlotAccessor;
import com.tttsaurus.ingameinfo.common.core.mvvm.compose.ComposeBlock;
import com.tttsaurus.ingameinfo.common.core.mvvm.registry.MvvmRegistry;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.gui.theme.registry.ThemeRegistry;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class IgiGuiContainer
{
    protected ViewModel<?> viewModel;

    protected boolean useHeldItemWhitelist = false;
    protected final List<GhostableItem> heldItemWhitelist = new ArrayList<>();
    protected boolean useHeldItemBlacklist = false;
    protected final List<GhostableItem> heldItemBlacklist = new ArrayList<>();

    // alias of View.mainGroup
    protected MainGroup mainGroup = new MainGroup();
    protected boolean debug = false;
    protected int exitKeyForFocusedGui = Keyboard.KEY_ESCAPE;
    protected boolean isFocused = false;
    protected boolean hasFocusBackground = true;
    protected int backgroundColor = -1072689136;
    protected String themeName = "default";

    private ThemeConfig themeConfig;

    private boolean prepareGuiOpen = false;
    private boolean initFlag = false;
    private boolean isActive = true;
    private Func<Boolean> exitCallback = () -> true;

    private void resetDef()
    {
        useHeldItemWhitelist = false;
        heldItemWhitelist.clear();
        useHeldItemBlacklist = false;
        heldItemBlacklist.clear();
        debug = false;
        exitKeyForFocusedGui = Keyboard.KEY_ESCAPE;
        isFocused = false;
        hasFocusBackground = true;
        backgroundColor = -1072689136;
        themeName = "default";
    }

    //<editor-fold desc="getters">
    public boolean getPrepareGuiOpen() { return prepareGuiOpen; }
    public boolean getActive() { return isActive; }
    public Func<Boolean> getExitCallback() { return exitCallback; }
    public int getExitKeyForFocusedGui() { return exitKeyForFocusedGui; }
    public boolean getFocused() { return isFocused; }
    public boolean getUseHeldItemWhitelist() { return useHeldItemWhitelist; }
    public boolean getUseHeldItemBlacklist() { return useHeldItemBlacklist; }
    public List<GhostableItem> getHeldItemWhitelist() { return heldItemWhitelist; }
    public List<GhostableItem> getHeldItemBlacklist() { return heldItemBlacklist; }
    public ThemeConfig getThemeConfig() { return themeConfig; }
    public RenderDecorator getRenderDecorator() { return InternalMethods.ViewModel$getRenderDecorator(viewModel); }
    //</editor-fold>

    protected IgiGuiContainer() { }

    public void onInit()
    {
        if (initFlag) return;
        initFlag = true;

        themeConfig = ThemeRegistry.getTheme(themeName);
        mainGroup.applyLogicTheme(themeConfig);

        InternalMethods.ViewModel$isActiveGetter$setter(viewModel, () -> isActive);
        InternalMethods.ViewModel$isActiveSetter$setter(viewModel, (flag) -> isActive = flag);
        InternalMethods.ViewModel$exitCallbackSetter$setter(viewModel, (callback) -> exitCallback = callback);
        InternalMethods.ViewModel$isFocusedGetter$setter(viewModel, () -> isFocused);
        InternalMethods.ViewModel$isFocusedSetter$setter(viewModel, (focused) -> isFocused = focused);
        viewModel.onStart();

        List<SlotAccessor> slotAccessors = InternalMethods.ViewModel$slotAccessors$getter(viewModel);
        for (SlotAccessor slotAccessor: slotAccessors)
        {
            ComposeBlock composeBlock = slotAccessor.getComposeBlock();
            if (composeBlock != null)
            {
                composeBlock.updateTheme(themeConfig);
                // first compose update is to add elements and applyLogicTheme
                composeBlock.update(0d, InternalMethods.ViewModel$sharedContext$getter(viewModel));
            }
        }

        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
        mainGroup.finishReCalc();

        mainGroup.onCollectLerpInfo();
    }

    public void onScaledResolutionResize()
    {
        mainGroup.resetRenderInfo();
        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
        mainGroup.finishReCalc();
    }

    public void onFixedUpdate(double deltaTime)
    {
        if (!isActive) return;

        List<SlotAccessor> slotAccessors = InternalMethods.ViewModel$slotAccessors$getter(viewModel);
        for (SlotAccessor slotAccessor: slotAccessors)
            if (slotAccessor.getComposeBlock() != null)
                slotAccessor.getComposeBlock().update(deltaTime, InternalMethods.ViewModel$sharedContext$getter(viewModel));

        viewModel.onFixedUpdate(deltaTime);
        mainGroup.onFixedUpdate(deltaTime);

        if (mainGroup.getNeedReCalc())
        {
            mainGroup.resetRenderInfo();
            mainGroup.calcWidthHeight();
            mainGroup.calcRenderPos(mainGroup.rect);
            mainGroup.finishReCalc();
        }

        mainGroup.onCollectLerpInfo();
    }

    public RenderOpQueue onRenderUpdate(boolean focused)
    {
        if (!isActive) return new RenderOpQueue();

        if (isFocused && hasFocusBackground)
            RenderUtils.renderRectFullScreen(backgroundColor);

        RenderOpQueue queue = new RenderOpQueue();
        mainGroup.onRenderUpdate(queue, focused);

        if (debug) mainGroup.renderDebugRect(queue);

        return queue;
    }

    public void onPropagateInput(InputState inputState)
    {
        if (!isActive) return;

        mainGroup.onPropagateInput(inputState);
    }

    public void prepareGuiOpen()
    {
        prepareGuiOpen = true;
    }

    public void onGuiOpen()
    {
        prepareGuiOpen = false;
        viewModel.onGuiOpen();
    }

    public void onGuiClose()
    {
        viewModel.onGuiClose();
    }

    // viewModel.start() may not work properly due to the lack of `undo` function
    // todo: state preserving hotswap
    public void refreshVvm(MvvmRegistry mvvmRegistry)
    {
        resetDef();
        viewModel.refresh(this, mvvmRegistry);

        themeConfig = ThemeRegistry.getTheme(themeName);
        mainGroup.applyLogicTheme(themeConfig);

        viewModel.onStart();

        List<SlotAccessor> slotAccessors = InternalMethods.ViewModel$slotAccessors$getter(viewModel);
        for (SlotAccessor slotAccessor: slotAccessors)
        {
            ComposeBlock composeBlock = slotAccessor.getComposeBlock();
            if (composeBlock != null)
            {
                composeBlock.clear();
                composeBlock.updateTheme(themeConfig);
                // first compose update is to add elements and applyLogicTheme
                composeBlock.update(0d, InternalMethods.ViewModel$sharedContext$getter(viewModel));
            }
        }

        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
        mainGroup.finishReCalc();

        mainGroup.onCollectLerpInfo();
    }
}
