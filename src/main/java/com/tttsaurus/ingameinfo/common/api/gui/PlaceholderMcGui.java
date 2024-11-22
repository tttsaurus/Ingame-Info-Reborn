package com.tttsaurus.ingameinfo.common.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class PlaceholderMcGui extends GuiScreen
{
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
            Minecraft.getMinecraft().displayGuiScreen(null);
    }

    @Override
    public void initGui()
    {
        Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();

        this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height / 2, 100, 20, "Close GUI"));
    }

    @Override
    public void onGuiClosed()
    {
        Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
        Minecraft.getMinecraft().inGameHasFocus = true;
    }

    public Runnable runnable = null;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Render buttons and other elements
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (runnable != null) runnable.run();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
