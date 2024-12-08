package com.tttsaurus.ingameinfo.common.api.gui;

import com.tttsaurus.ingameinfo.common.impl.gui.layout.MainGroup;
import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class IgiGuiContainer
{
    protected boolean debug = false;
    protected int exitKeyForFocusedGui = Keyboard.KEY_ESCAPE;
    protected MainGroup mainGroup = new MainGroup();
    protected boolean isFocused = false;
    protected boolean hasFocusBackground = true;
    protected int backgroundColor = -1072689136;

    private boolean initFlag = false;

    //<editor-fold desc="getters & setters">
    public void setDebug(boolean debug) { this.debug = debug; }

    public int getExitKeyForFocusedGui() { return exitKeyForFocusedGui; }
    public void setExitKeyForFocusedGui(int keycode) { exitKeyForFocusedGui = keycode; }

    public boolean getFocused() { return isFocused; }
    public void setFocused(boolean focused) { isFocused = focused; }

    public void setHasFocusBackground(boolean hasFocusBackground) { this.hasFocusBackground = hasFocusBackground; }

    public void setBackgroundColor(int color) { backgroundColor = color; }

    public boolean getInitFlag() { return initFlag; }
    //</editor-fold>

    protected IgiGuiContainer()
    {

    }

    public void onInit()
    {
        if (initFlag) return;
        initFlag = true;

        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
    }
    public void onScaledResolutionResize()
    {
        mainGroup.resetRenderInfo();
        mainGroup.calcWidthHeight();
        mainGroup.calcRenderPos(mainGroup.rect);
    }
    public void onFixedUpdate(double deltaTime)
    {
        mainGroup.onFixedUpdate(deltaTime);
    }
    public void onRenderUpdate(boolean focused)
    {
        if (isFocused && hasFocusBackground)
        {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtils.renderGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), backgroundColor, backgroundColor);
        }
        mainGroup.renderBackground();
        mainGroup.onRenderUpdate(focused);

        if (debug) mainGroup.renderDebugRect();
    }
}
