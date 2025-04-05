package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.api.function.IFunc;
import com.tttsaurus.ingameinfo.common.api.internal.InternalMethods;
import com.tttsaurus.ingameinfo.common.api.item.GhostableItem;
import com.tttsaurus.ingameinfo.common.api.mvvm.viewmodel.ViewModel;
import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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

    protected MainGroup mainGroup = new MainGroup();
    protected boolean debug = false;
    protected int exitKeyForFocusedGui = Keyboard.KEY_ESCAPE;
    protected boolean isFocused = false;
    protected boolean hasFocusBackground = true;
    protected int backgroundColor = -1072689136;

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

        viewModel.onFixedUpdate(deltaTime);
        mainGroup.onFixedUpdate(deltaTime);
    }
    public void onRenderUpdate(boolean focused)
    {
        if (!isActive) return;

        if (isFocused && hasFocusBackground)
        {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtils.renderGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), backgroundColor, backgroundColor);
        }

        if (mainGroup.getNeedReCalc())
        {
            mainGroup.resetRenderInfo();
            mainGroup.calcWidthHeight();
            mainGroup.calcRenderPos(mainGroup.rect);
            mainGroup.finishReCalc();
        }

        mainGroup.renderBackground();
        mainGroup.onRenderUpdate(focused);

        if (debug) mainGroup.renderDebugRect();
    }

    public void refresh()
    {
        viewModel.refresh();
    }
}
