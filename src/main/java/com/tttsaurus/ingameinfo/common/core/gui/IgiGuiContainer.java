package com.tttsaurus.ingameinfo.common.core.gui;

import com.tttsaurus.ingameinfo.common.core.function.IFunc;
import com.tttsaurus.ingameinfo.common.core.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.core.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.core.mvvm.binding.SlotAccessor;
import com.tttsaurus.ingameinfo.common.core.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.core.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.core.render.RenderUtils;
import com.tttsaurus.ingameinfo.common.core.gui.theme.registry.ThemeRegistry;
import net.minecraft.client.renderer.GlStateManager;
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

    private boolean initFlag = false;
    private boolean isActive = true;
    private IFunc<Boolean> exitCallback = () -> true;

    //<editor-fold desc="getters">
    public boolean getActive() { return isActive; }
    public IFunc<Boolean> getExitCallback() { return exitCallback; }
    public int getExitKeyForFocusedGui() { return exitKeyForFocusedGui; }
    public boolean getFocused() { return isFocused; }
    public boolean getInitFlag() { return initFlag; }
    public boolean getUseHeldItemWhitelist() { return useHeldItemWhitelist; }
    public boolean getUseHeldItemBlacklist() { return useHeldItemBlacklist; }
    public List<GhostableItem> getHeldItemWhitelist() { return heldItemWhitelist; }
    public List<GhostableItem> getHeldItemBlacklist() { return heldItemBlacklist; }
    //</editor-fold>

    protected IgiGuiContainer() { }

    public void onInit()
    {
        if (initFlag) return;
        initFlag = true;

        mainGroup.loadTheme(ThemeRegistry.getTheme(themeName));

        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
        mainGroup.finishReCalc();

        InternalMethods.instance.ViewModel$isActiveGetter$setter.invoke(viewModel, () -> isActive);
        InternalMethods.instance.ViewModel$isActiveSetter$setter.invoke(viewModel, (flag) -> isActive = flag);
        InternalMethods.instance.ViewModel$exitCallbackSetter$setter.invoke(viewModel, (callback) -> exitCallback = callback);
        InternalMethods.instance.ViewModel$isFocusedGetter$setter.invoke(viewModel, () -> isFocused);
        InternalMethods.instance.ViewModel$isFocusedSetter$setter.invoke(viewModel, (focused) -> isFocused = focused);
        viewModel.start();
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

        List<SlotAccessor> slotAccessors = InternalMethods.instance.ViewModel$slotAccessors$getter.invoke(viewModel);
        for (SlotAccessor slotAccessor: slotAccessors)
            if (slotAccessor.getComposeBlock() != null)
                slotAccessor.getComposeBlock().update(ThemeRegistry.getTheme(themeName));

        viewModel.onFixedUpdate(deltaTime);
        mainGroup.onFixedUpdate(deltaTime);

        if (mainGroup.getNeedReCalc())
        {
            mainGroup.resetRenderInfo();
            mainGroup.calcWidthHeight();
            mainGroup.calcRenderPos(mainGroup.rect);
            mainGroup.finishReCalc();
        }
    }
    public void onRenderUpdate(boolean focused)
    {
        if (!isActive) return;

        if (isFocused && hasFocusBackground)
        {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            RenderUtils.renderRectFullScreen(backgroundColor);
        }

        mainGroup.onRenderUpdate(focused);

        if (debug) mainGroup.renderDebugRect();
    }

    // can't refresh <Def> content
    // viewModel.start() may not work properly due to the lack of `undo` function
    // todo: state preserving hotswap
    public void refreshVvm()
    {
        viewModel.refresh();
        mainGroup.loadTheme(ThemeRegistry.getTheme(themeName));
        viewModel.start();
    }
}
