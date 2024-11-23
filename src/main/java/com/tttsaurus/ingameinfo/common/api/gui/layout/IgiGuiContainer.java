package com.tttsaurus.ingameinfo.common.api.gui.layout;

import com.tttsaurus.ingameinfo.common.api.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.*;

public class IgiGuiContainer
{
    protected MainGroup mainGroup = new MainGroup();
    protected boolean isFocused = false;
    protected boolean hasFocusBackground = true;
    protected int backgroundColor = (new Color(80, 80, 80, 160)).getRGB();

    private boolean initFlag = false;

    //<editor-fold desc="getters & setters">
    public MainGroup getMainGroup() { return mainGroup; }

    public boolean getFocused() { return isFocused; }
    public void setFocused(boolean focused) { isFocused = focused; }

    public void setHasFocusBackground(boolean hasFocusBackground) { this.hasFocusBackground = hasFocusBackground; }

    public void setBackgroundColor(int color) { backgroundColor = color; }

    public boolean getInitFlag() { return initFlag; }
    //</editor-fold>

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
    public void onRenderUpdate()
    {
        if (isFocused && hasFocusBackground)
        {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            RenderUtils.renderGradientRect(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), backgroundColor, backgroundColor);
        }
        mainGroup.onRenderUpdate();

        mainGroup.renderDebugRect();
    }
}
