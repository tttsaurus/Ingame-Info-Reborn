package com.tttsaurus.ingameinfo.common.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class PlaceholderMcGui extends GuiScreen
{
    private IPlaceholderDrawScreen drawAction = null;
    public void setDrawAction(IPlaceholderDrawScreen drawAction) { this.drawAction = drawAction; }

    @Override
    public void initGui()
    {
        Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
    }

    @Override
    public void onGuiClosed()
    {
        Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
        Minecraft.getMinecraft().inGameHasFocus = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (drawAction != null) drawAction.draw();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
